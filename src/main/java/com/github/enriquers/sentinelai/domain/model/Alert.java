package com.github.enriquers.sentinelai.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Alert {

  private final UUID id;
  private final String serviceName;
  private final AlertSeverity severity;
  private AlertStatus status;
  private final String message;
  private final Instant occurredAt;
  private Instant attendedAt;

  public static Alert create(UUID id, String serviceName, AlertSeverity severity, String message, Instant occurredAt) {
    return new Alert(id, serviceName, severity, message, occurredAt, AlertStatus.PENDING, null);
  }

  public Alert(UUID id, String serviceName, AlertSeverity severity, String message, Instant occurredAt, AlertStatus status, Instant attendedAt) {
    validateState(id, serviceName, severity, message, occurredAt, status);

    this.id = id;
    this.serviceName = serviceName;
    this.severity = severity;
    this.message = message;
    this.occurredAt = occurredAt;
    this.status = status;
    this.attendedAt = attendedAt;
  }


  public void markAsAnalyzing() {
    if (this.status == AlertStatus.RESOLVED) {
      throw new IllegalStateException("Cannot analyze an alert that is already resolved.");
    }
    this.status = AlertStatus.ANALYZING;
    this.attendedAt = Instant.now();
  }

  public void resolve() {
    if (this.status == AlertStatus.PENDING) {
      throw new IllegalStateException("Cannot resolve an alert that is still pending analysis.");
    }
    this.status = AlertStatus.RESOLVED;
  }


  private void validateState(UUID id, String serviceName, AlertSeverity severity, String message, Instant occurredAt, AlertStatus status) {
    Objects.requireNonNull(id, "Alert ID cannot be null");
    Objects.requireNonNull(severity, "Severity cannot be null");
    Objects.requireNonNull(occurredAt, "OccurredAt cannot be null");
    Objects.requireNonNull(status, "Status cannot be null");

    if (serviceName == null || serviceName.isBlank()) {
      throw new IllegalArgumentException("Service name cannot be empty");
    }
    if (message == null || message.isBlank()) {
      throw new IllegalArgumentException("Message cannot be empty");
    }
  }

  public UUID getId() { return id; }
  public String getServiceName() { return serviceName; }
  public AlertSeverity getSeverity() { return severity; }
  public String getMessage() { return message; }
  public Instant getOccurredAt() { return occurredAt; }
  public AlertStatus getStatus() { return status; }
  public Instant getAttendedAt() { return attendedAt; }
}
