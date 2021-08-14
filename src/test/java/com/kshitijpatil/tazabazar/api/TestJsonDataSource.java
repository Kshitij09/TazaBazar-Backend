package com.kshitijpatil.tazabazar.api;

import com.kshitijpatil.tazabazar.api.product.JsonDataSource;
import com.kshitijpatil.tazabazar.api.product.ProductInDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestJsonDataSource {
    private final JsonDataSource jsonDataSource = new JsonDataSource();

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
