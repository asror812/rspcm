package org.example.rspcm.dto.answer;

import org.example.rspcm.dto.common.QuestionSummary;
import org.example.rspcm.dto.common.UserSummary;

import java.time.LocalDateTime;

public record AnswerResponse(
        Long id,
        QuestionSummary question,
        UserSummary student,
        String answerText,
        String answerUrl,
        String filePath,
        String selectedOption,
        Integer score,
        String feedback,
        LocalDateTime submittedAt
) {
}
