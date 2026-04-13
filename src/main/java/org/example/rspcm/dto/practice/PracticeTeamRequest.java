package org.example.rspcm.dto.practice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record PracticeTeamRequest(
        @NotNull Long practiceId,
        @NotBlank String name,
        @NotEmpty Set<Long> memberIds
) {
}
