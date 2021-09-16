package com.kshitijpatil.tazabazar.apiv2.controller;

import com.kshitijpatil.tazabazar.apiv2.dto.CreateUserRequest;
import com.kshitijpatil.tazabazar.apiv2.dto.LoginResponse;
import com.kshitijpatil.tazabazar.apiv2.dto.UserAuthView;
import com.kshitijpatil.tazabazar.apiv2.userdetail.IUserService;
import com.kshitijpatil.tazabazar.security.JwtCreateService;
import com.kshitijpatil.tazabazar.security.dto.AuthRequest;
import com.kshitijpatil.tazabazar.security.dto.RefreshTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.stream.Collectors;

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
            // At this point, the user has been authenticated
            User user = (User) authenticate.getPrincipal();
            var roles = user.getAuthorities()
                    .stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            var refreshToken = jwtCreateService.generateRefreshToken();
            //var refreshToken = user.getUsername();
            userService.storeRefreshTokenFor(user.getUsername(), refreshToken);
            var userAuthView = userService.loadUserAuthViewByUsername(user.getUsername());
            var loginResponse = LoginResponse.builder()
                    .user(userAuthView)
                    .accessToken(jwtCreateService.generateToken(user.getUsername(), roles))
                    .refreshToken(refreshToken)
                    .authorities(new HashSet<>(roles))
                    .build();
            return ResponseEntity.ok()
                    .body(loginResponse);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("register")
    public UserAuthView register(@RequestBody @Valid CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("token")
    public RefreshTokenResponse refreshToken(@RequestHeader("refresh-token") String token) {
        var user = userService.loadUserByRefreshToken(token);
        var roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        var accessToken = jwtCreateService.generateToken(user.getUsername(), roles);
        return new RefreshTokenResponse(accessToken);
    }
}
