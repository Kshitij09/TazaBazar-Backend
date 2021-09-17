package com.kshitijpatil.tazabazar.apiv2.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kshitijpatil.tazabazar.ApiError;
import com.kshitijpatil.tazabazar.ApiErrorResponse;
import com.kshitijpatil.tazabazar.ApiServerApplication;
import com.kshitijpatil.tazabazar.apiv2.dto.ProductCategoryDto;
import com.kshitijpatil.tazabazar.apiv2.dto.ProductOutDto;
import com.kshitijpatil.tazabazar.apiv2.product.ProductMapper;
import com.kshitijpatil.tazabazar.apiv2.product.ProductNotFoundException;
import com.kshitijpatil.tazabazar.apiv2.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.kshitijpatil.tazabazar.apiv2.TestUtils.assertNotEmptyAndGet;
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

    @Test
    public void testGetProductCategories() throws Exception {
        var result = mockMvc.perform(get("/api/v2/products/categories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<ProductCategoryDto> actual = mapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        assertThat(actual).isNotEmpty();
    }

    @Test
    public void getProductBySkuWhenSkuExistsShouldReturnObject() throws Exception {
        var expected = assertNotEmptyAndGet(products.findById("fru-001"));
        var result = mockMvc.perform(get("/api/v2/products/fru-001"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseString = result.getResponse().getContentAsString();
        ProductOutDto actual = mapper.readValue(responseString, ProductOutDto.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(ProductMapper.toProductOutDto(expected));
    }

    @Test
    public void getProductBySkuWhenSkuDoesNotExistShouldReturnError() throws Exception {
        var result = mockMvc.perform(get("/api/v2/products/unknown"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andReturn();
        var responseString = result.getResponse().getContentAsString();
        ApiErrorResponse actual = mapper.readValue(responseString, ApiErrorResponse.class);
        ApiError expected = new ProductNotFoundException("unknown");
        assertThat(actual.getError()).isEqualTo(expected.getError());
        assertThat(actual.getMessage()).isEqualTo(expected.getMessage());
    }
}
