package com.kshitijpatil.tazabazar.api.security.mappers;

import com.kshitijpatil.tazabazar.api.security.dto.UserView;
import com.kshitijpatil.tazabazar.api.security.model.User;

@Deprecated
public class UserMapper {
    public static UserView toUserView(User user) {
        var userView = new UserView();
        userView.setUsername(user.getUsername());
        userView.setFullName(user.getFullName());
        userView.setRoles(user.getAuthorityStrings());
        return userView;
    }
}
