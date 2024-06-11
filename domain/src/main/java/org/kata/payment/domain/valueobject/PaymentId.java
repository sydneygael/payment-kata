package org.kata.payment.domain.valueobject;

import java.util.UUID;

public record PaymentId(String id) {
    public PaymentId() {
        this(UUID.randomUUID().toString());
    }
}
