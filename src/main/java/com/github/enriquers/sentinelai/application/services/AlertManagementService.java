package com.github.enriquers.sentinelai.application.services;

import com.github.enriquers.sentinelai.domain.model.Alert;
import com.github.enriquers.sentinelai.domain.model.AlertSeverity;
import com.github.enriquers.sentinelai.domain.ports.in.ProcessAlertUseCase;
import com.github.enriquers.sentinelai.domain.ports.in.ReportAlertUseCase;
import com.github.enriquers.sentinelai.domain.ports.out.AlertEventPublisherPort;
import com.github.enriquers.sentinelai.domain.ports.out.AlertRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertManagementService implements ProcessAlertUseCase, ReportAlertUseCase {

  private static final Logger log = LoggerFactory.getLogger(AlertManagementService.class);
  private final AlertRepositoryPort alertRepositoryPort;
  private final AlertEventPublisherPort alertEventPublisherPort;

  public AlertManagementService(AlertRepositoryPort alertRepositoryPort, AlertEventPublisherPort alertEventPublisherPort) {
    this.alertRepositoryPort = alertRepositoryPort;
    this.alertEventPublisherPort = alertEventPublisherPort;
  }

  @Override
  public void process(Alert alert) {
    alertRepositoryPort.save(alert);

    if(alert.getSeverity() == AlertSeverity.CRITICAL) {
      log.error("Critical alert received: {} from Service: {}", alert, alert.getServiceName());
    } else {
      log.info("Alert received: {} from Service: {}", alert, alert.getServiceName());
    }
    alert.markAsAnalyzing();
  }

  @Override
  public void report(Alert alert) {
    alertEventPublisherPort.publish(alert);
  }
}
