package org.kata.payment.domain.state;

import org.kata.payment.domain.model.Payment;

public interface PaymentState {
    void handle(Payment payment, PaymentEvent event);
    Payment.PaymentStatus getStatus();
}
