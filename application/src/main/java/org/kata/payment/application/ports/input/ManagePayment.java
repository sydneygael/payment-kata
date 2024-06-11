package org.kata.payment.application.ports.input;

import org.kata.payment.domain.aggregat.Payment;
import org.kata.payment.domain.valueobject.PaymentId;

public interface ManagePayment {

    Payment createPayment(Payment payment);
    Payment readingPayment(PaymentId paymentId);
    Payment modifyPayment (Payment payment);
}
