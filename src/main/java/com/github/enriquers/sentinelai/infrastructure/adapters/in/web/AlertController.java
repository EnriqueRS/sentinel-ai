package com.github.enriquers.sentinelai.infrastructure.adapters.in.web;

import static org.springframework.http.HttpStatus.CREATED;

import com.github.enriquers.sentinelai.domain.model.Alert;
import com.github.enriquers.sentinelai.domain.ports.in.ReportAlertUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

  private final ReportAlertUseCase reportAlertUseCase;

  @PostMapping
  @ResponseStatus(CREATED)
  public void reportAlert(@RequestBody AlertRequestDTO alertRequestDTO) {
    log.info("🚨 Received alert report request: {}", alertRequestDTO);
    Alert alert = AlertRestMapper.toDomain(alertRequestDTO);
    reportAlertUseCase.report(alert);
  }

}
