package com.kshitijpatil.tazabazar.apiv2.userdetail;

import com.kshitijpatil.tazabazar.api.security.jwt.RefreshTokenNotFoundException;
import com.kshitijpatil.tazabazar.apiv2.dto.CreateUserRequest;
import com.kshitijpatil.tazabazar.apiv2.dto.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service("db_user_service")
@Primary
@RequiredArgsConstructor
public class UserService implements IUserService, UserDetailsService {
    private final UserRepository users;
    private final JdbcAggregateTemplate template;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserView createUser(CreateUserRequest request) throws UsernameExistsException, PhoneExistsException {
        try {
            var user = new User(request.username, request.password, request.phone);
            user.setPassword(passwordEncoder.encode(user.password));
            var saved = template.insert(user);
            return UserMapper.toUserView(saved);
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
    public UserView storeRefreshTokenFor(String username, String refreshToken) throws UsernameNotFoundException {
        var user = users.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        user.setRefreshToken(refreshToken);
        return UserMapper.toUserView(users.save(user));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = users.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User: %s, not found", username)
                ));
        return UserMapper.toUserDetails(user);
    }

    @Override
    public UserView loadUserByRefreshToken(String refreshToken) throws RefreshTokenNotFoundException {
        var user = users.findByRefreshToken(refreshToken)
                .orElseThrow(RefreshTokenNotFoundException::new);
        return UserMapper.toUserView(user);
    }
}
