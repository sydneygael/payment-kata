package org.kata.payment.infrastructure.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource")
public record PostgresDatabaseConfig(String url, String driverClassName, String username, String password) {
}
