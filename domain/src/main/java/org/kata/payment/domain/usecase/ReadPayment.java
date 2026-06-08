package org.kata.payment.domain.usecase;

import org.kata.payment.domain.UseCase;
import org.kata.payment.domain.exception.PaymentNotFoundException;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;
import org.kata.payment.domain.port.out.PaymentRepository;

@UseCase
public class ReadPayment {

    private final PaymentRepository paymentRepository;

    public ReadPayment(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment execute(PaymentId paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(
                        "Payment with ID " + paymentId.id() + " not found"));
    }
}
