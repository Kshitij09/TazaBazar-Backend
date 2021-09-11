package com.kshitijpatil.tazabazar.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class DataSourceConfig {
    @Value("${db-password-filepath}")
    private String passwordFilepath;

    @Autowired
    private Environment env;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        var dbName = env.getProperty("env.DB_NAME", "test");
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/" + dbName);
        try {
            var password = Files.readString(Paths.get(passwordFilepath));
            dataSource.setPassword(password);
        } catch (IOException e) {
            dataSource.setPassword("test");
            e.printStackTrace();
        }
        return dataSource;
    }
}
