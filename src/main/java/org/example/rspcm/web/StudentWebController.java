package org.example.rspcm.web;

import lombok.RequiredArgsConstructor;
import org.example.rspcm.dto.practice.PracticeSubmissionSubmitRequest;
import org.example.rspcm.model.entity.User;
import org.example.rspcm.model.enums.ExamType;
import org.example.rspcm.service.ExamPracticeService;
import org.example.rspcm.service.ExamService;
import org.example.rspcm.service.PracticeParticipationService;
import org.example.rspcm.service.PracticeSubmissionService;
import org.example.rspcm.service.StudentDashboardService;
import org.example.rspcm.service.StudentQuestionExamService;
import org.example.rspcm.service.StudyGroupService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/student")
@PreAuthorize("hasRole('STUDENT')")
@RequiredArgsConstructor
public class StudentWebController {

    private final StudentDashboardService studentDashboardService;
    private final StudyGroupService studyGroupService;
    private final ExamService examService;
    private final ExamPracticeService examPracticeService;
    private final PracticeParticipationService practiceParticipationService;
    private final PracticeSubmissionService practiceSubmissionService;
    private final StudentQuestionExamService studentQuestionExamService;

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

    @GetMapping("/exams")
    public String exams(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("exams", examService.findMyExams(user, null, null, null, PageRequest.of(0, 50)));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "exams");
        return "student/exams";
    }

    @GetMapping("/exams/{id}")
    public String examDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("exam", examService.findById(id, user));
            try {
                model.addAttribute("myParticipation", practiceParticipationService.getMyParticipationByExam(id, user));
            } catch (Exception ignored) {}
            try {
                model.addAttribute("practices", examPracticeService.findAllForStudent(id, user));
            } catch (Exception ignored) {}
            try {
                model.addAttribute("myAttempt", studentQuestionExamService.getMyAttempt(id, user));
            } catch (Exception ignored) {}
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "exams");
        return "student/exam-detail";
    }

    @GetMapping("/exams/{id}/take")
    public String takeExam(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("exam", examService.findById(id, user));
            model.addAttribute("questions", studentQuestionExamService.getQuestions(id, user));
            model.addAttribute("attempt", studentQuestionExamService.getMyAttempt(id, user));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "exams");
        return "student/exam-take";
    }

    @PostMapping("/exams/{id}/start")
    public String startExam(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try { studentQuestionExamService.startAttempt(id, user); } catch (Exception ignored) {}
        return "redirect:/student/exams/" + id + "/take";
    }

    @PostMapping("/exams/{id}/submit")
    public String submitExam(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try { studentQuestionExamService.submitAttempt(id, user); } catch (Exception ignored) {}
        return "redirect:/student/exams/" + id;
    }

    @GetMapping("/select-theme")
    public String selectTheme(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("exams", examService.findMyExams(user, null, ExamType.PRACTICE, null, PageRequest.of(0, 50)));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "select-theme");
        return "student/select-theme";
    }

    @PostMapping("/select-theme/{examId}/practices/{examPracticeId}")
    public String selectPractice(@PathVariable Long examId, @PathVariable Long examPracticeId,
                                 @AuthenticationPrincipal User user) {
        try { practiceParticipationService.selectPractice(examId, examPracticeId, user); } catch (Exception ignored) {}
        return "redirect:/student/exams/" + examId;
    }

    @GetMapping("/teams")
    public String teams(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("participations", practiceParticipationService.getMyParticipations(user));
            model.addAttribute("invitations", practiceParticipationService.getMyTeamInvitations(user));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "teams");
        return "student/teams";
    }

    @GetMapping("/calendar")
    public String calendar(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("exams", examService.findMyExams(user, null, null, null, PageRequest.of(0, 100)));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "calendar");
        return "student/calendar";
    }

    @GetMapping("/reports")
    public String reports(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("participations", practiceParticipationService.getMyParticipations(user));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "reports");
        return "student/reports";
    }

    @GetMapping("/upload")
    public String upload(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("participations", practiceParticipationService.getMyParticipations(user));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "upload");
        return "student/upload";
    }

    @PostMapping("/upload/{participationId}")
    public String submitReport(@PathVariable Long participationId,
                               @RequestParam(required = false) String textAnswer,
                               @RequestParam(required = false) String fileUrl,
                               @AuthenticationPrincipal User user) {
        try {
            practiceSubmissionService.submit(participationId, new PracticeSubmissionSubmitRequest(textAnswer, fileUrl), user);
        } catch (Exception ignored) {}
        return "redirect:/student/reports";
    }

    @GetMapping("/status")
    public String status(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("exams", examService.findMyExams(user, null, null, null, PageRequest.of(0, 50)));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "status");
        return "student/status";
    }
}
