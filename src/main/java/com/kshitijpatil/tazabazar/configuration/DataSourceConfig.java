package com.kshitijpatil.tazabazar.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        var dbName = env.getProperty("env.DB_NAME", "test");
        dataSourceBuilder.url("jdbc:postgresql://localhost:5432/" + dbName);
        try {
            var password = Files.readString(Paths.get(passwordFilepath));
            dataSourceBuilder.password(password);
        } catch (IOException e) {
            dataSourceBuilder.password("test");
            e.printStackTrace();
        }
        return dataSourceBuilder.build();
    }
}
