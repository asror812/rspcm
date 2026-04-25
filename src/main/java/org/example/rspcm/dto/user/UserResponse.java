package org.example.rspcm.dto.user;

import java.util.Set;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        boolean enabled,
        boolean deleted,
        Set<String> roles
) {
}
