package org.kata.payment.domain.state;

import org.kata.payment.domain.aggregat.Payment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CanceledState extends AbstractPaymentState {

    @Override
    public void handle(Payment payment, PaymentEvent event) {
        throw new IllegalStateException("Cannot change status of a canceled payment");
    }

    @Override
    public Payment.PaymentStatus getStatus() {
        return Payment.PaymentStatus.CANCELED;
    }
}
