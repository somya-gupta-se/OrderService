package com.training.OrderService.event;

public class PaymentSuccessEvent {
    private Long orderId;
    private String status;

    public PaymentSuccessEvent() {}

    public PaymentSuccessEvent(Long orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public Long getOrderId() { return orderId; }
    public String getStatus() { return status; }
}