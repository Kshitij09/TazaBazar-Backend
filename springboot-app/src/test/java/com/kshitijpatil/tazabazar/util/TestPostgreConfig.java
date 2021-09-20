package com.kshitijpatil.tazabazar.util;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import javax.sql.DataSource;

@TestConfiguration
@PropertySource("classpath:postgres-datasource.properties")
public class TestPostgreConfig extends AbstractJdbcConfiguration {
    @Autowired
    Environment environment;

    @Bean
    @Profile("test")
    DataSource dataSource() {
        var dataSource = new HikariDataSource();
        dataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
        dataSource.setJdbcUrl(environment.getProperty("jdbc.url"));
        dataSource.setUsername(environment.getProperty("jdbc.username"));
        dataSource.setPassword(environment.getProperty("jdbc.password"));
        return dataSource;
    }
}
