package org.kata.payment.infrastructure.config;

import org.kata.payment.application.services.UseCase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "org.kata.payment",
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = UseCase.class)
)
public class BeanConfiguration {
}
