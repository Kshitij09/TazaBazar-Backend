package com.kshitijpatil.tazabazar.apiv2.userauth;

import com.kshitijpatil.tazabazar.apiv2.dto.UserAuthView;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserAuthRepository extends CrudRepository<UserAuth, String> {
    Optional<UserAuth> findByRefreshToken(String refreshToken);

    @Query("SELECT username FROM user_auth WHERE refresh_token=:refreshToken")
    Optional<String> findUsernameByRefreshToken(@Param("refreshToken") String refreshToken);

    @Query("SELECT ud.username as username, ud.phone as phone, ud.full_name as full_name, " +
            "ua.email_verified as email_verified, ua.phone_verified as phone_verified " +
            "FROM user_detail ud inner join user_auth ua using(username) " +
            "WHERE username=:username"
    )
    Optional<UserAuthView> getUserAuthViewByUsername(@Param("username") String username);

    @Query("SELECT ud.username as username, ud.phone as phone, ud.full_name as full_name, " +
            "ua.email_verified as email_verified, ua.phone_verified as phone_verified " +
            "FROM user_detail ud inner join user_auth ua using(username) "
    )
    Iterable<UserAuthView> findAllUserAuthViews();

}
