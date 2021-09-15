package com.kshitijpatil.tazabazar.apiv2.userauth;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserAuthRepository extends CrudRepository<UserAuth, String> {
    @Query("SELECT * FROM user_detail WHERE refresh_token=:refreshToken")
    Optional<UserAuth> findByRefreshToken(String refreshToken);
}
