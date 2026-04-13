package org.example.rspcm.dto.subject;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record SubjectRequest(
        @NotBlank String name,
        String description,
        Set<Long> studentIds,
        Set<Long> teacherIds
) {
}
