package org.kata.payment.infrastructure.adapter.out.persistence.adapter;

import org.junit.jupiter.api.Test;
import org.kata.payment.domain.model.Item;
import org.kata.payment.domain.model.Money;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;
import org.kata.payment.infrastructure.adapter.out.persistence.mapper.PaymentPersistenceMapper;
import org.kata.payment.infrastructure.adapter.out.persistence.repository.PaymentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Import(PaymentPersistenceMapper.class)
class PaymentJpaAdapterTest {

    @Autowired
    private PaymentJpaRepository jpaRepository;

    @Autowired
    private PaymentPersistenceMapper mapper;

    @Test
    void saveAndFindPaymentById() {
        var adapter = new PaymentJpaAdapter(jpaRepository, mapper);
        var payment = createTestPayment();

        var saved = adapter.save(payment);

        var found = adapter.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getPaymentType()).isEqualTo(payment.getPaymentType());
    }

    private Payment createTestPayment() {
        return Payment.builder()
                .id(new PaymentId())
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .items(Collections.singletonList(new Item("Test Item", new Money(1.0), 1)))
                .build();
    }
}
