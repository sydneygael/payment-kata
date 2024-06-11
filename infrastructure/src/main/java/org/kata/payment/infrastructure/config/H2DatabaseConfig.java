package org.kata.payment.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "h2")
public record H2DatabaseConfig(String driverClassName, String url, String username, String password) {
}

