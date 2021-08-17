package com.kshitijpatil.tazabazar.api.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class JsonDataSource {
    @Autowired
    private Logger logger;
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

    private List<ProductInDto> readProductsFrom(String filepath) {
        try {
            File file = ResourceUtils.getFile(filepath);
            ProductInDto[] fruits = mapper.readValue(file, ProductInDto[].class);
            logger.debug("Read " + fruits.length + " objects from " + filepath);
            return Arrays.asList(fruits);
        } catch (IOException e) {
            logger.error("Failed to read from " + filepath, e);
            return Collections.emptyList();
        }
    }

    public List<ProductInDto> getFruits() {
        return readProductsFrom(fruitsFilepath);
    }

    public List<ProductInDto> getDalsAndPulses() {
        return readProductsFrom(dalsAndPulsesFilepath);
    }

    public List<ProductInDto> getLeafyVegetables() {
        return readProductsFrom(leafyVegetablesFilepath);
    }

    public List<ProductInDto> getVegetables() {
        return readProductsFrom(vegetablesFilepath);
    }

    public List<ProductInDto> getRiceWheatAtta() {
        return readProductsFrom(riceWheatAttaFilepath);
    }
}
