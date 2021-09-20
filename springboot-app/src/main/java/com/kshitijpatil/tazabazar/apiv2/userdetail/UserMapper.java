package com.kshitijpatil.tazabazar.apiv2.userdetail;

import com.kshitijpatil.tazabazar.apiv2.dto.CartItemDto;
import com.kshitijpatil.tazabazar.apiv2.dto.UserAuthView;
import com.kshitijpatil.tazabazar.apiv2.dto.UserDetailView;
import com.kshitijpatil.tazabazar.apiv2.dto.UserView;
import com.kshitijpatil.tazabazar.apiv2.userauth.UserAuth;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserView toUserView(User user, UserAuth userAuth) {
        return UserView.builder()
                .username(user.username)
                .fullName(user.fullName)
                .phone(user.phone)
                .emailVerified(userAuth.emailVerified)
                .phoneVerified(userAuth.phoneVerified)
                .build();
    }

    public static UserAuthView toUserAuthView(User user, UserAuth userAuth) {
        return new UserAuthView(
                user.username,
                user.fullName,
                user.phone,
                userAuth.emailVerified,
                userAuth.phoneVerified
        );
    }

    public static UserDetailView toUserDetailView(User user) {
        var cartSet = user.cart.stream()
                .map(UserMapper::toCartItemDto)
                .collect(Collectors.toSet());
        return new UserDetailView(user.username, user.fullName, user.phone, cartSet);
    }

    public static CartItemDto toCartItemDto(CartItem cartItem) {
        return new CartItemDto(cartItem.inventoryId.getId(), cartItem.quantity);
    }
}
