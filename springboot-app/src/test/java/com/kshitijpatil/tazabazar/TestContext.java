package com.kshitijpatil.tazabazar;

import com.kshitijpatil.tazabazar.apiv2.initializer.ProductInitializer;
import com.kshitijpatil.tazabazar.apiv2.initializer.UserInitializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Use this context if you want to exclude initializers
 */
@TestConfiguration
public class TestContext {
    @MockBean
    private ProductInitializer productInitializer;
    @MockBean
    private UserInitializer userInitializer;
}
