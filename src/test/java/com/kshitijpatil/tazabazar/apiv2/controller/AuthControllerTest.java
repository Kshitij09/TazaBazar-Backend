package com.kshitijpatil.tazabazar.apiv2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kshitijpatil.tazabazar.TestContext;
import com.kshitijpatil.tazabazar.apiv2.dto.CreateUserRequest;
import com.kshitijpatil.tazabazar.apiv2.dto.LoginResponse;
import com.kshitijpatil.tazabazar.apiv2.dto.UserAuthView;
import com.kshitijpatil.tazabazar.apiv2.userdetail.IUserService;
import com.kshitijpatil.tazabazar.security.dto.AuthRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestContext.class)
@AutoConfigureMockMvc
public class AuthControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private IUserService userService;

    @Test
    @Transactional
    public void testCreateAccount() throws Exception {
        var createRequest = new CreateUserRequest("kshitij", "1234", "1234567890");
        createRequest.fullName = "Kshitij Patil";
        var requestBody = mapper.writeValueAsString(createRequest);
        var request = post("/api/v2/auth/register")
                .content(requestBody)
                .contentType("application/json");
        var result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseString = result.getResponse().getContentAsString();
        var userAuthView = mapper.readValue(responseString, UserAuthView.class);
        assertThat(userAuthView.username).isEqualTo(createRequest.username);
        assertThat(userAuthView.phone).isEqualTo(createRequest.phone);
        assertThat(userAuthView.fullName).isEqualTo(createRequest.fullName);
    }

    @Test
    @Transactional
    public void testLogin() throws Exception {
        var createRequest = new CreateUserRequest("kshitij", "1234", "1234567890");
        createRequest.fullName = "Kshitij Patil";
        createRequest.addAuthority("ROLE_USER");
        userService.createUser(createRequest);
        var authRequest = new AuthRequest(createRequest.username, createRequest.password);
        var requestBody = mapper.writeValueAsString(authRequest);
        var request = post("/api/v2/auth/login")
                .content(requestBody)
                .contentType("application/json");
        var result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseString = result.getResponse().getContentAsString();
        var loginResponse = mapper.readValue(responseString, LoginResponse.class);
        assertThat(loginResponse.getUser().username).isEqualTo(createRequest.username);
        assertThat(loginResponse.getUser().phone).isEqualTo(createRequest.phone);
        assertThat(loginResponse.getUser().fullName).isEqualTo(createRequest.fullName);
        assertThat(loginResponse.getUser().emailVerified).isFalse();
        assertThat(loginResponse.getUser().phoneVerified).isFalse();
        assertThat(loginResponse.getAuthorities()).containsOnly("ROLE_USER");
    }

    @Test
    @Transactional
    public void testRefreshToken() throws Exception {
        // Setup
        var createRequest = new CreateUserRequest("kshitij", "1234", "1234567890");
        createRequest.fullName = "Kshitij Patil";
        createRequest.addAuthority("ROLE_USER");
        userService.createUser(createRequest);
        var authRequest = new AuthRequest(createRequest.username, createRequest.password);
        var requestBody = mapper.writeValueAsString(authRequest);
        var request = post("/api/v2/auth/login")
                .content(requestBody)
                .contentType("application/json");
        var result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseString = result.getResponse().getContentAsString();
        var loginResponse = mapper.readValue(responseString, LoginResponse.class);

        // System under test
        var refreshTokenRequest = get("/api/v2/auth/token")
                .header("refresh-token", loginResponse.getRefreshToken());
        mockMvc.perform(refreshTokenRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
