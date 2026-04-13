package org.example.rspcm.controller;

import org.example.rspcm.dto.student.StudentDashboardResponse;
import org.example.rspcm.service.StudentDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student-dashboard")
public class StudentDashboardController {

    private final StudentDashboardService dashboardService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public StudentDashboardResponse me() {
        return dashboardService.getMyDashboard();
    }
}
