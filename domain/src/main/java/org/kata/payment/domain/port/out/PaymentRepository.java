package org.kata.payment.domain.port.out;

import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(PaymentId paymentId);
    List<Payment> findAll();
}
