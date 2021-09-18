package com.kshitijpatil.tazabazar.apiv2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kshitijpatil.tazabazar.ApiErrorResponse;
import com.kshitijpatil.tazabazar.apiv2.dto.LoginResponse;
import com.kshitijpatil.tazabazar.apiv2.dto.OrderLineDto;
import com.kshitijpatil.tazabazar.apiv2.product.ProductRepository;
import com.kshitijpatil.tazabazar.security.dto.AuthRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository products;

    private LoginResponse performLogin(String username, String password) throws Exception {
        var authRequest = new AuthRequest(username, password);
        var requestBody = mapper.writeValueAsString(authRequest);
        var request = post("/api/v2/auth/login")
                .content(requestBody)
                .contentType("application/json");
        var loginResponseString = mockMvc.perform(request)
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readValue(loginResponseString, LoginResponse.class);
    }

    @Test
    @Transactional
    public void placeOrderWithValidInventoriesShouldSucceed() throws Exception {
        var loginResponse = performLogin("john.doe@test.com", "1234");
        List<OrderLineDto> orderLines = new ArrayList<>();
        orderLines.add(new OrderLineDto(1L, 5L));
        orderLines.add(new OrderLineDto(5L, 1L));
        orderLines.add(new OrderLineDto(8L, 2L));
        var request = post("/api/v2/orders")
                .content(mapper.writeValueAsString(orderLines))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void placeOrderWithInvalidInventoryShouldReturn404() throws Exception {
        var loginResponse = performLogin("john.doe@test.com", "1234");
        List<OrderLineDto> orderLines = new ArrayList<>();
        orderLines.add(new OrderLineDto(1L, 5L));
        orderLines.add(new OrderLineDto(500L, 1L));
        orderLines.add(new OrderLineDto(8L, 2L));
        var request = post("/api/v2/orders")
                .content(mapper.writeValueAsString(orderLines))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON);
        var responseString = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();
        var apiError = mapper.readValue(responseString, ApiErrorResponse.class);
        assertThat(apiError.getError()).isEqualTo("inv-001");
    }
}
