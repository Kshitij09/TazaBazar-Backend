package com.kshitijpatil.tazabazar.apiv2.userdetail;

import com.kshitijpatil.tazabazar.api.security.jwt.RefreshTokenNotFoundException;
import com.kshitijpatil.tazabazar.apiv2.dto.CreateUserRequest;
import com.kshitijpatil.tazabazar.apiv2.dto.UserView;
import com.kshitijpatil.tazabazar.apiv2.userauth.UserAuthView;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IUserService {
    UserAuthView createUser(CreateUserRequest user) throws UsernameExistsException, PhoneExistsException;

    void storeRefreshTokenFor(String username, String refreshToken) throws UsernameNotFoundException;

    UserView loadUserViewByUsername(String username) throws UsernameNotFoundException;

    UserAuthView loadUserAuthViewByUsername(String username) throws UsernameNotFoundException;

    UserDetails loadUserByRefreshToken(String refreshToken) throws RefreshTokenNotFoundException;
}
