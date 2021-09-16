package com.kshitijpatil.tazabazar.apiv2.userauth;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserAuthRepository extends CrudRepository<UserAuth, String> {
    @Query("SELECT * FROM user_auth WHERE refresh_token=:refreshToken")
    Optional<UserAuth> findByRefreshToken(@Param("refreshToken") String refreshToken);

    @Query("SELECT username FROM user_auth WHERE refresh_token=:refreshToken")
    Optional<String> findUsernameByRefreshToken(@Param("refreshToken") String refreshToken);
}
