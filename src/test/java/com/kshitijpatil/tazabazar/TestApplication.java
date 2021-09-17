package com.kshitijpatil.tazabazar;

import com.kshitijpatil.tazabazar.apiv2.initializer.ProductInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootApplication
public class TestApplication {
    @MockBean
    private ProductInitializer productInitializer;
}
