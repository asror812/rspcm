package org.example.rspcm.web;

import lombok.RequiredArgsConstructor;
import org.example.rspcm.model.entity.User;
import org.example.rspcm.service.StudentDashboardService;
import org.example.rspcm.service.StudyGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
@PreAuthorize("hasRole('STUDENT')")
@RequiredArgsConstructor
public class StudentWebController {

    private final StudentDashboardService studentDashboardService;
    private final StudyGroupService studyGroupService;

    @GetMapping({"", "/dashboard"})
    public String dashboard(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("dashboard", studentDashboardService.getMe(user));
            model.addAttribute("groups", studyGroupService.findOwnStudentGroups(user));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "dashboard");
        return "student/dashboard";
    }

    @GetMapping("/select-theme")
    public String selectTheme(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "select-theme");
        return "student/select-theme";
    }

    @GetMapping("/teams")
    public String teams(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "teams");
        return "student/teams";
    }

    @GetMapping("/calendar")
    public String calendar(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "calendar");
        return "student/calendar";
    }

    @GetMapping("/reports")
    public String reports(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "reports");
        return "student/reports";
    }

    @GetMapping("/upload")
    public String upload(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "upload");
        return "student/upload";
    }

    @GetMapping("/status")
    public String status(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "status");
        return "student/status";
    }
}
