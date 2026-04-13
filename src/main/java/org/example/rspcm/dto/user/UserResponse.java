package org.example.rspcm.dto.user;

import java.util.Set;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        boolean enabled,
        Set<String> roles
) {
}
