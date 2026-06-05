package org.example.rspcm.web;

import lombok.RequiredArgsConstructor;
import org.example.rspcm.dto.exam.ExamRequest;
import org.example.rspcm.dto.practice.PracticeSubmissionReviewRequest;
import org.example.rspcm.model.entity.User;
import org.example.rspcm.model.enums.ExamStatus;
import org.example.rspcm.model.enums.ExamType;
import org.example.rspcm.repository.StudyGroupRepository;
import org.example.rspcm.repository.SubjectRepository;
import org.example.rspcm.service.AdminDashboardService;
import org.example.rspcm.service.ExamService;
import org.example.rspcm.service.PracticeParticipationService;
import org.example.rspcm.service.PracticeSubmissionService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/teacher")
@PreAuthorize("hasRole('TEACHER')")
@RequiredArgsConstructor
public class TeacherWebController {

    private final AdminDashboardService dashboardService;
    private final StudyGroupService studyGroupService;
    private final ExamService examService;
    private final PracticeSubmissionService submissionService;
    private final PracticeParticipationService practiceParticipationService;
    private final SubjectRepository subjectRepository;
    private final StudyGroupRepository groupRepository;

    // ========================
    // DASHBOARD
    // ========================

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
        try {
            // Get teacher's own exams then collect participations from them
            var exams = examService.findAll(user, null, ExamType.PRACTICE, null, true, null, PageRequest.of(0, 50));
            java.util.List<org.example.rspcm.dto.practice.PracticeParticipationResponse> allParticipations = new java.util.ArrayList<>();
            for (var exam : exams) {
                try {
                    var page = practiceParticipationService.findAll(exam.id(), null, user, PageRequest.of(0, 100));
                    allParticipations.addAll(page.getContent());
                } catch (Exception ignored) {}
            }
            model.addAttribute("participations", allParticipations);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "teams");
        return "teacher/teams";
    }

    // ========================
    // REPORTS
    // ========================

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

    @GetMapping("/reports/{id}")
    public String reportDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("submission", submissionService.getById(id, user));
            model.addAttribute("history", submissionService.getHistory(id, user));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "reports");
        return "teacher/report-detail";
    }

    @PostMapping("/reports/{id}/grade")
    public String gradeSubmission(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            submissionService.grade(id, new PracticeSubmissionReviewRequest(null, null), user);
        } catch (Exception ignored) {
        }
        return "redirect:/teacher/reports/" + id;
    }

    @PostMapping("/reports/{id}/return")
    public String returnSubmission(@PathVariable Long id,
                                    @RequestParam(required = false) String teacherComment,
                                    @AuthenticationPrincipal User user) {
        try {
            submissionService.returnSubmission(id, new PracticeSubmissionReviewRequest(teacherComment, null), user);
        } catch (Exception ignored) {
        }
        return "redirect:/teacher/reports/" + id;
    }

    // ========================
    // RESULTS / PROGRESS
    // ========================

    @GetMapping("/results")
    public String results(Model model, @AuthenticationPrincipal User user) {
        try {
            var exams = examService.findAll(user, null, ExamType.QUESTION, null, true, null, PageRequest.of(0, 20));
            model.addAttribute("exams", exams);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "results");
        return "teacher/results";
    }

    @GetMapping("/progress")
    public String progress(Model model, @AuthenticationPrincipal User user) {
        try {
            var exams = examService.findAll(user, null, ExamType.PRACTICE, null, true, null, PageRequest.of(0, 20));
            model.addAttribute("exams", exams);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "progress");
        return "teacher/progress";
    }

    // ========================
    // EXAMS
    // ========================

    @GetMapping("/exams")
    public String exams(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("exams", examService.findAll(user, null, null, null, true, null, PageRequest.of(0, 50)));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "exams");
        return "teacher/exams";
    }

    @GetMapping("/exams/{id}")
    public String examDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("exam", examService.findById(id, user));
            model.addAttribute("statuses", ExamStatus.values());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "exams");
        return "teacher/exam-detail";
    }

    @GetMapping("/exams/new")
    public String newExamForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("allSubjects", subjectRepository.findAll());
        model.addAttribute("allGroups", groupRepository.findAll());
        model.addAttribute("examTypes", ExamType.values());
        model.addAttribute("editMode", false);
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "exams");
        return "teacher/exam-form";
    }

    @PostMapping("/exams")
    public String createExam(@RequestParam String title,
                              @RequestParam(required = false) String description,
                              @RequestParam String startAt,
                              @RequestParam String endAt,
                              @RequestParam Integer maxScore,
                              @RequestParam Integer taskLimit,
                              @RequestParam ExamType type,
                              @RequestParam Long subjectId,
                              @RequestParam(required = false) Set<Long> groupIds,
                              Model model, @AuthenticationPrincipal User user) {
        try {
            LocalDateTime start = LocalDateTime.parse(startAt);
            LocalDateTime end = LocalDateTime.parse(endAt);
            examService.create(user, new ExamRequest(title, description, start, end, maxScore, taskLimit, type, subjectId, groupIds, null));
            return "redirect:/teacher/exams";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("allSubjects", subjectRepository.findAll());
            model.addAttribute("allGroups", groupRepository.findAll());
            model.addAttribute("examTypes", ExamType.values());
            model.addAttribute("editMode", false);
            model.addAttribute("currentUser", user);
            model.addAttribute("activePage", "exams");
            return "teacher/exam-form";
        }
    }

    @GetMapping("/exams/{id}/edit")
    public String editExamForm(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("exam", examService.findById(id, user));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("allSubjects", subjectRepository.findAll());
        model.addAttribute("allGroups", groupRepository.findAll());
        model.addAttribute("examTypes", ExamType.values());
        model.addAttribute("editMode", true);
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "exams");
        return "teacher/exam-form";
    }

    @PostMapping("/exams/{id}")
    public String updateExam(@PathVariable Long id,
                              @RequestParam String title, @RequestParam(required = false) String description,
                              @RequestParam String startAt, @RequestParam String endAt,
                              @RequestParam Integer maxScore, @RequestParam Integer taskLimit,
                              @RequestParam ExamType type, @RequestParam Long subjectId,
                              @RequestParam(required = false) Set<Long> groupIds,
                              Model model, @AuthenticationPrincipal User user) {
        try {
            LocalDateTime start = LocalDateTime.parse(startAt);
            LocalDateTime end = LocalDateTime.parse(endAt);
            examService.update(id, new ExamRequest(title, description, start, end, maxScore, taskLimit, type, subjectId, groupIds, null), user);
            return "redirect:/teacher/exams";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            try {
                model.addAttribute("exam", examService.findById(id, user));
            } catch (Exception ignored) {
            }
            model.addAttribute("allSubjects", subjectRepository.findAll());
            model.addAttribute("allGroups", groupRepository.findAll());
            model.addAttribute("examTypes", ExamType.values());
            model.addAttribute("editMode", true);
            model.addAttribute("currentUser", user);
            model.addAttribute("activePage", "exams");
            return "teacher/exam-form";
        }
    }

    @PostMapping("/exams/{id}/status")
    public String changeExamStatus(@PathVariable Long id, @RequestParam ExamStatus status, @AuthenticationPrincipal User user) {
        try {
            examService.updateStatus(id, status, user);
        } catch (Exception ignored) {
        }
        return "redirect:/teacher/exams/" + id;
    }

    @PostMapping("/exams/{id}/delete")
    public String deleteExam(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            examService.delete(id, user);
        } catch (Exception ignored) {
        }
        return "redirect:/teacher/exams";
    }
}
