package org.tus.tx.rms.domain.outbox;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RmsOutboxEventRepository extends JpaRepository<RmsOutboxEventEntity, Long> {

    Optional<RmsOutboxEventEntity> findByEventId(String eventId);

    @Query("SELECT e FROM RmsOutboxEventEntity e WHERE e.readyToSend = true AND e.status = :status AND (e.nextRetryAt IS NULL OR e.nextRetryAt <= :now) ORDER BY e.createdAt ASC")
    List<RmsOutboxEventEntity> findDueForRelay(@Param("status") String status, @Param("now") Instant now, Pageable pageable);
}
