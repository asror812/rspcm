package org.example.rspcm.dto.group;

import org.example.rspcm.model.enums.GroupLanguage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record GroupRequest(
        @NotBlank String name,
        String description,
        @NotNull GroupLanguage language,
        Set<Long> teacherIds,
        Set<Long> studentIds
) {
}
