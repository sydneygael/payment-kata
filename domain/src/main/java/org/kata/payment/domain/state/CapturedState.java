package org.kata.payment.domain.state;


import lombok.extern.slf4j.Slf4j;
import org.kata.payment.domain.aggregat.Payment;

@Slf4j
public class CapturedState implements PaymentState {

    @Override
    public void authorized(Payment payment) {
        log.debug("Cannot authorize payment {}", payment.getId());
        throw new IllegalStateException("Cannot authorize a captured payment");
    }

    @Override
    public void captured(Payment payment) {
        log.debug("Cannot capture payment {}", payment.getId());
        throw new IllegalStateException("Payment is already captured");
    }

    @Override
    public void canceled(Payment payment) {
        log.debug("Cannot cancel payment {}", payment.getId());
        throw new IllegalStateException("Cannot cancel a captured payment");
    }

    @Override
    public Payment.PaymentStatus getStatus() {
        return Payment.PaymentStatus.CAPTURED;
    }
}
