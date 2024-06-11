package org.kata.payment.domain.state;

import lombok.extern.slf4j.Slf4j;
import org.kata.payment.domain.aggregat.Payment;

@Slf4j
public class NewState implements PaymentState {

    @Override
    public void authorized(Payment payment) {
        payment.setState(new AuthorizedState());
        log.debug("Payment {} authorized", payment.getId());
    }

    @Override
    public void captured(Payment payment) {
        throw new IllegalStateException("Cannot capture a payment that is not authorized");
    }

    @Override
    public void canceled(Payment payment) {
        payment.setState(new CanceledState());
        log.debug("Payment {} canceled", payment.getId());
    }

    @Override
    public Payment.PaymentStatus getStatus() {
        return Payment.PaymentStatus.NEW;
    }
}
