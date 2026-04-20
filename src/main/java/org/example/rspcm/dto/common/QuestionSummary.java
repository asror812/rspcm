package org.example.rspcm.dto.common;

import org.example.rspcm.model.enums.QuestionType;

public record QuestionSummary(
        Long id,
        String text,
        QuestionType type,
        Integer maxScore
) {
}
