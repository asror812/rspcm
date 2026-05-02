package org.example.rspcm.dto.exam;

import jakarta.validation.constraints.NotNull;

public record ExamQuestionRequest(
        @NotNull Long examId,
        @NotNull Long questionId,
        @NotNull Integer score,
        @NotNull Integer orderIndex
) {
}
