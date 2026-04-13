package org.example.rspcm.dto.answer;

import java.time.LocalDateTime;

public record AnswerResponse(
        Long id,
        Long questionId,
        Long studentId,
        String answerText,
        String answerUrl,
        String filePath,
        String selectedOption,
        Integer score,
        String feedback,
        LocalDateTime submittedAt
) {
}
