package com.github.enriquers.sentinelai.application.services;

import com.github.enriquers.sentinelai.domain.model.Alert;
import com.github.enriquers.sentinelai.domain.model.AlertSeverity;
import com.github.enriquers.sentinelai.domain.ports.in.ProcessAlertUseCase;
import com.github.enriquers.sentinelai.domain.ports.out.AlertRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertManagementService implements ProcessAlertUseCase {

  private static final Logger log = LoggerFactory.getLogger(AlertManagementService.class);
  private final AlertRepositoryPort alertRepositoryPort;

  public AlertManagementService(AlertRepositoryPort alertRepositoryPort) {
    this.alertRepositoryPort = alertRepositoryPort;
  }

  @Override
  public void process(Alert alert) {
    alertRepositoryPort.save(alert);

    if(alert.getSeverity() == AlertSeverity.CRITICAL) {
      log.error("Critical alert received: {} from Service: {}", alert, alert.getServiceName());
    } else {
      log.info("Alert received: {} from Service: {}", alert, alert.getServiceName());
    }
  }
}
