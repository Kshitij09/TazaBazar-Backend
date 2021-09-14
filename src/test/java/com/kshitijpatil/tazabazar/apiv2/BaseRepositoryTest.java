package com.kshitijpatil.tazabazar.apiv2;

import com.kshitijpatil.tazabazar.util.TestPostgreConfig;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestPostgreConfig.class)
@EnableJdbcRepositories
@Sql("classpath:schema.sql")
@ActiveProfiles("test")
public abstract class BaseRepositoryTest {
}
