package com.kshitijpatil.tazabazar.apiv2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kshitijpatil.tazabazar.apiv2.dto.LoginResponse;
import com.kshitijpatil.tazabazar.apiv2.initializer.ProductInitializer;
import com.kshitijpatil.tazabazar.apiv2.userdetail.IUserService;
import com.kshitijpatil.tazabazar.security.dto.AuthRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @MockBean
    private ProductInitializer productInitializer;

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
    public void testGetUserByUsername() throws Exception {
        var results = mockMvc.perform(get("/api/v2/users/john.doe@test.com"))
                .andDo(print())
                .andExpect(status().is(UNAUTHORIZED.value()))
                .andReturn();
    }
}