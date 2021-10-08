package com.kshitijpatil.tazabazar.utils;

import com.kshitijpatil.tazabazar.apiv2.userdetail.UserNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.function.Supplier;

public class ExceptionUtils {
    public static Supplier<UsernameNotFoundException> usernameNotFoundExceptionSupplier(String username) {
        return () -> makeUsernameNotFoundException(username);
    }

    public static UsernameNotFoundException makeUsernameNotFoundException(String username) {
        return new UsernameNotFoundException(String.format("User: %s, not found", username));
    }

    public static Supplier<UserNotFoundException> userNotFoundExceptionSupplier(String username) {
        return () -> new UserNotFoundException(username);
    }
}
