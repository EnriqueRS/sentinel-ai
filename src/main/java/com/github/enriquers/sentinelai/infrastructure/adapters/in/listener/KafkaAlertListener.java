package com.github.enriquers.sentinelai.infrastructure.adapters.in.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.enriquers.sentinelai.domain.model.Alert;
import com.github.enriquers.sentinelai.domain.model.AlertSeverity;
import com.github.enriquers.sentinelai.domain.ports.in.ProcessAlertUseCase;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaAlertListener {

  private final ProcessAlertUseCase processAlertUseCase;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "system.alerts", groupId = "sentinel-backend-group")
  public void listen(String kafkaMessage) {
    log.info("📥 Message received from Kafka: {}", kafkaMessage);
    try {
      AlertDTO dto = parseMessage(kafkaMessage);
      Alert domainAlert = mapToDomain(dto);
      processAlertUseCase.process(domainAlert);
    } catch (Exception e) {
      // TODO Dead Letter Queue (DLQ)
      log.error("❌ Error processing alert", e);
    }
  }

  private Alert mapToDomain(AlertDTO dto) {
    return Alert.create(
        UUID.fromString(dto.id()),
        dto.serviceName(),
        AlertSeverity.valueOf(dto.severity()),
        dto.message(),
        Instant.parse(dto.occurredAt())
    );
  }

  private AlertDTO parseMessage(String kafkaMessage) {
    try {
      return objectMapper.readValue(kafkaMessage, AlertDTO.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to parse Kafka message", e);
    }
  }
}
