package org.kata.payment.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.kata.payment.infrastructure.PaymentApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/payment.feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "org.kata.payment.steps")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, json:target/cucumber.json")
public class ValidationRunner {


    @SpringBootTest(classes = {PaymentApplication.class, FeatureBeanDefinitions.class})
    @ComponentScan(basePackages = "org.kata.payment")
    @CucumberContextConfiguration
    static class FeatureSpringConfig {
    }

    @TestConfiguration
    static class FeatureBeanDefinitions {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}
