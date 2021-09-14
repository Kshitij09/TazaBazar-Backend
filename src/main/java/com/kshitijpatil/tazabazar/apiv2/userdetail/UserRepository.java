package com.kshitijpatil.tazabazar.apiv2.userdetail;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    @Query("SELECT * FROM user_detail WHERE (username,password)=(:username,:password)")
    Optional<User> findByUsernameAndPassword(String username, String password);

    @Query("SELECT * FROM user_detail WHERE (username,refresh_token)=(:username,:refreshToken)")
    Optional<User> findByUsernameAndRefreshToken(String username, String refreshToken);
}
