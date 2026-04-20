package org.example.rspcm.exception;

import java.sql.Timestamp;

public record ErrorMessage(
        Timestamp timestamp,
        String errorCode,
        String message,
        String path
) {
}
