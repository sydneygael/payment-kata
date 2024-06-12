package org.kata.payment.infrastructure.adapters.outbound.persistence.repository;

import org.kata.payment.infrastructure.adapters.outbound.persistence.entity.PaymentEntity;
import org.kata.payment.domain.aggregat.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByPaymentType(Payment.PaymentType paymentType);

    Optional<PaymentEntity> findByStatus(Payment.PaymentStatus status);

    List<PaymentEntity> findByItemsName(String itemName);
}
