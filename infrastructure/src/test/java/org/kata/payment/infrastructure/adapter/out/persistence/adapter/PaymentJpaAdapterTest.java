package org.kata.payment.infrastructure.adapter.out.persistence.adapter;

import org.junit.jupiter.api.Test;
import org.kata.payment.domain.model.Item;
import org.kata.payment.domain.model.Money;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;
import org.kata.payment.infrastructure.adapter.out.persistence.mapper.PaymentPersistenceMapper;
import org.kata.payment.infrastructure.adapter.out.persistence.repository.PaymentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "app.payment.storage.type=jpa",
        "spring.batch.job.enabled=false"
})
@Transactional
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
