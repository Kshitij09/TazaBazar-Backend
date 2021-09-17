package com.kshitijpatil.tazabazar.apiv2.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kshitijpatil.tazabazar.ApiServerApplication;
import com.kshitijpatil.tazabazar.apiv2.dto.ProductOutDto;
import com.kshitijpatil.tazabazar.apiv2.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiServerApplication.class)
@AutoConfigureMockMvc
public class ProductControllerTest {
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository products;

    @Test
    public void testGetProducts() throws Exception {
        var result = mockMvc.perform(get("/api/v2/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<ProductOutDto> actual = mapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        assertThat(actual).isNotEmpty();
        var vegetablesResult = mockMvc.perform(get("/api/v2/products?category=vegetables"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<ProductOutDto> vegetablesActual = mapper.readValue(
                vegetablesResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        vegetablesActual.forEach(vegetable -> assertThat(vegetable.category).isEqualTo("vegetables"));
    }
}
