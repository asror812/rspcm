package org.example.rspcm.mapper;

import org.example.rspcm.dto.practice.PracticeJournalResponse;
import org.example.rspcm.model.entity.PracticeJournal;

public final class PracticeJournalMapper {
    private PracticeJournalMapper() {
    }

    public static PracticeJournalResponse toResponse(PracticeJournal journal) {
        return new PracticeJournalResponse(
                journal.getId(),
                SummaryMapper.toPracticeSummary(journal.getPractice()),
                SummaryMapper.toUserSummary(journal.getStudent()),
                journal.getTeam() == null ? null : SummaryMapper.toPracticeTeamSummary(journal.getTeam()),
                journal.getContent(),
                journal.getFilePath(),
                journal.getCalendarText(),
                journal.getCalendarFilePath(),
                journal.getSubmittedAt()
        );
    }
}
