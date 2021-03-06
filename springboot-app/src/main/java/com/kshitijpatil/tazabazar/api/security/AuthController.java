package com.kshitijpatil.tazabazar.api.security;

import com.kshitijpatil.tazabazar.api.security.dto.CreateUserRequest;
import com.kshitijpatil.tazabazar.api.security.dto.UserLoginResponse;
import com.kshitijpatil.tazabazar.api.security.dto.UserView;
import com.kshitijpatil.tazabazar.api.security.model.User;
import com.kshitijpatil.tazabazar.api.security.service.IUserService;
import com.kshitijpatil.tazabazar.security.JwtCreateService;
import com.kshitijpatil.tazabazar.security.dto.AuthRequest;
import com.kshitijpatil.tazabazar.security.dto.RefreshTokenResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Authentication")
@RestController
@Hidden
@RequestMapping("/api/auth")
@Deprecated
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtCreateService jwtCreateService;

    @Autowired
    private IUserService userService;

    @PostMapping("login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            // At this point, the user has been validated
            User user = (User) authenticate.getPrincipal();
            var roles = user.getAuthorityStrings();
            var refreshToken = jwtCreateService.generateRefreshToken();
            userService.storeRefreshToken(user.getUsername(), refreshToken);
            var userLoginResponse = UserLoginResponse.builder()
                    .username(user.getUsername())
                    .accessToken(jwtCreateService.generateToken(user.getUsername(), roles))
                    .refreshToken(refreshToken)
                    .authorities(roles)
                    .build();
            return ResponseEntity.ok()
                    .body(userLoginResponse);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("register")
    public UserView register(@RequestBody @Valid CreateUserRequest request) {
        return userService.create(request);
    }

    @PostMapping("token")
    public RefreshTokenResponse refreshToken(@RequestHeader("refresh-token") String token) {
        var user = userService.findUserWithRefreshToken(token);
        var accessToken = jwtCreateService.generateToken(user.getUsername(), user.getRoles());
        return new RefreshTokenResponse(accessToken);
    }

}
