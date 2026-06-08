package org.kata.payment.infrastructure.adapter.out.persistence.adapter;

import org.kata.payment.domain.port.out.PaymentRepository;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;
import org.kata.payment.infrastructure.adapter.out.persistence.mapper.PaymentPersistenceMapper;
import org.kata.payment.infrastructure.adapter.out.persistence.repository.PaymentJpaRepository;

import java.util.List;
import java.util.Optional;

public class PaymentJpaAdapter implements PaymentRepository {

    private final PaymentJpaRepository jpaRepository;
    private final PaymentPersistenceMapper mapper;

    public PaymentJpaAdapter(PaymentJpaRepository jpaRepository, PaymentPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Payment save(Payment payment) {
        var saved = jpaRepository.save(mapper.toEntity(payment));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Payment> findById(PaymentId paymentId) {
        return jpaRepository.findById(Long.valueOf(paymentId.id()))
                .map(mapper::toDomain);
    }

    @Override
    public List<Payment> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
}
