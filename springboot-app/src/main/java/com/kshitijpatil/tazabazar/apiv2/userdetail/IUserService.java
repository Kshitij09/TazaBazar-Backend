package com.kshitijpatil.tazabazar.apiv2.userdetail;

import com.kshitijpatil.tazabazar.apiv2.dto.*;
import com.kshitijpatil.tazabazar.apiv2.product.InventoryNotFoundException;
import com.kshitijpatil.tazabazar.apiv2.userauth.Role;
import com.kshitijpatil.tazabazar.security.jwt.RefreshTokenNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IUserService {
    UserAuthView createUser(CreateUserRequest user) throws UsernameExistsException, PhoneExistsException;

    void storeRefreshTokenFor(String username, String refreshToken) throws UserNotFoundException;

    UserView loadUserViewByUsername(String username) throws UserNotFoundException;

    UserAuthView loadUserAuthViewByUsername(String username) throws UserNotFoundException;

    List<UserAuthView> loadAllUsers();

    UserDetails loadUserByRefreshToken(String refreshToken) throws RefreshTokenNotFoundException;

    Role addRole(Role role);

    void clearAll();

    UserDetailView updateCart(String username, List<CartItemDto> cartItems) throws InventoryNotFoundException, UserNotFoundException;

    List<CartItemDto> getCartOf(String username) throws UserNotFoundException;

    void deleteUserByUsername(String username) throws UserNotFoundException;
}
