package com.eda.tracing.repository;

import com.eda.tracing.domain.OrderRevision;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRevisionRepository extends JpaRepository<OrderRevision, Long> {
}
