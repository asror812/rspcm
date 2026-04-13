package org.example.rspcm.dto.practice;

import jakarta.validation.constraints.NotBlank;

public record PracticeTopicRequest(
        @NotBlank String title,
        String description
) {
}
