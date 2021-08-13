package com.kshitijpatil.tazabazar.api.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class JsonDataSource {
    private final Logger logger = LoggerFactory.getLogger(JsonDataSource.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private @Setter
    String fruitsFilepath = "classpath:json/fruits.json";
    private @Setter
    String dalsAndPulsesFilepath = "classpath:json/dals_and_pulses.json";
    private @Setter
    String leafyVegetablesFilepath = "classpath:json/leafy_vegetables.json";
    private @Setter
    String riceWheatAttaFilepath = "classpath:json/rice_wheat_atta.json";
    private @Setter
    String vegetablesFilepath = "classpath:json/vegetables.json";

    private List<ProductDto> readProductsFrom(String filepath) {
        try {
            File file = ResourceUtils.getFile(filepath);
            ProductDto[] fruits = mapper.readValue(file, ProductDto[].class);
            logger.debug("Read " + fruits.length + " objects from " + filepath);
            return Arrays.asList(fruits);
        } catch (IOException e) {
            logger.error("Failed to read from " + filepath, e);
            return Collections.emptyList();
        }
    }

    public List<ProductDto> getFruits() {
        return readProductsFrom(fruitsFilepath);
    }

    public List<ProductDto> getDalsAndPulses() {
        return readProductsFrom(dalsAndPulsesFilepath);
    }

    public List<ProductDto> getLeafyVegetables() {
        return readProductsFrom(leafyVegetablesFilepath);
    }

    public List<ProductDto> getVegetables() {
        return readProductsFrom(vegetablesFilepath);
    }

    public List<ProductDto> getRiceWheatAtta() {
        return readProductsFrom(riceWheatAttaFilepath);
    }
}
