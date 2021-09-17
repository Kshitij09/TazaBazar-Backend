package com.kshitijpatil.tazabazar.apiv2.controller;

import com.kshitijpatil.tazabazar.apiv2.dto.UserAuthView;
import com.kshitijpatil.tazabazar.apiv2.userdetail.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v2/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping
    public List<UserAuthView> getAllUsers() {
        return userService.loadAllUsers();
    }

    @GetMapping("{username}")
    public ResponseEntity<UserAuthView> getUserByUsername(@PathVariable("username") String username) {
        var loggedInUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!Objects.equals(loggedInUsername, username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        var userView = userService.loadUserAuthViewByUsername(username);
        return ResponseEntity.ok(userView);
    }
}
