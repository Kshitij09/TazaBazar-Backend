package com.kshitijpatil.tazabazar.configuration;

/*import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Configuration
public class DataSourceConfig extends AbstractJdbcConfiguration {
    @Value("${db-password-filepath}")
    private String passwordFilepath;

    @Autowired
    private Environment env;

    @Bean
    NamedParameterJdbcOperations operations(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Override
    public JdbcMappingContext jdbcMappingContext(Optional<NamingStrategy> namingStrategy, JdbcCustomConversions customConversions) {
        var context = super.jdbcMappingContext(namingStrategy, customConversions);
        context.setForceQuote(false);
        return context;
    }

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
}*/
