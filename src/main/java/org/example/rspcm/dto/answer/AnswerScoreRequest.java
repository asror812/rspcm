package org.example.rspcm.dto.answer;

import jakarta.validation.constraints.NotNull;

public record AnswerScoreRequest(
        @NotNull Integer score,
        Boolean correct
) {
}
