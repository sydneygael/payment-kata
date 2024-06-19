package org.kata.payment.domain.state;

import org.kata.payment.domain.aggregat.Payment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CapturedState extends AbstractPaymentState {

    @Override
    public void handle(Payment payment, PaymentEvent event) {
        throw new IllegalStateException("Cannot change status of a captured payment");
    }

    @Override
    public Payment.PaymentStatus getStatus() {
        return Payment.PaymentStatus.CAPTURED;
    }
}
