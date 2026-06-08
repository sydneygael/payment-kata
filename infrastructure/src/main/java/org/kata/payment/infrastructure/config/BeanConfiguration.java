package org.kata.payment.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kata.payment.domain.UseCase;
import org.kata.payment.domain.port.out.PaymentRepository;
import org.kata.payment.infrastructure.adapter.out.persistence.adapter.PaymentInMemoryAdapter;
import org.kata.payment.infrastructure.adapter.out.persistence.adapter.PaymentJpaAdapter;
import org.kata.payment.infrastructure.adapter.out.persistence.mapper.PaymentPersistenceMapper;
import org.kata.payment.infrastructure.adapter.out.persistence.repository.PaymentJpaRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "org.kata.payment",
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = UseCase.class)
)
public class BeanConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @ConditionalOnProperty(name = "app.payment.storage.type", havingValue = "jpa")
    public PaymentRepository paymentJpaAdapter(PaymentJpaRepository jpaRepository, PaymentPersistenceMapper mapper) {
        return new PaymentJpaAdapter(jpaRepository, mapper);
    }

    @Bean
    @ConditionalOnProperty(name = "app.payment.storage.type", havingValue = "in-memory")
    public PaymentRepository paymentInMemoryAdapter() {
        return new PaymentInMemoryAdapter();
    }
}
