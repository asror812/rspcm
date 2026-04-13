package org.example.rspcm.service;

import org.example.rspcm.dto.practice.PracticeJournalRequest;
import org.example.rspcm.exception.BadRequestException;
import org.example.rspcm.exception.NotFoundException;
import org.example.rspcm.model.entity.AppUser;
import org.example.rspcm.model.entity.Practice;
import org.example.rspcm.model.entity.PracticeJournal;
import org.example.rspcm.model.entity.PracticeTeam;
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

    public List<PracticeJournal> findMine() {
        AppUser student = currentUserService.getCurrentUser();
        return journalRepository.findByStudentId(student.getId());
    }

    public List<PracticeJournal> findByPractice(Long practiceId) {
        return journalRepository.findByPracticeId(practiceId);
    }

    @Transactional
    public PracticeJournal submit(PracticeJournalRequest request) {
        AppUser student = currentUserService.getCurrentUser();
        Practice practice = practiceRepository.findById(request.practiceId())
                .orElseThrow(() -> new NotFoundException("Practice topilmadi: " + request.practiceId()));

        if (practice.isCalendarRequired()
                && (isBlank(request.calendarText()) && isBlank(request.calendarFilePath()))) {
            throw new BadRequestException("Bu practice uchun calendar to'ldirish majburiy");
        }

        PracticeTeam team = null;
        if (request.teamId() != null) {
            team = teamRepository.findById(request.teamId())
                    .orElseThrow(() -> new NotFoundException("Practice team topilmadi: " + request.teamId()));
        }

        PracticeJournal journal = PracticeJournal.builder()
                .practice(practice)
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
