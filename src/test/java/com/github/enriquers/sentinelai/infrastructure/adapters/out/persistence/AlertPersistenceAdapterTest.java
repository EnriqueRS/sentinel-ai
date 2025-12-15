package com.github.enriquers.sentinelai.infrastructure.adapters.out.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.enriquers.sentinelai.domain.model.Alert;
import com.github.enriquers.sentinelai.domain.model.AlertSeverity;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AlertPersistenceAdapter.class})
public class AlertPersistenceAdapterTest {
  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

  @Autowired
  private AlertPersistenceAdapter adapter;

  @Autowired
  private SpringDataAlertRepository springRepository;

  @Test
  @DisplayName("Should save alert successfully")
  void should_save_alert_successfully() {
    UUID uuid = UUID.randomUUID();
    Alert alert = Alert.create(
        uuid,
        "payment-service",
        AlertSeverity.CRITICAL,
        "DB connection failed",
        Instant.now()
    );

    // 2. Act: Usar el adapter para guardar
    adapter.save(alert);

    // 3. Assert: Verificar que está en la base de datos real
    Optional<AlertEntity> alertEntity = springRepository.findById(uuid);
    assertTrue(alertEntity.isPresent());
    assertEquals("payment-service", alertEntity.get().getServiceName());
    assertEquals(AlertSeverity.CRITICAL, alertEntity.get().getSeverity());
    assertEquals("DB connection failed", alertEntity.get().getMessage());
    assertEquals(uuid, alertEntity.get().getId());
  }
}
