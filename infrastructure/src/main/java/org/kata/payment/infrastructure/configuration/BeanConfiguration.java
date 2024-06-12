package org.kata.payment.infrastructure.configuration;

import org.kata.payment.application.ports.output.Payments;
import org.kata.payment.application.services.UseCase;
import org.kata.payment.infrastructure.adapters.outbound.persistence.payments.PaymentsInMemory;
import org.kata.payment.infrastructure.adapters.outbound.persistence.payments.PaymentsJpa;
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
    @ConditionalOnProperty(name = "app.payment.storage.type", havingValue = "jpa")
    public Payments paymentsJpa(PaymentsJpa paymentsJpa) {
        return paymentsJpa;
    }

    @Bean
    @ConditionalOnProperty(name = "app.payment.storage.type", havingValue = "in-memory")
    public Payments paymentsInMemory() {
        return new PaymentsInMemory();
    }
}
