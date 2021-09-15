package com.kshitijpatil.tazabazar.apiv2.userdetail;

import com.kshitijpatil.tazabazar.api.security.model.Role;
import com.kshitijpatil.tazabazar.apiv2.dto.UserView;
import org.springframework.security.core.userdetails.UserDetails;

public class UserMapper {
    public static UserView toUserView(User user) {
        return UserView.builder()
                .username(user.username)
                .fullName(user.fullName)
                .phone(user.phone)
                .emailVerified(user.emailVerified)
                .phoneVerified(user.phoneVerified)
                .build();
    }

    public static UserDetails toUserDetails(User user) {
        // TODO: Fixup Roles here
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.username)
                .password(user.password)
                .roles(Role.USER)
                .build();
    }
}
