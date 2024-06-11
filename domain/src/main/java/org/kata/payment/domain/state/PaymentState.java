package org.kata.payment.domain.state;

import org.kata.payment.domain.aggregat.Payment;

public interface PaymentState {
    void authorized(Payment payment);
    void captured(Payment payment);
    void canceled(Payment payment);
    Payment.PaymentStatus getStatus();
}

