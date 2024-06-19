package org.kata.payment.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount) {

    public Money {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        // Assurez-vous que l'instance de BigDecimal est immuable (représentation canonique)
        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Money(double amount) {
        this(BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_EVEN));
    }
}
