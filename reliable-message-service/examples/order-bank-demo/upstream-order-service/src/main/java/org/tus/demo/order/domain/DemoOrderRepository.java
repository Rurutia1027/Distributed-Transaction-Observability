package org.tus.demo.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DemoOrderRepository extends JpaRepository<DemoOrderEntity, Long> {

    Optional<DemoOrderEntity> findByOrderNo(String orderNo);
}
