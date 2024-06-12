package org.kata.payment.infrastructure.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Configuration
    @EnableConfigurationProperties(H2DatabaseConfig.class)
    @ConditionalOnProperty(name = "app.datasource.type", havingValue = "h2")
    static class H2DatabaseConfiguration {
        private final H2DatabaseConfig h2DatabaseConfig;

        public H2DatabaseConfiguration(H2DatabaseConfig h2DatabaseConfig) {
            this.h2DatabaseConfig = h2DatabaseConfig;
        }

        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(h2DatabaseConfig.driverClassName());
            dataSource.setUrl(h2DatabaseConfig.url());
            dataSource.setUsername(h2DatabaseConfig.username());
            dataSource.setPassword(h2DatabaseConfig.password());
            return dataSource;
        }
    }

    @Configuration
    @EnableConfigurationProperties(PostgresDatabaseConfig.class)
    @ConditionalOnProperty(name = "app.datasource.type", havingValue = "postgres")
    static class PostgresDatabaseConfiguration {
        private final PostgresDatabaseConfig postgresDatabaseConfig;

        public PostgresDatabaseConfiguration(PostgresDatabaseConfig postgresDatabaseConfig) {
            this.postgresDatabaseConfig = postgresDatabaseConfig;
        }

        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(postgresDatabaseConfig.driverClassName());
            dataSource.setUrl(postgresDatabaseConfig.url());
            dataSource.setUsername(postgresDatabaseConfig.username());
            dataSource.setPassword(postgresDatabaseConfig.password());
            return dataSource;
        }
    }
}
