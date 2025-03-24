package com.training.OrderService.controller;

import com.training.OrderService.model.Order;
import com.training.OrderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public void placeOrder(
            @RequestParam Long customerId,
            @RequestParam String productId,
            @RequestParam int quantity,
            @RequestParam BigDecimal totalPrice) {

        Order order = orderService.placeOrder(customerId, productId, quantity, totalPrice);

    }
    @GetMapping("/process/{orderId}")
    public void processOrder(@PathVariable Long orderId){
        orderService.updateOrder(orderId);
    }

    @PostMapping("/finalize/{orderId}")
    public ResponseEntity<String> finalizeOrder(@PathVariable Long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body("Order finalized successfully");
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Optional<Order> order = orderService.getOrderById(orderId);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }
}