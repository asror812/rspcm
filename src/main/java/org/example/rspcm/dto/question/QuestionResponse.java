package org.example.rspcm.dto.question;

import org.example.rspcm.model.enums.QuestionType;

import java.util.List;

public record QuestionResponse(
        Long id,
        String text,
        QuestionType type,
        List<QuestionOptionResponse> options
) {
}
