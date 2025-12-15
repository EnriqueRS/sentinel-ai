package com.github.enriquers.sentinelai.infrastructure.adapters.out.persistence;

import com.github.enriquers.sentinelai.domain.model.Alert;

public class AlertMapper {

  public static AlertEntity fromAlert(Alert alert){
    if(alert == null) {
      return null;
    }
    return AlertEntity.builder()
        .id(alert.getId())
        .serviceName(alert.getServiceName())
        .severity(alert.getSeverity())
        .status(alert.getStatus())
        .message(alert.getMessage())
        .occurredAt(alert.getOccurredAt())
        .attendedAt(alert.getAttendedAt())
        .build();
  }

  public static Alert toDomain(AlertEntity entity) {
    if (entity == null) return null;
    return new Alert(
        entity.getId(),
        entity.getServiceName(),
        entity.getSeverity(),
        entity.getMessage(),
        entity.getOccurredAt(),
        entity.getStatus(),
        entity.getAttendedAt()
    );
  }

}
