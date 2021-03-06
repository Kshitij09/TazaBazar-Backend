package com.kshitijpatil.tazabazar.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kshitijpatil.tazabazar.api.product.ProductInDto;
import com.kshitijpatil.tazabazar.apiv2.dto.CreateUserRequest;
import com.kshitijpatil.tazabazar.apiv2.dto.ProductCategoryDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JsonDataSource {
    private final Logger logger;
    private final ObjectMapper mapper = new ObjectMapper();
    @Value("classpath:json/fruits.json")
    private String fruitsFilepath;
    @Value("classpath:json/dals_and_pulses.json")
    private String dalsAndPulsesFilepath;
    @Value("classpath:json/leafy_vegetables.json")
    private String leafyVegetablesFilepath;
    @Value("classpath:json/vegetables.json")
    private String vegetablesFilepath;
    @Value("classpath:json/rice_wheat_atta.json")
    String riceWheatAttaFilepath;
    @Value("classpath:json/product_categories.json")
    String productCategoriesFilepath;
    @Value("classpath:json/users.json")
    String usersFilepath;

    private <T> List<T> readJsonArrayFrom(String filepath, Class<T> valueType) {
        try {
            File file = ResourceUtils.getFile(filepath);
            var javaType = mapper.getTypeFactory()
                    .constructCollectionType(List.class, valueType);
            List<T> objects = mapper.readValue(file, javaType);
            logger.debug("Read " + objects.size() + " objects from " + productCategoriesFilepath);
            return objects;
        } catch (IOException e) {
            logger.error("Failed to read from " + filepath, e);
            return Collections.emptyList();
        }
    }

    private List<ProductInDto> readProductsFrom(String filepath) {
        return readJsonArrayFrom(filepath, ProductInDto.class);
    }

    public List<ProductCategoryDto> getProductCategories() {
        return readJsonArrayFrom(productCategoriesFilepath, ProductCategoryDto.class);
    }

    public List<CreateUserRequest> getUserAccounts() {
        return readJsonArrayFrom(usersFilepath, CreateUserRequest.class);
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
