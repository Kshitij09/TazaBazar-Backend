package com.kshitijpatil.tazabazar.apiv2.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kshitijpatil.tazabazar.ApiErrorResponse;
import com.kshitijpatil.tazabazar.apiv2.dto.LoginResponse;
import com.kshitijpatil.tazabazar.apiv2.dto.OrderDto;
import com.kshitijpatil.tazabazar.apiv2.dto.OrderLineDto;
import com.kshitijpatil.tazabazar.apiv2.order.OrderStatus;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private final Random random = new Random();

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

    private List<OrderLineDto> getOrderLines() {
        List<OrderLineDto> orderLines = new ArrayList<>();
        orderLines.add(new OrderLineDto(random.nextInt(10) + 1L, random.nextInt(7) + 1L));
        orderLines.add(new OrderLineDto(random.nextInt(10) + 1L, random.nextInt(7) + 1L));
        orderLines.add(new OrderLineDto(random.nextInt(10) + 1L, random.nextInt(7) + 1L));
        return orderLines;
    }

    private ResultActions performPlaceOrder(List<OrderLineDto> orderLines, String accessToken) throws Exception {
        var request = post("/api/v2/orders")
                .content(mapper.writeValueAsString(orderLines))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON);
        return mockMvc.perform(request);
    }

    @Test
    @Transactional
    public void placeOrderWithValidInventoriesShouldSucceed() throws Exception {
        var loginResponse = performLogin("john.doe@test.com", "1234");
        var orderLines = getOrderLines();
        performPlaceOrder(orderLines, loginResponse.getAccessToken())
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void placeOrderWithInvalidInventoryShouldReturn404() throws Exception {
        var loginResponse = performLogin("john.doe@test.com", "1234");
        List<OrderLineDto> orderLines = getOrderLines();
        orderLines.add(new OrderLineDto(500L, 2L));
        var responseString = performPlaceOrder(orderLines, loginResponse.getAccessToken())
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();
        var apiError = mapper.readValue(responseString, ApiErrorResponse.class);
        assertThat(apiError.getError()).isEqualTo("inv-001");
    }

    @Test
    @Transactional
    public void testGetOrderByIdAuthorized() throws Exception {
        var username = "john.doe@test.com";
        var loginResponse = performLogin(username, "1234");
        var orderLines = getOrderLines();
        var responseString = performPlaceOrder(orderLines, loginResponse.getAccessToken())
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        var orderResponse = mapper.readValue(responseString, OrderDto.class);
        var getRequest = get("/api/v2/orders/" + orderResponse.id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON);
        var getOrderResponse = mockMvc.perform(getRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        var orderDto = mapper.readValue(getOrderResponse, OrderDto.class);
        assertThat(orderDto.username).isEqualTo(username);
        assertThat(orderDto.orderLines).containsAll(orderLines);
        assertThat(orderDto.status).isEqualTo(OrderStatus.ACCEPTED);
    }

    @Test
    @Transactional
    public void testGetOrderByIdUnauthorized() throws Exception {
        var username = "john.doe@test.com";
        var loginResponse = performLogin(username, "1234");
        var orderLines = getOrderLines();
        var responseString = performPlaceOrder(orderLines, loginResponse.getAccessToken())
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        var orderResponse = mapper.readValue(responseString, OrderDto.class);
        var differentUser = performLogin("john.smith@test.com", "9876");
        var getRequest = get("/api/v2/orders/" + orderResponse.id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + differentUser.getAccessToken())
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(getRequest)
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    private UUID performPlaceOrderAndGetId(List<OrderLineDto> orderLines, String accessToken) throws Exception {
        var responseString = performPlaceOrder(orderLines, accessToken)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        var orderResponse = mapper.readValue(responseString, OrderDto.class);
        return orderResponse.id;
    }

    @Test
    @Transactional
    public void testGetOrdersByUsername() throws Exception {
        var username = "john.doe@test.com";
        var loginResponse = performLogin(username, "1234");
        var orderLines1 = getOrderLines();
        var order1uuid = performPlaceOrderAndGetId(orderLines1, loginResponse.getAccessToken());
        var orderLines2 = getOrderLines();
        var order2uuid = performPlaceOrderAndGetId(orderLines2, loginResponse.getAccessToken());
        var getRequest = get(String.format("/api/v2/users/%s/orders", username))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON);
        var responseString = mockMvc.perform(getRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        List<OrderDto> retrievedOrders = mapper.readValue(responseString, new TypeReference<>() {
        });
        var retrievedOrderIds = retrievedOrders.stream()
                .map(OrderDto::getId)
                .collect(Collectors.toList());
        assertThat(retrievedOrderIds).containsOnly(order1uuid, order2uuid);
    }

}
