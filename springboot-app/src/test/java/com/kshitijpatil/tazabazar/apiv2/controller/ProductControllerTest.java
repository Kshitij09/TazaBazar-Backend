package com.kshitijpatil.tazabazar.apiv2.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kshitijpatil.tazabazar.ApiError;
import com.kshitijpatil.tazabazar.ApiErrorResponse;
import com.kshitijpatil.tazabazar.ApiServerApplication;
import com.kshitijpatil.tazabazar.apiv2.dto.InventoryOutDto;
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
import org.springframework.lang.Nullable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

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

    private String getRequestUri(@Nullable String category, @Nullable String nameQuery) {
        var requestUri = "/api/v2/products";
        if (category != null && nameQuery != null)
            requestUri += String.format("?category=%s&q=%s", category, nameQuery);
        else if (nameQuery != null)
            requestUri += String.format("?q=%s", nameQuery);
        else if (category != null)
            requestUri += String.format("?category=%s", category);
        return requestUri;
    }

    @Test
    public void testGetAllProducts() throws Exception {
        var noFilters = get(getRequestUri(null, null));
        var result = mockMvc.perform(noFilters)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<ProductOutDto> actual = mapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        assertThat(actual).isNotEmpty();
    }

    @Test
    public void testGetProductsByCategory() throws Exception {
        var filterByVegetables = get(getRequestUri("vegetables", null));
        var vegetablesResult = mockMvc.perform(filterByVegetables)
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
    public void testGetProductsByName() throws Exception {
        var expected = products.searchProductByName("dal")
                .stream().map(ProductMapper::toProductOutDto)
                .collect(Collectors.toList());
        var filterByName = get(getRequestUri(null, "dal"));
        var queryResult = mockMvc.perform(filterByName)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<ProductOutDto> actual = mapper.readValue(
                queryResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        assertThat(actual).containsAll(expected);
    }

    @Test
    public void testGetProductsByCategoryAndName() throws Exception {
        var searchCategory = "dals-and-pulses";
        var searchQuery = "moong";
        var expected = products.searchProductByCategoryAndName(searchCategory, searchQuery)
                .stream().map(ProductMapper::toProductOutDto)
                .collect(Collectors.toList());
        var filterByCategoryAndName = get(getRequestUri(searchCategory, searchQuery));
        var queryResult = mockMvc.perform(filterByCategoryAndName)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<ProductOutDto> actual = mapper.readValue(
                queryResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        assertThat(actual).containsAll(expected);
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

    @Test
    public void getProductInventoriesWhenSkuExistsShouldReturnObject() throws Exception {
        var productSku = "dlpl-001";
        var expected = assertNotEmptyAndGet(products.findById(productSku));
        var inventoriesUri = String.format("/api/v2/products/%s/inventories", productSku);
        var result = mockMvc.perform(get(inventoriesUri))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseString = result.getResponse().getContentAsString();
        List<InventoryOutDto> actual = mapper.readValue(responseString, new TypeReference<>() {
        });
        assertThat(actual).isNotEmpty();
        var expectedInventories = expected.inventories.stream()
                .map(ProductMapper::toInventoryOutDto)
                .collect(Collectors.toList());
        assertThat(actual).containsAll(expectedInventories);
    }

    @Test
    public void getProductInventoriesWhenSkuDoesNotExistShouldReturnError() throws Exception {
        var inventoriesUri = "/api/v2/products/unknown/inventories";
        var result = mockMvc.perform(get(inventoriesUri))
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
