package org.kata.payment.infrastructure.adapters.outbound.persistence.payments;

import org.junit.jupiter.api.Test;
import org.kata.payment.domain.aggregat.Payment;
import org.kata.payment.domain.valueobject.Item;
import org.kata.payment.domain.valueobject.Money;
import org.kata.payment.domain.valueobject.PaymentId;
import org.kata.payment.infrastructure.adapters.outbound.persistence.entity.PaymentEntity;
import org.kata.payment.infrastructure.adapters.outbound.persistence.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@ComponentScan(
        basePackages = "org.kata.payment.infrastructure.adapters.outbound.persistence",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {PaymentsJpa.class, PaymentRepository.class})
)
class PaymentsJpaTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void saveAndFindPaymentById() {
        // Given
        var payment = createTestPayment();

        // When
        var savedEntity = paymentRepository.save(new PaymentEntity(payment));

        // Then
        Optional<PaymentEntity> foundEntity = paymentRepository.findById(savedEntity.getId());
        assertThat(foundEntity).isPresent();

        var foundPayment = foundEntity.get().toDomain();
        assertThat(foundPayment.getId()).isEqualTo(payment.getId());
        assertThat(foundPayment.getPaymentType()).isEqualTo(payment.getPaymentType());
    }

    private Payment createTestPayment() {
        return Payment.builder()
                .id(new PaymentId("1"))
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.singletonList(new Item("Test Item", new Money(1.0), 1)))
                .build();
    }
}


