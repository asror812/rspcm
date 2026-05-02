package org.example.rspcm.dto.question;

import jakarta.validation.constraints.NotBlank;

public record QuestionOptionRequest(
        @NotBlank String text,
        boolean correct,
        Integer orderIndex
) {
}
