package com.training.OrderService.service;
//import com.training.OrderService.event.OrderEvent;
//import com.training.OrderService.event.OrderEventProducer;
import com.training.OrderService.event.PaymentSuccessEvent;
import com.training.OrderService.model.Order;
import com.training.OrderService.model.OrderStatus;
import com.training.OrderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.management.Notification;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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

    /*@KafkaListener(topics = "payment-events", groupId = "zion-group")
    public void listenPaymentSuccess(PaymentSuccessEvent event) {
        if ("SUCCESS".equals(event.getStatus())) {
            finalizeOrder(event.getOrderId());
        }
    }*/

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
        //OrderEvent orderEvent = new OrderEvent(order.getId(),order.getCustomerId(), "Order PLACED");
        String value = "for customer id "+savedOrder.getCustomerId()+" and order id is "+savedOrder.getId();
        //orderEventProducer.sendOrderEvent(orderEvent);
        kafkaTemplate.send("orderTopic", "ORDER_PLACED", value);



     /*   Notification notification = new Notification(
                savedOrder.getId(),
                savedOrder.getCustomerId(),
                "Your order has been placed successfully!"
        );

        kafkaTemplate.send("notification-topic", notification);*/

        //kafkaTemplate.send("orderTopic", savedOrder.getId(), "ORDER_PLACED");

        return savedOrder;
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public void updateOrder(Long orderId){
        Optional<Order> order = getOrderById(orderId);
        if(order.isPresent()){
            order.get().setStatus(OrderStatus.CONFIRMED);
            System.out.println("Order payment is successful so order confirmed");
            orderRepository.save(order.get());
            System.out.println("Triggering order confirmed event");
            String value = "for customer id "+order.get().getCustomerId()+" and order id is "+order.get().getId();
            kafkaTemplate.send("orderTopic", "ORDER_CONFIRMED", value);
        }
    }
}