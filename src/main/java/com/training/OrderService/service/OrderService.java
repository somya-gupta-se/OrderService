package com.training.OrderService.service;
//import com.training.OrderService.event.OrderEvent;
//import com.training.OrderService.event.OrderEventProducer;
import com.training.OrderService.exception.OrderNotFoundException;
import com.training.OrderService.model.Order;
import com.training.OrderService.model.OrderStatus;
import com.training.OrderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.kafka.core.KafkaTemplate;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

/*
    private OrderEventProducer orderEventProducer;


    @Autowired
    public OrderService(@Lazy OrderEventProducer orderEventProducer) {
        this.orderEventProducer = orderEventProducer;
    }*/

   /* @Autowired
    public OrderService(OrderRepository orderRepository, OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer;
    }*/

@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void finalizeOrder(Long orderId) {
        System.out.println("Finalizing order with ID: " + orderId);
    }

    public Order placeOrder(Long customerId, String productId, int quantity, BigDecimal totalPrice) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        System.out.println("Order placed successfully for order id "+savedOrder.getId()+" publishing orde placed event");
        String value = "for customer id "+savedOrder.getCustomerId()+" and order id is "+savedOrder.getId();
        kafkaTemplate.send("orderTopic", "ORDER_PLACED", value);
        return savedOrder;
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException("Order with ID "+orderId+" not found"));
    }

    public void updateOrder(Long orderId){
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.CONFIRMED);
            System.out.println("Order payment is successful so order confirmed");
            orderRepository.save(order);
            System.out.println("Triggering order confirmed event");
            String value = "for customer id "+order.getCustomerId()+" and order id is "+order.getId();
            kafkaTemplate.send("orderTopic", "ORDER_CONFIRMED", value);

    }
}