package org.kata.payment.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(H2DatabaseConfig.class)
public class DatabaseConfig {

    private final H2DatabaseConfig h2DatabaseConfig;

    public DatabaseConfig(H2DatabaseConfig h2DatabaseConfig) {
        this.h2DatabaseConfig = h2DatabaseConfig;
    }

    @Bean
    public DataSource dataSource() {
        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(h2DatabaseConfig.driverClassName());
        dataSource.setUrl(h2DatabaseConfig.url());
        dataSource.setUsername(h2DatabaseConfig.username());
        dataSource.setPassword(h2DatabaseConfig.password());
        return dataSource;
    }
}
