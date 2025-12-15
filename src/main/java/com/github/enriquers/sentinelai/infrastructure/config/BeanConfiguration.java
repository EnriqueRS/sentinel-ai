package com.github.enriquers.sentinelai.infrastructure.config;

import com.github.enriquers.sentinelai.application.services.AlertManagementService;
import com.github.enriquers.sentinelai.domain.ports.in.ProcessAlertUseCase;
import com.github.enriquers.sentinelai.domain.ports.out.AlertRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

  @Bean
  public ProcessAlertUseCase processAlertUseCase(AlertRepositoryPort alertRepositoryPort) {
    return new AlertManagementService(alertRepositoryPort);
  }
}
