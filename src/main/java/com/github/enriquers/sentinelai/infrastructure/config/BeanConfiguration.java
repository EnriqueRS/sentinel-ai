package com.github.enriquers.sentinelai.infrastructure.config;

import com.github.enriquers.sentinelai.application.services.AlertManagementService;
import com.github.enriquers.sentinelai.domain.ports.in.ProcessAlertUseCase;
import com.github.enriquers.sentinelai.domain.ports.in.ReportAlertUseCase;
import com.github.enriquers.sentinelai.domain.ports.out.AlertEventPublisherPort;
import com.github.enriquers.sentinelai.domain.ports.out.AlertRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
  @Bean
  public AlertManagementService alertManagementService(AlertRepositoryPort repo, AlertEventPublisherPort publisher) {
    return new AlertManagementService(repo, publisher);
  }

  @Bean
  public ProcessAlertUseCase processAlertUseCase(AlertManagementService service) {
    return service;
  }

  @Bean
  public ReportAlertUseCase reportAlertUseCase(AlertManagementService service) {
    return service;
  }
}
