package org.tus.tx.rms.domain.twophase;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RmsMessageResultRepository extends
        JpaRepository<RmsMessageResultEntity, Long> {
}
