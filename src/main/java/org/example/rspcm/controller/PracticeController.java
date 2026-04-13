package org.example.rspcm.controller;

import org.example.rspcm.dto.practice.PracticeAssignGroupsRequest;
import org.example.rspcm.dto.practice.PracticeRequest;
import org.example.rspcm.dto.practice.PracticeResponse;
import org.example.rspcm.mapper.PracticeMapper;
import org.example.rspcm.service.PracticeService;
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
@RequestMapping("/api/practices")
public class PracticeController {

    private final PracticeService practiceService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public List<PracticeResponse> getAll() {
        return practiceService.findAll().stream().map(PracticeMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public PracticeResponse getById(@PathVariable Long id) {
        return PracticeMapper.toResponse(practiceService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public PracticeResponse create(@Valid @RequestBody PracticeRequest request) {
        return PracticeMapper.toResponse(practiceService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public PracticeResponse update(@PathVariable Long id, @Valid @RequestBody PracticeRequest request) {
        return PracticeMapper.toResponse(practiceService.update(id, request));
    }

    @PatchMapping("/{id}/assign-groups")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public PracticeResponse assignGroups(@PathVariable Long id, @Valid @RequestBody PracticeAssignGroupsRequest request) {
        return PracticeMapper.toResponse(practiceService.assignGroups(id, request.groupIds()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public void delete(@PathVariable Long id) {
        practiceService.delete(id);
    }
}
