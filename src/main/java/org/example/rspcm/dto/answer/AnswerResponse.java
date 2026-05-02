package org.example.rspcm.dto.answer;

import org.example.rspcm.dto.common.QuestionSummary;

import java.time.LocalDateTime;
import java.util.List;

public record AnswerResponse(
        Long id,
        QuestionSummary question,
        String textAnswer,
        List<Long> selectedOptionIds,
        Integer score,
        Boolean correct,
        LocalDateTime answeredAt
) {
}
