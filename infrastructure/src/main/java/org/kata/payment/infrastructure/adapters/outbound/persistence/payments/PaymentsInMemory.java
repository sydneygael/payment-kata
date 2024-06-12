package org.kata.payment.infrastructure.adapters.outbound.persistence.payments;

import org.kata.payment.application.ports.output.Payments;
import org.kata.payment.domain.aggregat.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class PaymentsInMemory implements Payments {

    private final Map<String, Payment> paymentStorage = new ConcurrentHashMap<>();

    @Override
    public Payment save(Payment payment) {
        paymentStorage.put(payment.getId().id(), payment);
        return payment;
    }

    @Override
    public Optional<Payment> findPaymentById(String id) {
        return Optional.ofNullable(paymentStorage.get(id));
    }

    @Override
    public List<Payment> findAll() {
        return new ArrayList<>(paymentStorage.values());
    }
}
