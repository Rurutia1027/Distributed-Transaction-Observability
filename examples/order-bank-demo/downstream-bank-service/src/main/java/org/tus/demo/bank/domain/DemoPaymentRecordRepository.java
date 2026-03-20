package org.tus.demo.bank.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DemoPaymentRecordRepository extends JpaRepository<DemoPaymentRecordEntity, Long> {
    Optional<DemoPaymentRecordEntity> findByMessageId(String messageId);
}
