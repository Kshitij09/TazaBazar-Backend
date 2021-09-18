package com.kshitijpatil.tazabazar.apiv2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kshitijpatil.tazabazar.apiv2.dto.CartItemDto;
import com.kshitijpatil.tazabazar.apiv2.dto.LoginResponse;
import com.kshitijpatil.tazabazar.apiv2.userdetail.IUserService;
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

import java.util.ArrayList;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private IUserService userService;

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
    public void getAllUsersWithoutAdminAuthenticationShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v2/users"))
                .andDo(print())
                .andExpect(status().is(UNAUTHORIZED.value()));
        var loginResponse = performLogin("john.doe@test.com", "1234");
        var userAuthenticatedRequest = get("/api/v2/users")
                .header(HttpHeaders.AUTHORIZATION, loginResponse.getAccessToken());
        mockMvc.perform(userAuthenticatedRequest)
                .andDo(print())
                .andExpect(status().is(UNAUTHORIZED.value()));
    }

    @Test
    public void getAllUsersWithAdminAuthenticationShouldReturn200() throws Exception {
        var loginResponse = performLogin("ashok.kumar@test.com", "0000");
        var adminAuthenticatedRequest = get("/api/v2/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getAccessToken());
        mockMvc.perform(adminAuthenticatedRequest)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUserByUsernameWithoutAuthorizationShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v2/users/john.doe@test.com"))
                .andDo(print())
                .andExpect(status().is(UNAUTHORIZED.value()))
                .andReturn();
        // Trying to access someone else's profile
        var loginResponse = performLogin("john.doe@test.com", "1234");
        var authenticatedRequest = get("/api/v2/users/john.smith@test.com")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getAccessToken());
        mockMvc.perform(authenticatedRequest)
                .andDo(print())
                .andExpect(status().is(UNAUTHORIZED.value()));
    }

    @Test
    public void getUserByUsernameWithAuthorizationShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/v2/users/john.doe@test.com"))
                .andDo(print())
                .andExpect(status().is(UNAUTHORIZED.value()))
                .andReturn();
        // Trying to access someone else's profile
        var loginResponse = performLogin("john.doe@test.com", "1234");
        var authenticatedRequest = get("/api/v2/users/john.doe@test.com")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getAccessToken());
        mockMvc.perform(authenticatedRequest)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthorizedUpdateCartByUsername() throws Exception {
        var loginResponse = performLogin("john.doe@test.com", "1234");
        var cartItems = new ArrayList<CartItemDto>();
        cartItems.add(new CartItemDto(1L, 1L));
        cartItems.add(new CartItemDto(2L, 4L));
        cartItems.add(new CartItemDto(3L, 6L));
        var request = put("/api/v2/users/john.doe@test.com/cart")
                .content(mapper.writeValueAsString(cartItems))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getAccessToken());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testUnauthorizedUpdateCartByUsername() throws Exception {
        var loginResponse = performLogin("john.doe@test.com", "1234");
        var cartItems = new ArrayList<CartItemDto>();
        cartItems.add(new CartItemDto(1L, 1L));
        cartItems.add(new CartItemDto(2L, 4L));
        cartItems.add(new CartItemDto(3L, 6L));
        var request = put("/api/v2/users/unknown@test.com/cart")
                .content(mapper.writeValueAsString(cartItems))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getAccessToken());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}