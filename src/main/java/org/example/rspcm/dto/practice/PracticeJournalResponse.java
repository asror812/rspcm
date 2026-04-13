package org.example.rspcm.dto.practice;

import java.time.LocalDateTime;

public record PracticeJournalResponse(
        Long id,
        Long practiceId,
        Long studentId,
        Long teamId,
        String content,
        String filePath,
        String calendarText,
        String calendarFilePath,
        LocalDateTime submittedAt
) {
}
