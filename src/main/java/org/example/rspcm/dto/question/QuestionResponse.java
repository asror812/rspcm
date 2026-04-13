package org.example.rspcm.dto.question;

import org.example.rspcm.model.enums.QuestionType;

public record QuestionResponse(
        Long id,
        String text,
        QuestionType type,
        String optionsJson,
        String correctAnswer,
        Integer maxScore,
        Long examId,
        Long practiceId
) {
}
