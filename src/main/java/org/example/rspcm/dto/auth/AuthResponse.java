package org.example.rspcm.dto.auth;

import java.util.Set;

public record AuthResponse(
        Long userId,
        String firstName,
        String lastName,
        String email,
        Set<String> roles,
        String token
) {
}
