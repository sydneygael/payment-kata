package org.kata.payment.domain.model;

import java.util.UUID;

public record PaymentId(String id) {
    public PaymentId() {
        this(UUID.randomUUID().toString());
    }
}
