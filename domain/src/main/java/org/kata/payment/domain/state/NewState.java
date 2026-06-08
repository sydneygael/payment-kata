package org.kata.payment.domain.state;

import org.kata.payment.domain.model.Payment;

public class NewState extends AbstractPaymentState {

    @Override
    public void handle(Payment payment, PaymentEvent event) {
        switch (event) {
            case AUTHORIZE -> {
                changeState(payment, new AuthorizedState());
                log.debug("Payment {} authorized", payment.getId());
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
        return Payment.PaymentStatus.NEW;
    }
}
