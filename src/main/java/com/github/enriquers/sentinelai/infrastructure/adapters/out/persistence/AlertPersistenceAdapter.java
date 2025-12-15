package com.github.enriquers.sentinelai.infrastructure.adapters.out.persistence;

import com.github.enriquers.sentinelai.domain.model.Alert;
import com.github.enriquers.sentinelai.domain.ports.out.AlertRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AlertPersistenceAdapter implements AlertRepositoryPort {

  private final SpringDataAlertRepository springDataAlertRepository;

  @Override
  public Alert save(Alert alert) {
    AlertEntity alertEntity = AlertMapper.fromAlert(alert);
    springDataAlertRepository.save(alertEntity);
    return alert;
  }
}
