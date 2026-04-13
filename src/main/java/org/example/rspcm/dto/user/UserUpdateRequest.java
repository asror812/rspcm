package org.example.rspcm.dto.user;

import org.example.rspcm.model.enums.RoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UserUpdateRequest(
        @NotBlank String fullName,
        @NotEmpty Set<RoleName> roles,
        boolean enabled
) {
}
