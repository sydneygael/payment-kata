package org.kata.payment.configuration;

import io.cucumber.spring.CucumberContextConfiguration;
import org.kata.payment.infrastructure.PaymentApplication;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = PaymentApplication.class)
public class CucumberSpringConfiguration {
}
