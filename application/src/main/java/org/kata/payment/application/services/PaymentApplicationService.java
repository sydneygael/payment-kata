package org.kata.payment.application.services;

import org.kata.payment.application.ports.input.ManagePayment;
import org.kata.payment.application.ports.output.Payments;
import org.kata.payment.application.services.exception.PaymentNotFoundException;
import org.kata.payment.domain.aggregat.Payment;
import org.kata.payment.domain.valueobject.PaymentId;

public class PaymentApplicationService implements ManagePayment {

    private final Payments payments;

    public PaymentApplicationService(Payments payments) {
        this.payments = payments;
    }

    @Override
    public Payment createPayment(Payment payment) {
        return payments.save(payment);
    }

    @Override
    public Payment readingPayment(PaymentId paymentId) {
        return payments.findPaymentById(paymentId.id())
                .orElseThrow(() -> new PaymentNotFoundException("Payment with ID " + paymentId.id() + " not found"));
    }

    @Override
    public Payment modifyPayment(Payment payment) {
        var existingPayment = readingPayment(payment.getId());

        // Apply modifications to the existing payment
        // For simplicity, let's assume we directly copy attributes from the provided payment
        existingPayment.setPaymentType(payment.getPaymentType());
        existingPayment.setItems(payment.getItems());
        return payments.save(existingPayment);
    }
}
