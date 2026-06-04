package org.example.rspcm.web;

import lombok.RequiredArgsConstructor;
import org.example.rspcm.model.entity.User;
import org.example.rspcm.service.AdminDashboardService;
import org.example.rspcm.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminWebController {

    private final AdminDashboardService dashboardService;
    private final UserService userService;

    @GetMapping({"", "/dashboard"})
    public String dashboard(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("stats", dashboardService.getGeneralStats());
            model.addAttribute("recentReports", dashboardService.getRecentReports(PageRequest.of(0, 5)));
            model.addAttribute("groups", dashboardService.getOwnStudyGroups(user));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "dashboard");
        return "admin/dashboard";
    }

    @GetMapping("/students")
    public String students(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("users", userService.findAll(PageRequest.of(0, 50)));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "students");
        return "admin/students";
    }

    @GetMapping("/teachers")
    public String teachers(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("users", userService.findAll(PageRequest.of(0, 50)));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "teachers");
        return "admin/teachers";
    }

    @GetMapping("/groups")
    public String groups(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("groups", dashboardService.getOwnStudyGroups(user));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "groups");
        return "admin/groups";
    }

    @GetMapping("/practices")
    public String practices(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "practices");
        return "admin/practices";
    }

    @GetMapping("/exams")
    public String exams(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "exams");
        return "admin/exams";
    }
}
