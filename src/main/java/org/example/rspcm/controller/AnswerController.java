package org.example.rspcm.controller;

import org.example.rspcm.dto.answer.AnswerRequest;
import org.example.rspcm.dto.answer.AnswerResponse;
import org.example.rspcm.dto.answer.AnswerScoreRequest;
import org.example.rspcm.mapper.AnswerMapper;
import org.example.rspcm.service.AnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answers")
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<AnswerResponse> getAll() {
        return answerService.findAll().stream().map(AnswerMapper::toResponse).toList();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public List<AnswerResponse> myAnswers() {
        return answerService.findMine().stream().map(AnswerMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public AnswerResponse getById(@PathVariable Long id) {
        return AnswerMapper.toResponse(answerService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public AnswerResponse create(@Valid @RequestBody AnswerRequest request) {
        return AnswerMapper.toResponse(answerService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public AnswerResponse updateMine(@PathVariable Long id, @Valid @RequestBody AnswerRequest request) {
        return AnswerMapper.toResponse(answerService.updateMine(id, request));
    }

    @PatchMapping("/{id}/score")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public AnswerResponse score(@PathVariable Long id, @Valid @RequestBody AnswerScoreRequest request) {
        return AnswerMapper.toResponse(answerService.score(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public void deleteMine(@PathVariable Long id) {
        answerService.deleteMine(id);
    }
}
