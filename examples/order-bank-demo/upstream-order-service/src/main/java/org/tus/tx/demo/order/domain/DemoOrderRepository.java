package org.tus.tx.demo.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DemoOrderRepository extends JpaRepository<DemoOrder, Long> {

    Optional<DemoOrder> findByOrderNo(String orderNo);
}
