package org.example.rspcm.dto.exam;

import org.example.rspcm.dto.common.ExamSummary;
import org.example.rspcm.dto.common.QuestionSummary;

public record ExamQuestionResponse(
        Long id,
        ExamSummary exam,
        QuestionSummary question,
        Integer score,
        Integer orderIndex
) {
}
