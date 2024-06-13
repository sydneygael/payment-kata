package org.kata.payment.features;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.*;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import static io.cucumber.junit.platform.engine.Constants.JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameters({
        @ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "org.kata.payment.features"),
        @ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true"),
        @ConfigurationParameter(key = JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME, value = "long"),
        @ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "html:target/cucumber-report/cucumber.html")
})
@SpringBootConfiguration
public class ValidationTestsRunner {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
