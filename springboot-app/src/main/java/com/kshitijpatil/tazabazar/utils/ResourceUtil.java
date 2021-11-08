package com.kshitijpatil.tazabazar.utils;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@AllArgsConstructor
public class ResourceUtil {

    private final ResourceLoader resourceLoader;

    public String asString(String resourcePath) throws IOException {
        Resource resource = resourceLoader.getResource(resourcePath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }

    public InputStream getInputStream(String resourcePath) throws IOException {
        Resource resource = resourceLoader.getResource(resourcePath);
        return resource.getInputStream();
    }

    public byte[] readAllBytes(String resourcePath) throws IOException {
        return getInputStream(resourcePath).readAllBytes();
    }
}