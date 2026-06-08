package org.kata.payment.infrastructure.adapter.in.rest.dto;

import org.kata.payment.domain.model.Payment;

import java.math.BigDecimal;
import java.util.List;

public record PaymentRequest(
        Payment.PaymentType paymentType,
        Payment.PaymentStatus paymentStatus,
        List<ItemRequest> items
) {
    public record ItemRequest(String name, BigDecimal price, int quantity) {}
}
