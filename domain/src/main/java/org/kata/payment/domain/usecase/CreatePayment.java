package org.kata.payment.domain.usecase;

import org.kata.payment.domain.UseCase;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.port.out.PaymentRepository;

@UseCase
public class CreatePayment {

    private final PaymentRepository paymentRepository;

    public CreatePayment(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment execute(Payment payment) {
        return paymentRepository.save(payment);
    }
}
