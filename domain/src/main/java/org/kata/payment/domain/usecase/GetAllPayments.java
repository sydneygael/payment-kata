package org.kata.payment.domain.usecase;

import org.kata.payment.domain.UseCase;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.port.out.PaymentRepository;

import java.util.List;

@UseCase
public class GetAllPayments {

    private final PaymentRepository paymentRepository;

    public GetAllPayments(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> execute() {
        return paymentRepository.findAll();
    }
}
