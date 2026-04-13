package org.example.rspcm.dto.question;

import org.example.rspcm.model.enums.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionRequest(
        @NotBlank String text,
        @NotNull QuestionType type,
        String optionsJson,
        String correctAnswer,
        Integer maxScore,
        Long examId,
        Long practiceId
) {
}
