package org.kata.payment.domain.state;

import org.kata.payment.domain.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPaymentState implements PaymentState {

    protected static final Logger log = LoggerFactory.getLogger(AbstractPaymentState.class);

    @Override
    public void handle(Payment payment, PaymentEvent event) {
        throw new IllegalStateException("Event " + event + " not allowed in state " + this.getStatus());
    }

    protected void changeState(Payment payment, PaymentState newState) {
        payment.changeState(newState);
        log.info("Payment {} changed to state {}", payment.getId(), newState.getStatus());
    }
}
