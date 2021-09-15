package com.kshitijpatil.tazabazar.apiv2.controller;

import com.kshitijpatil.tazabazar.api.security.dto.AuthRequest;
import com.kshitijpatil.tazabazar.api.security.model.Role;
import com.kshitijpatil.tazabazar.api.security.service.JwtCreateService;
import com.kshitijpatil.tazabazar.apiv2.dto.CreateUserRequest;
import com.kshitijpatil.tazabazar.apiv2.dto.LoginResponse;
import com.kshitijpatil.tazabazar.apiv2.dto.UserView;
import com.kshitijpatil.tazabazar.apiv2.userdetail.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthControllerV2 {
    private final AuthenticationManager authenticationManager;
    private final JwtCreateService jwtCreateService;
    private final IUserService userService;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            // At this point, the user has been validated
            User user = (User) authenticate.getPrincipal();
            var roles = List.of(Role.USER);
            var refreshToken = jwtCreateService.generateRefreshToken();
            var userView = userService.storeRefreshTokenFor(user.getUsername(), refreshToken);
            var loginResponse = LoginResponse.builder()
                    .user(userView)
                    .accessToken(jwtCreateService.generateToken(user.getUsername(), roles))
                    .refreshToken(refreshToken)
                    .build();
            return ResponseEntity.ok()
                    .body(loginResponse);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("register")
    public UserView register(@RequestBody @Valid CreateUserRequest request) {
        return userService.createUser(request);
    }
}
