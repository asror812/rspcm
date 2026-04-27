package org.example.rspcm.dto.auth;

import java.util.Set;

public record AuthResponse(
        String email,
        Set<String> roles,
        String token
) {
}
