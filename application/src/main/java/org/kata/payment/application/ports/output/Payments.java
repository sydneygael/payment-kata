package org.kata.payment.application.ports.output;

import org.kata.payment.domain.aggregat.Payment;

import java.util.Optional;

public interface Payments {
    Payment save(Payment payment);
    Optional<Payment> findPaymentById(String id);
}
