package org.tus.tx.demo.order.web;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tus.tx.demo.order.domain.DemoOrder;
import org.tus.tx.demo.order.service.OrderCheckoutService;

@RestController
@RequestMapping("/api/orders")
public class OrderDemoController {

    private final OrderCheckoutService checkoutService;

    public OrderDemoController(OrderCheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/checkout")
    public DemoOrder checkout(@Valid @RequestBody CheckoutRequest req) {
        return checkoutService.checkout(req.getOrderNo(), req.getAmountCents(), req.getTraceId());
    }
}
