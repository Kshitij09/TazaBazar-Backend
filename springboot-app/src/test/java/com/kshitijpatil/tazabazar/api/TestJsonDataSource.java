package com.kshitijpatil.tazabazar.api;

import com.kshitijpatil.tazabazar.api.product.ProductInDto;
import com.kshitijpatil.tazabazar.utils.JsonDataSource;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestJsonDataSource {

    // JsonDataSource uses @Autowired Logger & @Value objects
    @Autowired
    private JsonDataSource jsonDataSource;

    @Test
    public void testReadFruitsJson() {
        List<ProductInDto> parsedProducts = jsonDataSource.getFruits();
        assertThat(parsedProducts).isNotEmpty();
    }

    @Test
    public void testReadVegetablesJson() {
        List<ProductInDto> parsedProducts = jsonDataSource.getVegetables();
        assertThat(parsedProducts).isNotEmpty();
    }

    @Test
    public void testReadLeafyVegetablesJson() {
        List<ProductInDto> parsedProducts = jsonDataSource.getLeafyVegetables();
        assertThat(parsedProducts).isNotEmpty();
    }

    @Test
    public void testReadRiceWheatAttaJson() {
        List<ProductInDto> parsedProducts = jsonDataSource.getRiceWheatAtta();
        assertThat(parsedProducts).isNotEmpty();
    }

    @Test
    public void testReadDalsAndPulsesJson() {
        List<ProductInDto> parsedProducts = jsonDataSource.getDalsAndPulses();
        assertThat(parsedProducts).isNotEmpty();
    }
}

/*
@TestConfiguration
class LoggerImplTestContextConfiguration {
    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger(getClass());
    }

    @Bean
    public JsonDataSource jsonDataSource() {
        return new JsonDataSource(logger());
    }
}*/
