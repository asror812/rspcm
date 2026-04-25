package org.example.rspcm.controller;

import org.example.rspcm.dto.practice.PracticeTopicRequest;
import org.example.rspcm.dto.practice.PracticeTopicResponse;
import org.example.rspcm.mapper.PracticeTopicMapper;
import org.example.rspcm.service.PracticeTopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/practices/{practiceId}/topics")
public class PracticeTopicController {

    private final PracticeTopicService topicService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public ResponseEntity<List<PracticeTopicResponse>> list(@PathVariable Long practiceId) {
        return ResponseEntity.ok(topicService.getByPracticeId(practiceId).stream().map(PracticeTopicMapper::toResponse).toList());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<PracticeTopicResponse> create(@PathVariable Long practiceId, @Valid @RequestBody PracticeTopicRequest request) {
        return ResponseEntity.ok(PracticeTopicMapper.toResponse(topicService.create(practiceId, request)));
    }

    @PutMapping("/{topicId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<PracticeTopicResponse> update(@PathVariable Long practiceId,
                                       @PathVariable Long topicId,
                                       @Valid @RequestBody PracticeTopicRequest request) {
        return ResponseEntity.ok(PracticeTopicMapper.toResponse(topicService.update(topicId, request)));
    }

    @DeleteMapping("/{topicId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<Void> delete(@PathVariable Long practiceId, @PathVariable Long topicId) {
        topicService.delete(topicId);
        return ResponseEntity.noContent().build();
    }
}
