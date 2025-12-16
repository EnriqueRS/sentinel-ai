package com.github.enriquers.sentinelai.infrastructure.adapters.in.listener;

public record AlertDTO(
    String id,
    String serviceName,
    String message,
    String severity,
    String occurredAt
) {

}
