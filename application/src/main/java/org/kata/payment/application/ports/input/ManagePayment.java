package org.kata.payment.application.ports.input;

import org.kata.payment.application.services.exception.PaymentNotFoundException;
import org.kata.payment.domain.aggregat.Payment;
import org.kata.payment.domain.valueobject.PaymentId;

import java.util.List;

public interface ManagePayment {

    Payment createPayment(Payment payment);
    Payment readingPayment(PaymentId paymentId) throws PaymentNotFoundException;
    Payment modifyPayment (Payment payment) throws PaymentNotFoundException;
    List<Payment> getAllPayments();
}
