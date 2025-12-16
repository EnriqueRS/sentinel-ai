package com.github.enriquers.sentinelai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.enriquers.sentinelai.domain.model.AlertSeverity;
import com.github.enriquers.sentinelai.infrastructure.adapters.in.listener.KafkaAlertListener;
import com.github.enriquers.sentinelai.infrastructure.adapters.out.persistence.AlertEntity;
import com.github.enriquers.sentinelai.infrastructure.adapters.out.persistence.SpringDataAlertRepository;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({KafkaAlertListener.class})
@Testcontainers
class SentinelAiApplicationTests {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

  @Container
  static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private SpringDataAlertRepository alertRepository;

  @Test
  @DisplayName("Send JSON to system.alerts and verify DB persistence")
  void should_create_valid_alert() throws Exception {
    String topic = "system.alerts";
    UUID uuid = UUID.fromString("e73201a7-9b66-4ccc-b182-e5a7b0f56797");
    String serviceName = "payment-service";
    String message = "DB connection failed";
    String json = """
        {
          "id": "%s",
          "serviceName": "%s",
          "severity": "CRITICAL",
          "message": "%s",
          "occurredAt": "2024-06-01T12:00:00Z"
        }
        """.formatted(uuid, serviceName, message);

    // Send message
    kafkaTemplate.send(topic, json).get();

    // Wait until the alert is persisted in the alerts table (adjust table name if different)
    Awaitility.await()
        .atMost(Duration.ofSeconds(10))
        .pollInterval(Duration.ofMillis(500))
        .untilAsserted(() -> {
          Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM alerts", Integer.class);
          assertTrue(count != null && count > 0, "Expected at least one persisted alert in alerts table");
        });

    Optional<AlertEntity> alertEntity = alertRepository.findById(uuid);
    assertTrue(alertEntity.isPresent());
    assertEquals(serviceName, alertEntity.get().getServiceName());
    assertEquals(AlertSeverity.CRITICAL, alertEntity.get().getSeverity());
    assertEquals(message, alertEntity.get().getMessage());
    assertEquals(uuid, alertEntity.get().getId());
  }

}
