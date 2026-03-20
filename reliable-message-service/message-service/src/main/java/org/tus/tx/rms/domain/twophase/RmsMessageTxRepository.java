package org.tus.tx.rms.domain.twophase;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RmsMessageTxRepository extends JpaRepository<RmsMessageTxEntity, Long> {

    Optional<RmsMessageTxEntity> findByMessageId(String messageId);

    @Query("SELECT m FROM RmsMessageTxEntity m WHERE m.status = :status AND (m.nextRetryAt IS NULL OR m.nextRetryAt <= :now) ORDER BY m.createdAt ASC")
    List<RmsMessageTxEntity> findDueForRelay(@Param("status") String status, @Param("now") Instant now, Pageable pageable);
}
