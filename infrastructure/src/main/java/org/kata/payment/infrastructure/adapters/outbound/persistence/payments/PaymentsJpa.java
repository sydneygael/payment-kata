package org.kata.payment.infrastructure.adapters.outbound.persistence.payments;

import org.kata.payment.application.ports.output.Payments;
import org.kata.payment.domain.aggregat.Payment;
import org.kata.payment.infrastructure.adapters.outbound.persistence.entity.PaymentEntity;
import org.kata.payment.infrastructure.adapters.outbound.persistence.repository.PaymentRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentsJpa implements Payments {

    private final PaymentRepository paymentRepository;

    public PaymentsJpa(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment save(Payment payment) {
        PaymentEntity paymentEntity = new PaymentEntity(payment);
        PaymentEntity savedEntity = paymentRepository.save(paymentEntity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Payment> findPaymentById(String id) {
        return paymentRepository.findById(Long.valueOf(id)).map(PaymentEntity::toDomain);
    }
}
