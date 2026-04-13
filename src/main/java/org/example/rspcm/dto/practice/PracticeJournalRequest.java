package org.example.rspcm.dto.practice;

import jakarta.validation.constraints.NotNull;

public record PracticeJournalRequest(
        @NotNull Long practiceId,
        Long teamId,
        String content,
        String filePath,
        String calendarText,
        String calendarFilePath
) {
}
