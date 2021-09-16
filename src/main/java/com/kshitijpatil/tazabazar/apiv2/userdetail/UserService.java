package com.kshitijpatil.tazabazar.apiv2.userdetail;

import com.kshitijpatil.tazabazar.api.security.jwt.RefreshTokenNotFoundException;
import com.kshitijpatil.tazabazar.apiv2.dto.CreateUserRequest;
import com.kshitijpatil.tazabazar.apiv2.dto.UserAuthView;
import com.kshitijpatil.tazabazar.apiv2.dto.UserView;
import com.kshitijpatil.tazabazar.apiv2.userauth.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;


@Service("db_user_service")
@Primary
@RequiredArgsConstructor
public class UserService implements IUserService, UserDetailsService {
    private final UserRepository users;
    private final UserAuthRepository userAccounts;
    private final RoleRepository roles;
    private final JdbcAggregateTemplate template;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserAuthView createUser(CreateUserRequest request) throws UsernameExistsException, PhoneExistsException, RoleNotFoundException {
        try {
            var userDetail = new User(request.username, request.fullName, request.phone);
            var userAuth = new UserAuth(AggregateReference.to(userDetail.username), request.password);
            request.authorities.forEach(roleName -> {
                var role = roles.findById(roleName)
                        .orElseThrow(() -> new RoleNotFoundException(roleName));
                userAuth.add(new Authority(AggregateReference.to(role.name)));
            });
            userAuth.setPassword(passwordEncoder.encode(userAuth.password));
            var savedDetails = template.insert(userDetail);
            template.insert(userAuth);
            return UserMapper.toUserAuthView(savedDetails, userAuth);
        } catch (DbActionExecutionException exception) {
            if (exception.getCause() instanceof DuplicateKeyException) {
                var causeMessage = exception.getCause().getMessage();
                if (causeMessage.contains("violates unique constraint \"user_detail_pkey\"")) {
                    throw new UsernameExistsException(request.username);
                } else if (causeMessage.contains("violates unique constraint \"user_detail_phone_key\"")) {
                    throw new PhoneExistsException(request.phone);
                }
            }
        }
        return null;
    }

    @Override
    public void storeRefreshTokenFor(String username, String refreshToken) throws UsernameNotFoundException {
        var user = userAccounts.findById(username)
                .orElseThrow(() -> makeUsernameNotFoundException(username));
        user.setRefreshToken(refreshToken);
        userAccounts.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userAuth = userAccounts.findById(username)
                .orElseThrow(() -> makeUsernameNotFoundException(username));
        var userRoles = userAuth.grantedAuthorities.stream()
                .map(Authority::getAuthority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return org.springframework.security.core.userdetails.User.builder()
                .username(Objects.requireNonNull(userAuth.username.getId()))
                .password(userAuth.password)
                .authorities(userRoles)
                .build();
    }

    @Override
    public UserView loadUserViewByUsername(String username) throws UsernameNotFoundException {
        var userAuth = userAccounts.findById(username)
                .orElseThrow(() -> makeUsernameNotFoundException(username));
        // At this point, we can be sure that user exists (one-to-one mapping)
        var userDetails = users.findById(username).get();
        return UserMapper.toUserView(userDetails, userAuth);
    }

    private UsernameNotFoundException makeUsernameNotFoundException(String username) {
        return new UsernameNotFoundException(
                String.format("User: %s, not found", username)
        );
    }

    @Override
    public UserAuthView loadUserAuthViewByUsername(String username) throws UsernameNotFoundException {
        return userAccounts.getUserAuthViewByUsername(username)
                .orElseThrow(() -> makeUsernameNotFoundException(username));
    }

    @Override
    public UserDetails loadUserByRefreshToken(String refreshToken) throws RefreshTokenNotFoundException {
        // TODO: Report this on spring-data-jdbc
        // for some reason `userAccounts.findByRefreshToken(refreshToken)`
        // is causing "relation authority does not exist" error
        var username = userAccounts.findUsernameByRefreshToken(refreshToken)
                .orElseThrow(RefreshTokenNotFoundException::new);
        var userAuth = userAccounts.findById(username).get();
        var userRoles = userAuth.grantedAuthorities.stream()
                .map(Authority::getAuthority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return org.springframework.security.core.userdetails.User.builder()
                .username(Objects.requireNonNull(userAuth.username.getId()))
                .password(userAuth.password)
                .authorities(userRoles)
                .build();
    }
}