package com.github.enriquers.sentinelai.infrastructure.adapters.out.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.enriquers.sentinelai.domain.model.Alert;
import com.github.enriquers.sentinelai.domain.ports.out.AlertEventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisherAdapter implements AlertEventPublisherPort {

  @Value("${messaging.kafka.topic.alerts}")
  private String topic;

  private final ObjectMapper objectMapper;

  private final KafkaTemplate<String, String> kafkaTemplate;


  @Override
  public void publish(Alert alert) {
    try {
      String alertJson = objectMapper.writeValueAsString(alert);
      kafkaTemplate.send(topic, alert.getId().toString(), alertJson);
      log.info("📤 Published alert to Kafka topic {}: {}", topic, alertJson);
    } catch (JsonProcessingException e) {
      log.error("❌ Failed to serialize alert to JSON", e);
    }
  }
}
