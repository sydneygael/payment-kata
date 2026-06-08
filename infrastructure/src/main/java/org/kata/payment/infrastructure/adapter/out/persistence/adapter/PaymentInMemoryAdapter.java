package org.kata.payment.infrastructure.adapter.out.persistence.adapter;

import org.kata.payment.domain.port.out.PaymentRepository;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentInMemoryAdapter implements PaymentRepository {

    private final Map<String, Payment> store = new ConcurrentHashMap<>();

    @Override
    public Payment save(Payment payment) {
        store.put(payment.getId().id(), payment);
        return payment;
    }

    @Override
    public Optional<Payment> findById(PaymentId paymentId) {
        return Optional.ofNullable(store.get(paymentId.id()));
    }

    @Override
    public List<Payment> findAll() {
        return new ArrayList<>(store.values());
    }
}
