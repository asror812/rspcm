package org.example.rspcm.controller;

import org.example.rspcm.dto.profile.StudentProfileResponse;
import org.example.rspcm.dto.profile.StudentProfileUpdateRequest;
import org.example.rspcm.dto.profile.TeacherProfileResponse;
import org.example.rspcm.dto.profile.TeacherProfileUpdateRequest;
import org.example.rspcm.mapper.StudentProfileMapper;
import org.example.rspcm.mapper.TeacherProfileMapper;
import org.example.rspcm.service.CurrentUserService;
import org.example.rspcm.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;
    private final CurrentUserService currentUserService;

    @GetMapping("/students/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public StudentProfileResponse getStudentProfile(@PathVariable Long userId) {
        return StudentProfileMapper.toResponse(profileService.getStudentProfile(userId));
    }

    @PutMapping("/students/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public StudentProfileResponse updateStudentProfile(@PathVariable Long userId,
                                                       @Valid @RequestBody StudentProfileUpdateRequest request) {
        return StudentProfileMapper.toResponse(profileService.updateStudentProfile(userId, request));
    }

    @GetMapping("/students/me")
    @PreAuthorize("hasRole('STUDENT')")
    public StudentProfileResponse myStudentProfile() {
        Long userId = currentUserService.getCurrentUser().getId();
        return StudentProfileMapper.toResponse(profileService.getStudentProfile(userId));
    }

    @PutMapping("/students/me")
    @PreAuthorize("hasRole('STUDENT')")
    public StudentProfileResponse updateMyStudentProfile(@Valid @RequestBody StudentProfileUpdateRequest request) {
        Long userId = currentUserService.getCurrentUser().getId();
        return StudentProfileMapper.toResponse(profileService.updateStudentProfile(userId, request));
    }

    @GetMapping("/teachers/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public TeacherProfileResponse getTeacherProfile(@PathVariable Long userId) {
        return TeacherProfileMapper.toResponse(profileService.getTeacherProfile(userId));
    }

    @PutMapping("/teachers/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public TeacherProfileResponse updateTeacherProfile(@PathVariable Long userId,
                                                       @Valid @RequestBody TeacherProfileUpdateRequest request) {
        return TeacherProfileMapper.toResponse(profileService.updateTeacherProfile(userId, request));
    }

    @GetMapping("/teachers/me")
    @PreAuthorize("hasRole('TEACHER')")
    public TeacherProfileResponse myTeacherProfile() {
        Long userId = currentUserService.getCurrentUser().getId();
        return TeacherProfileMapper.toResponse(profileService.getTeacherProfile(userId));
    }

    @PutMapping("/teachers/me")
    @PreAuthorize("hasRole('TEACHER')")
    public TeacherProfileResponse updateMyTeacherProfile(@Valid @RequestBody TeacherProfileUpdateRequest request) {
        Long userId = currentUserService.getCurrentUser().getId();
        return TeacherProfileMapper.toResponse(profileService.updateTeacherProfile(userId, request));
    }
}
