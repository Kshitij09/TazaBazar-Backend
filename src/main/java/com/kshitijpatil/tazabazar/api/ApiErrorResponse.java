package com.kshitijpatil.tazabazar.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Data
public class ApiErrorResponse {
    private OffsetDateTime timestamp;
    private String error;
    private String message;
}
