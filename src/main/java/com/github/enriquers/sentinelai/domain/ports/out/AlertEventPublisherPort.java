package com.github.enriquers.sentinelai.domain.ports.out;

import com.github.enriquers.sentinelai.domain.model.Alert;

public interface AlertEventPublisherPort {
  void publish(Alert alert);
}