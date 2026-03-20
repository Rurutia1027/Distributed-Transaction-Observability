package org.tus.demo.order.web;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.tus.demo.order.service.OrderCheckoutService;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/demo/orders")
@RequiredArgsConstructor
public class OrderDemoController {

    private final OrderCheckoutService checkoutService;

    @PostMapping("/checkout")
    public Map<String, Object> checkout(@RequestBody CheckoutBody body) throws Exception {
        String orderNo = checkoutService.checkout(body.getAmount());
        return Map.of("orderNo", orderNo, "ok", true);
    }

    @Data
    public static class CheckoutBody {
        private BigDecimal amount;
    }
}
