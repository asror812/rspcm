package org.example.rspcm.dto.practice;

import org.example.rspcm.dto.common.PracticeSummary;
import org.example.rspcm.dto.common.PracticeTeamSummary;
import org.example.rspcm.dto.common.UserSummary;

import java.time.LocalDateTime;

public record PracticeJournalResponse(
        Long id,
        PracticeSummary practice,
        UserSummary student,
        PracticeTeamSummary team,
        String content,
        String filePath,
        String calendarText,
        String calendarFilePath,
        LocalDateTime submittedAt
) {
}
