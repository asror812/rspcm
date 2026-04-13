package org.example.rspcm.controller;

import org.example.rspcm.dto.practice.PracticeTeamRequest;
import org.example.rspcm.dto.practice.PracticeTeamResponse;
import org.example.rspcm.mapper.PracticeTeamMapper;
import org.example.rspcm.service.PracticeTeamService;
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
@RequestMapping("/api/practice-teams")
public class PracticeTeamController {

    private final PracticeTeamService teamService;

    @GetMapping("/practice/{practiceId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public List<PracticeTeamResponse> byPractice(@PathVariable Long practiceId) {
        return teamService.getByPracticeId(practiceId).stream().map(PracticeTeamMapper::toResponse).toList();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public PracticeTeamResponse create(@Valid @RequestBody PracticeTeamRequest request) {
        return PracticeTeamMapper.toResponse(teamService.create(request));
    }
}
