package org.kata.payment.domain.state;

import org.kata.payment.domain.aggregat.Payment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizedState extends AbstractPaymentState {

    @Override
    public void handle(Payment payment, PaymentEvent event) {
        switch (event) {
            case CAPTURE -> {
                changeState(payment, new CapturedState());
                log.debug("Payment {} captured", payment.getId());
            }
            case CANCEL -> {
                changeState(payment, new CanceledState());
                log.debug("Payment {} canceled", payment.getId());
            }
            default -> super.handle(payment, event);
        }
    }

    @Override
    public Payment.PaymentStatus getStatus() {
        return Payment.PaymentStatus.AUTHORIZED;
    }
}
