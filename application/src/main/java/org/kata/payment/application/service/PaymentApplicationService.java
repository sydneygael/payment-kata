package org.kata.payment.application.service;

import org.kata.payment.application.UseCase;
import org.kata.payment.application.port.in.ManagePayment;
import org.kata.payment.application.port.out.PaymentRepository;
import org.kata.payment.domain.exception.PaymentNotFoundException;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;

import java.util.List;

@UseCase
public class PaymentApplicationService implements ManagePayment {

    private final PaymentRepository paymentRepository;

    public PaymentApplicationService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment readingPayment(PaymentId paymentId) throws PaymentNotFoundException {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with ID " + paymentId.id() + " not found"));
    }

    @Override
    public Payment modifyPayment(Payment payment) throws PaymentNotFoundException {
        var existing = readingPayment(payment.getId());

        if (payment.getStatus() != existing.getStatus()) {
            switch (payment.getStatus()) {
                case AUTHORIZED -> existing.authorize();
                case CAPTURED -> existing.capture();
                case CANCELED -> existing.cancel();
                default -> throw new IllegalStateException("Cannot transition to status " + payment.getStatus());
            }
        }

        existing.updateDetails(payment.getPaymentType(), payment.getItems());
        return paymentRepository.save(existing);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
