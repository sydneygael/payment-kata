package org.kata.payment.domain.usecase;

import org.kata.payment.domain.UseCase;
import org.kata.payment.domain.exception.PaymentNotFoundException;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.port.out.PaymentRepository;

@UseCase
public class ModifyPayment {

    private final PaymentRepository paymentRepository;

    public ModifyPayment(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment execute(Payment payment) {
        var existing = paymentRepository.findById(payment.getId())
                .orElseThrow(() -> new PaymentNotFoundException(
                        "Payment with ID " + payment.getId().id() + " not found"));

        if (payment.getStatus() != existing.getStatus()) {
            switch (payment.getStatus()) {
                case AUTHORIZED -> existing.authorize();
                case CAPTURED   -> existing.capture();
                case CANCELED   -> existing.cancel();
                default -> throw new IllegalStateException(
                        "Cannot transition to status " + payment.getStatus());
            }
        }

        existing.updateDetails(payment.getPaymentType(), payment.getItems());
        return paymentRepository.save(existing);
    }
}
