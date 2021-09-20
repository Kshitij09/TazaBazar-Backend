package com.kshitijpatil.tazabazar.apiv2;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtils {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> T assertNotEmptyAndGet(Optional<T> item) {
        assertThat(item).isNotEmpty();
        return item.get();
    }
}
