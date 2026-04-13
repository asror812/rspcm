package org.example.rspcm.dto.practice;

import org.example.rspcm.model.enums.WorkMode;

import java.time.LocalDateTime;
import java.util.Set;

public record PracticeResponse(
        Long id,
        String name,
        String description,
        String resourceUrl,
        String requirements,
        LocalDateTime deadline,
        WorkMode workMode,
        Integer teamSize,
        boolean calendarRequired,
        Set<Long> groupIds,
        Set<Long> studentIds,
        Long createdBy,
        Long subjectId
) {
}
