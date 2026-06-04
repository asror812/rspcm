package org.example.rspcm.web;

import lombok.RequiredArgsConstructor;
import org.example.rspcm.model.entity.User;
import org.example.rspcm.service.AdminDashboardService;
import org.example.rspcm.service.StudyGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teacher")
@PreAuthorize("hasRole('TEACHER')")
@RequiredArgsConstructor
public class TeacherWebController {

    private final AdminDashboardService dashboardService;
    private final StudyGroupService studyGroupService;

    @GetMapping({"", "/dashboard"})
    public String dashboard(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("groups", studyGroupService.findOwnTeacherGroups(user));
            model.addAttribute("recentReports", dashboardService.getRecentReports(
                    org.springframework.data.domain.PageRequest.of(0, 5)));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "dashboard");
        return "teacher/dashboard";
    }

    @GetMapping("/groups")
    public String groups(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("groups", studyGroupService.findOwnTeacherGroups(user));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "groups");
        return "teacher/groups";
    }

    @GetMapping("/students")
    public String students(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("groups", studyGroupService.findOwnTeacherGroups(user));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "students");
        return "teacher/students";
    }

    @GetMapping("/teams")
    public String teams(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "teams");
        return "teacher/teams";
    }

    @GetMapping("/reports")
    public String reports(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("reports", dashboardService.getRecentReports(
                    org.springframework.data.domain.PageRequest.of(0, 50)));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "reports");
        return "teacher/reports";
    }

    @GetMapping("/results")
    public String results(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "results");
        return "teacher/results";
    }

    @GetMapping("/exams")
    public String exams(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "exams");
        return "teacher/exams";
    }

    @GetMapping("/progress")
    public String progress(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "progress");
        return "teacher/progress";
    }
}
