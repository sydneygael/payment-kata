package org.kata.payment.domain.port.in;

import org.kata.payment.domain.exception.PaymentNotFoundException;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;

import java.util.List;

public interface ManagePayment {

    Payment createPayment(Payment payment);
    Payment readingPayment(PaymentId paymentId) throws PaymentNotFoundException;
    Payment modifyPayment(Payment payment) throws PaymentNotFoundException;
    List<Payment> getAllPayments();
}
