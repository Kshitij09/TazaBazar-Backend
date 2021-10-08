package com.kshitijpatil.tazabazar.apiv2.userdetail;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, String> {
    @Modifying
    @Query("DELETE FROM user_detail WHERE username = :username")
    int deleteByUsername(@Param("username") String username);
}
