package com.kshitijpatil.tazabazar.api;

import com.kshitijpatil.tazabazar.api.product.JsonDataSource;
import com.kshitijpatil.tazabazar.api.product.ProductDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestJsonDataSource {
    private final JsonDataSource jsonDataSource = new JsonDataSource();

    @Test
    public void testReadFruitsJson() {
        List<ProductDto> parsedProducts = jsonDataSource.getFruits();
        assertThat(parsedProducts).isNotEmpty();
    }

    @Test
    public void testReadVegetablesJson() {
        List<ProductDto> parsedProducts = jsonDataSource.getVegetables();
        assertThat(parsedProducts).isNotEmpty();
    }

    @Test
    public void testReadLeafyVegetablesJson() {
        List<ProductDto> parsedProducts = jsonDataSource.getLeafyVegetables();
        assertThat(parsedProducts).isNotEmpty();
    }

    @Test
    public void testReadRiceWheatAttaJson() {
        List<ProductDto> parsedProducts = jsonDataSource.getRiceWheatAtta();
        assertThat(parsedProducts).isNotEmpty();
    }

    @Test
    public void testReadDalsAndPulsesJson() {
        List<ProductDto> parsedProducts = jsonDataSource.getDalsAndPulses();
        assertThat(parsedProducts).isNotEmpty();
    }
}
