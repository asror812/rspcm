package org.example.rspcm.dto.exam;

import java.time.LocalDateTime;
import java.util.Set;

public record ExamResponse(
        Long id,
        String title,
        String description,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Integer maxScore,
        Set<Long> groupIds,
        Set<Long> studentIds,
        Long createdBy,
        Long subjectId
) {
}
