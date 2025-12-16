package com.github.enriquers.sentinelai.infrastructure.adapters.in.listener;

import com.github.enriquers.sentinelai.domain.model.Alert;
import com.github.enriquers.sentinelai.domain.model.AlertSeverity;
import com.github.enriquers.sentinelai.domain.model.AlertStatus;
import java.time.Instant;
import java.util.UUID;

public class AlertMapper {


  public static Alert toDomain(AlertDTO alertDTO) {
    if (alertDTO == null) return null;
    return new Alert(
        UUID.fromString(alertDTO.id()),
        alertDTO.service(),
        AlertSeverity.valueOf(alertDTO.severity()),
        alertDTO.message(),
        Instant.parse(alertDTO.timestamp()),
        AlertStatus.PENDING,
        null
    );
  }
}
