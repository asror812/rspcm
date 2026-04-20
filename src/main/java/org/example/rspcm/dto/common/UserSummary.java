package org.example.rspcm.dto.common;

public record UserSummary(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
