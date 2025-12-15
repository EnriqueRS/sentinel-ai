package com.github.enriquers.sentinelai.domain.ports.in;

import com.github.enriquers.sentinelai.domain.model.Alert;

public interface ProcessAlertUseCase {
  void process(Alert alert);
}
