package com.github.enriquers.sentinelai.infrastructure.adapters.in.web;

import com.github.enriquers.sentinelai.domain.model.Alert;
import com.github.enriquers.sentinelai.domain.model.AlertSeverity;
import java.time.Instant;
import java.util.UUID;

public class AlertRestMapper {

  public static Alert toDomain(AlertRequestDTO dto) {
    if (dto == null) {
      return null;
    }
    return Alert.create(
        UUID.fromString(dto.id()),
        dto.serviceName(),
        AlertSeverity.valueOf(dto.severity()),
        dto.message(),
        Instant.parse(dto.occurredAt())
    );
  }

}
