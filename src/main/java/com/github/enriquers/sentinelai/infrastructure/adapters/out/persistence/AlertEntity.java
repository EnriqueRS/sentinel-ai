package com.github.enriquers.sentinelai.infrastructure.adapters.out.persistence;

import com.github.enriquers.sentinelai.domain.model.AlertSeverity;
import com.github.enriquers.sentinelai.domain.model.AlertStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "alerts")
public class AlertEntity {

  @Id
  private UUID id;
  private String serviceName;

  @Enumerated(EnumType.STRING)
  private AlertSeverity severity;

  @Enumerated(EnumType.STRING)
  private AlertStatus status;

  private String message;
  private Instant occurredAt;
  private Instant attendedAt;
}
