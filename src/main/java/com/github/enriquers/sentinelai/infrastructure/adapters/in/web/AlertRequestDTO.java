package com.github.enriquers.sentinelai.infrastructure.adapters.in.web;

public record AlertRequestDTO(
    String id,
    String serviceName,
    String message,
    String severity,
    String occurredAt
) {

}