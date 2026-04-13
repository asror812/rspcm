package org.example.rspcm.controller;

import org.example.rspcm.dto.practice.PracticeTopicRequest;
import org.example.rspcm.dto.practice.PracticeTopicResponse;
import org.example.rspcm.mapper.PracticeTopicMapper;
import org.example.rspcm.service.PracticeTopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public List<PracticeTopicResponse> list(@PathVariable Long practiceId) {
        return topicService.getByPracticeId(practiceId).stream().map(PracticeTopicMapper::toResponse).toList();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public PracticeTopicResponse create(@PathVariable Long practiceId, @Valid @RequestBody PracticeTopicRequest request) {
        return PracticeTopicMapper.toResponse(topicService.create(practiceId, request));
    }

    @PutMapping("/{topicId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public PracticeTopicResponse update(@PathVariable Long practiceId,
                                        @PathVariable Long topicId,
                                        @Valid @RequestBody PracticeTopicRequest request) {
        return PracticeTopicMapper.toResponse(topicService.update(topicId, request));
    }

    @DeleteMapping("/{topicId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public void delete(@PathVariable Long practiceId, @PathVariable Long topicId) {
        topicService.delete(topicId);
    }
}
