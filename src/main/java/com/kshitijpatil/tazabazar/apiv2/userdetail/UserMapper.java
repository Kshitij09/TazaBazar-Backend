package com.kshitijpatil.tazabazar.apiv2.userdetail;

import com.kshitijpatil.tazabazar.apiv2.dto.UserView;
import com.kshitijpatil.tazabazar.apiv2.userauth.UserAuth;
import com.kshitijpatil.tazabazar.apiv2.userauth.UserAuthView;

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
        return UserAuthView.builder()
                .username(user.username)
                .fullName(user.fullName)
                .phone(user.phone)
                .emailVerified(userAuth.emailVerified)
                .phoneVerified(userAuth.phoneVerified)
                .build();
    }
}
