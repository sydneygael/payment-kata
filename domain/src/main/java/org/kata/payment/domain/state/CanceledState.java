package org.kata.payment.domain.state;

import lombok.extern.slf4j.Slf4j;
import org.kata.payment.domain.aggregat.Payment;

@Slf4j
public class CanceledState implements PaymentState {

    @Override
    public void authorized(Payment payment) {
        throw new IllegalStateException("Cannot authorize a canceled payment");
    }

    @Override
    public void captured(Payment payment) {
        throw new IllegalStateException("Cannot capture a canceled payment");
    }

    @Override
    public void canceled(Payment payment) {
        throw new IllegalStateException("Payment is already canceled");
    }

    @Override
    public Payment.PaymentStatus getStatus() {
        return Payment.PaymentStatus.CANCELED;
    }
}
