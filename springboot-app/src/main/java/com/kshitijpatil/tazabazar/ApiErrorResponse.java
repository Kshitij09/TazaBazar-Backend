package com.kshitijpatil.tazabazar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ApiErrorResponse {
    private OffsetDateTime timestamp;
    private String error;
    private String message;
}
