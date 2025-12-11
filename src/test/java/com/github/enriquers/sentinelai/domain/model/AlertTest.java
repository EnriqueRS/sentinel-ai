package com.github.enriquers.sentinelai.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class AlertTest {

  @Test
  @DisplayName("Should create valid alert")
  public void should_create_valid_alert(){
    UUID id = UUID.randomUUID();
    String serviceName = "users-service";
    AlertSeverity severity = AlertSeverity.WARNING;
    String message = "Example alert message";
    java.time.Instant occurredAt = java.time.Instant.now();

    Alert alert = Alert.create(id, serviceName, severity, message, occurredAt);

    assertNotNull(alert);
    assertEquals(id, alert.getId());
    assertEquals(serviceName, alert.getServiceName());
    assertEquals(severity, alert.getSeverity());
    assertEquals(message, alert.getMessage());
    assertEquals(occurredAt, alert.getOccurredAt());
    assertEquals(AlertStatus.PENDING, alert.getStatus());
    assertNull(alert.getAttendedAt());
  }

  @Test
  @DisplayName("Should fail when service name is empty")
  public void should_fail_when_service_name_is_empty() {
    UUID id = UUID.randomUUID();
    String serviceName = "";
    AlertSeverity severity = AlertSeverity.CRITICAL;
    String message = "Critical alert message";
    java.time.Instant occurredAt = java.time.Instant.now();

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      Alert.create(id, serviceName, severity, message, occurredAt);
    });

    assertEquals("Service name cannot be empty", exception.getMessage());
  }

  @Test
  @DisplayName("Should fail when resolve pending alert")
  public void should_fail_when_resolve_pending_alert() {
    UUID id = UUID.randomUUID();
    String serviceName = "users-service";
    AlertSeverity severity = AlertSeverity.CRITICAL;
    String message = "Critical alert message";
    java.time.Instant occurredAt = java.time.Instant.now();

    Alert alert = Alert.create(id, serviceName, severity, message, occurredAt);

    IllegalStateException exception = assertThrows(IllegalStateException.class, alert::resolve);

    assertEquals("Cannot resolve an alert that is still pending analysis.", exception.getMessage());
  }

}