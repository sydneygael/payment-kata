package org.kata.payment.infrastructure.config;

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
        private final H2DatabaseConfig config;

        H2DatabaseConfiguration(H2DatabaseConfig config) {
            this.config = config;
        }

        @Bean
        public DataSource dataSource() {
            var ds = new DriverManagerDataSource();
            ds.setDriverClassName(config.driverClassName());
            ds.setUrl(config.url());
            ds.setUsername(config.username());
            ds.setPassword(config.password());
            return ds;
        }
    }

    @Configuration
    @EnableConfigurationProperties(PostgresDatabaseConfig.class)
    @ConditionalOnProperty(name = "app.datasource.type", havingValue = "postgres")
    static class PostgresDatabaseConfiguration {
        private final PostgresDatabaseConfig config;

        PostgresDatabaseConfiguration(PostgresDatabaseConfig config) {
            this.config = config;
        }

        @Bean
        public DataSource dataSource() {
            var ds = new DriverManagerDataSource();
            ds.setDriverClassName(config.driverClassName());
            ds.setUrl(config.url());
            ds.setUsername(config.username());
            ds.setPassword(config.password());
            return ds;
        }
    }
}
