package org.example.rspcm.dto.common;

import java.time.LocalDateTime;

public record ExamSummary(
        Long id,
        String title,
        LocalDateTime endAt
) {
}
