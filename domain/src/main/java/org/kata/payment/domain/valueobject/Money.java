package org.kata.payment.domain.valueobject;

public record Money(double amount) {

    public Money {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}
