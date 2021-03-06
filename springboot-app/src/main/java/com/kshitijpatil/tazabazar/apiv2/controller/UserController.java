package com.kshitijpatil.tazabazar.apiv2.controller;

import com.kshitijpatil.tazabazar.apiv2.dto.CartItemDto;
import com.kshitijpatil.tazabazar.apiv2.dto.OrderDto;
import com.kshitijpatil.tazabazar.apiv2.dto.UserAuthView;
import com.kshitijpatil.tazabazar.apiv2.dto.UserDetailView;
import com.kshitijpatil.tazabazar.apiv2.order.IOrderService;
import com.kshitijpatil.tazabazar.apiv2.userdetail.IUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Tag(name = "User Specific")
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v2/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final IUserService userService;
    private final IOrderService orderService;

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

    @DeleteMapping("{username}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUserByUsername(@PathVariable("username") String username) {
        userService.deleteUserByUsername(username);
    }

    @PutMapping("{username}/cart")
    public ResponseEntity<UserDetailView> updateUserCart(@PathVariable("username") String username, @RequestBody List<CartItemDto> cartItems, Principal principal) {
        if (!principal.getName().equals(username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(userService.updateCart(username, cartItems));
    }

    @GetMapping("{username}/cart")
    public ResponseEntity<List<CartItemDto>> getUserCart(@PathVariable("username") String username, Principal principal) {
        if (!principal.getName().equals(username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(userService.getCartOf(username));
    }

    @GetMapping("{username}/orders")
    public ResponseEntity<List<OrderDto>> getOrdersByUsername(@PathVariable("username") String username, Principal principal) {
        if (!principal.getName().equals(username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(orderService.getOrdersByUsername(username));
    }
}
