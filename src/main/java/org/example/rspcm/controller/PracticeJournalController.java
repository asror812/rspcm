package org.example.rspcm.controller;

import org.example.rspcm.dto.practice.PracticeJournalRequest;
import org.example.rspcm.dto.practice.PracticeJournalResponse;
import org.example.rspcm.mapper.PracticeJournalMapper;
import org.example.rspcm.service.PracticeJournalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/practice-journals")
public class PracticeJournalController {

    private final PracticeJournalService journalService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public List<PracticeJournalResponse> myJournals() {
        return journalService.findMine().stream().map(PracticeJournalMapper::toResponse).toList();
    }

    @GetMapping("/practice/{practiceId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<PracticeJournalResponse> byPractice(@PathVariable Long practiceId) {
        return journalService.findByPractice(practiceId).stream().map(PracticeJournalMapper::toResponse).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public PracticeJournalResponse submit(@Valid @RequestBody PracticeJournalRequest request) {
        return PracticeJournalMapper.toResponse(journalService.submit(request));
    }
}
