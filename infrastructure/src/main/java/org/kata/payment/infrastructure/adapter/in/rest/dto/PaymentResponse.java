package org.kata.payment.infrastructure.adapter.in.rest.dto;

import org.kata.payment.domain.model.Payment;

import java.math.BigDecimal;
import java.util.List;

public record PaymentResponse(
        String id,
        Payment.PaymentType paymentType,
        Payment.PaymentStatus paymentStatus,
        List<ItemResponse> items
) {
    public record ItemResponse(String name, BigDecimal price, int quantity) {}
}
