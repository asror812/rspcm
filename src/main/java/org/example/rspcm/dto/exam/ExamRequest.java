package org.example.rspcm.dto.exam;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Set;

public record ExamRequest(
        @NotBlank String title,
        String description,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Integer maxScore,
        Set<Long> groupIds,
        Set<Long> studentIds,
        Long subjectId
) {
}
