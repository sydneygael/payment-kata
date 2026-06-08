package org.kata.payment.infrastructure.adapter.out.persistence.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kata.payment.domain.model.Item;
import org.kata.payment.domain.model.Money;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentInMemoryAdapterTest {

    private PaymentInMemoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PaymentInMemoryAdapter();
    }

    @Test
    void saveAndFindPaymentById() {
        var payment = createTestPayment();
        adapter.save(payment);

        var found = adapter.findById(payment.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(payment.getId());
    }

    private Payment createTestPayment() {
        return Payment.builder()
                .id(new PaymentId("1"))
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.singletonList(new Item("Test Item", new Money(10.0), 1)))
                .build();
    }
}
