package org.example.rspcm.service;

import org.example.rspcm.dto.practice.PracticeJournalRequest;
import org.example.rspcm.exception.ErrorCodes;
import org.example.rspcm.exception.ErrorMessageException;
import org.example.rspcm.exception.NotFoundException;
import org.example.rspcm.model.entity.*;
import org.example.rspcm.repository.PracticeJournalRepository;
import org.example.rspcm.repository.PracticeRepository;
import org.example.rspcm.repository.PracticeTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PracticeJournalService {

    private final PracticeJournalRepository journalRepository;
    private final PracticeRepository practiceRepository;
    private final PracticeTeamRepository teamRepository;
    private final CurrentUserService currentUserService;

    public List<PracticeLogbook> findMine() {
        User student = currentUserService.getCurrentUser();
        return journalRepository.findByStudentId(student.getId());
    }

    public List<PracticeLogbook> findByPracticalTask(Long practicalTaskId) {
        return journalRepository.findByPracticalTaskId(practicalTaskId);
    }

    @Transactional
    public PracticeLogbook submit(PracticeJournalRequest request) {
        User student = currentUserService.getCurrentUser();
        PracticalTask practicalTask = practiceRepository.findById(request.practiceId())
                .orElseThrow(() -> new NotFoundException("PracticalTask topilmadi: " + request.practiceId()));

        if (practicalTask.isSchedulingRequired()
                && (isBlank(request.calendarText()) && isBlank(request.calendarFilePath()))) {
            throw new ErrorMessageException("Bu practicalTask uchun calendar to'ldirish majburiy", ErrorCodes.BadRequest);
        }

        PracticeTeam team = null;
        if (request.teamId() != null) {
            team = teamRepository.findById(request.teamId())
                    .orElseThrow(() -> new NotFoundException("PracticalTask team topilmadi: " + request.teamId()));
        }

        PracticeLogbook journal = PracticeLogbook.builder()
                .practicalTask(practicalTask)
                .student(student)
                .team(team)
                .content(request.content())
                .filePath(request.filePath())
                .calendarText(request.calendarText())
                .calendarFilePath(request.calendarFilePath())
                .submittedAt(LocalDateTime.now())
                .build();
        return journalRepository.save(journal);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
