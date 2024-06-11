package org.kata.payment.domain.state;

import lombok.extern.slf4j.Slf4j;
import org.kata.payment.domain.aggregat.Payment;

@Slf4j
public class AuthorizedState implements PaymentState {

    @Override
    public void authorized(Payment payment) {
        throw new IllegalStateException("Payment is already authorized");
    }

    @Override
    public void captured(Payment payment) {
        payment.setState(new CapturedState());
        log.info("Payment {} captured", payment.getId());
    }

    @Override
    public void canceled(Payment payment) {
        payment.setState(new CanceledState());
        log.info("Payment {} canceled", payment.getId());
    }

    @Override
    public Payment.PaymentStatus getStatus() {
        return Payment.PaymentStatus.AUTHORIZED;
    }
}
