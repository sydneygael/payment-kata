package org.kata.payment.infrastructure.adapter.out.persistence.repository;

import org.kata.payment.domain.model.Payment;
import org.kata.payment.infrastructure.adapter.out.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByPaymentType(Payment.PaymentType paymentType);

    Optional<PaymentEntity> findByStatus(Payment.PaymentStatus status);

    List<PaymentEntity> findByItemsName(String itemName);
}
