package org.example.rspcm.dto.answer;

import jakarta.validation.constraints.NotNull;

public record AnswerRequest(
        @NotNull Long questionId,
        String answerText,
        String answerUrl,
        String filePath,
        String selectedOption
) {
}
