package org.kata.payment.domain.state;

import org.kata.payment.domain.aggregat.Payment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractPaymentState implements PaymentState {

    @Override
    public void handle(Payment payment, PaymentEvent event) {
        throw new IllegalStateException("Event " + event + " not allowed in state " + this.getStatus());
    }

    protected void changeState(Payment payment, PaymentState newState) {
        payment.setState(newState);
        log.info("Payment {} changed to state {}", payment.getId(), newState.getStatus());
    }
}
