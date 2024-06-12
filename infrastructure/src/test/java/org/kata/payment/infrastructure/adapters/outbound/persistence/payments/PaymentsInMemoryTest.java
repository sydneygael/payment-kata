package org.kata.payment.infrastructure.adapters.outbound.persistence.payments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kata.payment.domain.aggregat.Payment;
import org.kata.payment.domain.valueobject.Item;
import org.kata.payment.domain.valueobject.Money;
import org.kata.payment.domain.valueobject.PaymentId;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentsInMemoryTest {

    private PaymentsInMemory paymentsInMemory;

    @BeforeEach
    void setUp() {
        paymentsInMemory = new PaymentsInMemory();
    }

    @Test
    void saveAndFindPaymentById() {
        var payment = createTestPayment();
        paymentsInMemory.save(payment);

        Optional<Payment> foundPayment = paymentsInMemory.findPaymentById(payment.getId().id());
        assertThat(foundPayment).isPresent();
        assertThat(foundPayment.get().getId()).isEqualTo(payment.getId());
    }

    private Payment createTestPayment() {
        return Payment.builder()
                .id(new PaymentId("1"))
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.singletonList(new Item("Test Item", new Money(10.0), 1)))
                .build();
    }
}
