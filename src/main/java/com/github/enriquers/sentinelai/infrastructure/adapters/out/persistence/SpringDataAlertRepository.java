package com.github.enriquers.sentinelai.infrastructure.adapters.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAlertRepository extends JpaRepository<AlertEntity, UUID> {
}
