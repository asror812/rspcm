package org.example.rspcm.web;

import lombok.RequiredArgsConstructor;
import org.example.rspcm.dto.exam.ExamRequest;
import org.example.rspcm.dto.group.GroupRequest;
import org.example.rspcm.dto.practice.PracticeRequest;
import org.example.rspcm.dto.user.UserCreateRequest;
import org.example.rspcm.dto.user.UserUpdateRequest;
import org.example.rspcm.model.entity.User;
import org.example.rspcm.model.enums.ExamStatus;
import org.example.rspcm.model.enums.ExamType;
import org.example.rspcm.model.enums.GroupLanguage;
import org.example.rspcm.model.enums.RoleName;
import org.example.rspcm.model.enums.SubmissionType;
import org.example.rspcm.model.enums.WorkMode;
import org.example.rspcm.repository.StudyGroupRepository;
import org.example.rspcm.repository.SubjectRepository;
import org.example.rspcm.repository.UserRepository;
import org.example.rspcm.service.AdminDashboardService;
import org.example.rspcm.service.ExamService;
import org.example.rspcm.service.PracticeService;
import org.example.rspcm.service.StudyGroupService;
import org.example.rspcm.service.UserService;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminWebController {

    private final AdminDashboardService dashboardService;
    private final UserService userService;
    private final StudyGroupService groupService;
    private final ExamService examService;
    private final PracticeService practiceService;
    private final SubjectRepository subjectRepository;
    private final StudyGroupRepository groupRepository;
    private final UserRepository userRepository;

    // ========================
    // DASHBOARD
    // ========================

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

    // ========================
    // USERS / STUDENTS
    // ========================

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

    @GetMapping("/users/{id}")
    public String userDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("u", userService.findResponseById(id));
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "students");
        return "admin/user-detail";
    }

    @GetMapping("/users/new")
    public String newUserForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("roles", RoleName.values());
        model.addAttribute("editMode", false);
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "students");
        return "admin/user-form";
    }

    @PostMapping("/users")
    public String createUser(@RequestParam String firstName, @RequestParam String lastName,
                              @RequestParam String email, @RequestParam String password,
                              @RequestParam(required = false) Set<RoleName> roles,
                              @RequestParam(defaultValue = "true") boolean enabled,
                              Model model, @AuthenticationPrincipal User user) {
        try {
            Set<RoleName> r = (roles == null || roles.isEmpty()) ? Set.of(RoleName.ROLE_STUDENT) : roles;
            userService.createResponse(new UserCreateRequest(firstName, lastName, email, password, r, enabled));
            return "redirect:/admin/students";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", RoleName.values());
            model.addAttribute("editMode", false);
            model.addAttribute("currentUser", user);
            model.addAttribute("activePage", "students");
            return "admin/user-form";
        }
    }

    @GetMapping("/users/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("u", userService.findResponseById(id));
        model.addAttribute("roles", RoleName.values());
        model.addAttribute("editMode", true);
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "students");
        return "admin/user-form";
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable Long id,
                              @RequestParam String firstName, @RequestParam String lastName,
                              @RequestParam(required = false) Set<RoleName> roles,
                              @RequestParam(defaultValue = "true") boolean enabled,
                              Model model, @AuthenticationPrincipal User user) {
        try {
            Set<RoleName> r = (roles == null || roles.isEmpty()) ? Set.of(RoleName.ROLE_STUDENT) : roles;
            userService.update(id, new UserUpdateRequest(firstName, lastName, r, enabled));
            return "redirect:/admin/students";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("u", userService.findResponseById(id));
            model.addAttribute("roles", RoleName.values());
            model.addAttribute("editMode", true);
            model.addAttribute("currentUser", user);
            model.addAttribute("activePage", "students");
            return "admin/user-form";
        }
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin/students";
    }

    // ========================
    // GROUPS
    // ========================

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

    @GetMapping("/groups/{id}")
    public String groupDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("group", groupService.findAdminResponseById(id));
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "groups");
        return "admin/group-detail";
    }

    @GetMapping("/groups/new")
    public String newGroupForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("allSubjects", subjectRepository.findAll());
        model.addAttribute("allTeachers", findUsersByRole(RoleName.ROLE_TEACHER));
        model.addAttribute("allStudents", findUsersByRole(RoleName.ROLE_STUDENT));
        model.addAttribute("languages", GroupLanguage.values());
        model.addAttribute("editMode", false);
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "groups");
        return "admin/group-form";
    }

    @PostMapping("/groups")
    public String createGroup(@RequestParam String name,
                               @RequestParam(required = false) String description,
                               @RequestParam GroupLanguage language,
                               @RequestParam(required = false) Set<Long> subjectIds,
                               @RequestParam(required = false) Set<Long> teacherIds,
                               @RequestParam(required = false) Set<Long> studentIds,
                               Model model, @AuthenticationPrincipal User user) {
        try {
            groupService.createResponse(new GroupRequest(name, description, language, subjectIds, teacherIds, studentIds));
            return "redirect:/admin/groups";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("allSubjects", subjectRepository.findAll());
            model.addAttribute("allTeachers", findUsersByRole(RoleName.ROLE_TEACHER));
            model.addAttribute("allStudents", findUsersByRole(RoleName.ROLE_STUDENT));
            model.addAttribute("languages", GroupLanguage.values());
            model.addAttribute("editMode", false);
            model.addAttribute("currentUser", user);
            model.addAttribute("activePage", "groups");
            return "admin/group-form";
        }
    }

    @GetMapping("/groups/{id}/edit")
    public String editGroupForm(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("group", groupService.findAdminResponseById(id));
        model.addAttribute("allSubjects", subjectRepository.findAll());
        model.addAttribute("allTeachers", findUsersByRole(RoleName.ROLE_TEACHER));
        model.addAttribute("allStudents", findUsersByRole(RoleName.ROLE_STUDENT));
        model.addAttribute("languages", GroupLanguage.values());
        model.addAttribute("editMode", true);
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "groups");
        return "admin/group-form";
    }

    @PostMapping("/groups/{id}")
    public String updateGroup(@PathVariable Long id,
                               @RequestParam String name,
                               @RequestParam(required = false) String description,
                               @RequestParam GroupLanguage language,
                               @RequestParam(required = false) Set<Long> subjectIds,
                               @RequestParam(required = false) Set<Long> teacherIds,
                               @RequestParam(required = false) Set<Long> studentIds,
                               Model model, @AuthenticationPrincipal User user) {
        try {
            groupService.update(id, new GroupRequest(name, description, language, subjectIds, teacherIds, studentIds));
            return "redirect:/admin/groups";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("group", groupService.findAdminResponseById(id));
            model.addAttribute("allSubjects", subjectRepository.findAll());
            model.addAttribute("allTeachers", findUsersByRole(RoleName.ROLE_TEACHER));
            model.addAttribute("allStudents", findUsersByRole(RoleName.ROLE_STUDENT));
            model.addAttribute("languages", GroupLanguage.values());
            model.addAttribute("editMode", true);
            model.addAttribute("currentUser", user);
            model.addAttribute("activePage", "groups");
            return "admin/group-form";
        }
    }

    @PostMapping("/groups/{id}/delete")
    public String deleteGroup(@PathVariable Long id) {
        groupService.delete(id);
        return "redirect:/admin/groups";
    }

    // ========================
    // EXAMS
    // ========================

    @GetMapping("/exams")
    public String exams(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("exams", examService.findAll(user, null, null, null, false, null, PageRequest.of(0, 50)));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "exams");
        return "admin/exams";
    }

    @GetMapping("/exams/{id}")
    public String examDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("exam", examService.findById(id, user));
        model.addAttribute("statuses", ExamStatus.values());
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "exams");
        return "admin/exam-detail";
    }

    @GetMapping("/exams/new")
    public String newExamForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("allSubjects", subjectRepository.findAll());
        model.addAttribute("allGroups", groupRepository.findAll());
        model.addAttribute("examTypes", ExamType.values());
        model.addAttribute("editMode", false);
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "exams");
        return "admin/exam-form";
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
            return "redirect:/admin/exams";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("allSubjects", subjectRepository.findAll());
            model.addAttribute("allGroups", groupRepository.findAll());
            model.addAttribute("examTypes", ExamType.values());
            model.addAttribute("editMode", false);
            model.addAttribute("currentUser", user);
            model.addAttribute("activePage", "exams");
            return "admin/exam-form";
        }
    }

    @GetMapping("/exams/{id}/edit")
    public String editExamForm(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("exam", examService.findById(id, user));
        model.addAttribute("allSubjects", subjectRepository.findAll());
        model.addAttribute("allGroups", groupRepository.findAll());
        model.addAttribute("examTypes", ExamType.values());
        model.addAttribute("editMode", true);
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "exams");
        return "admin/exam-form";
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
            return "redirect:/admin/exams";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("exam", examService.findById(id, user));
            model.addAttribute("allSubjects", subjectRepository.findAll());
            model.addAttribute("allGroups", groupRepository.findAll());
            model.addAttribute("examTypes", ExamType.values());
            model.addAttribute("editMode", true);
            model.addAttribute("currentUser", user);
            model.addAttribute("activePage", "exams");
            return "admin/exam-form";
        }
    }

    @PostMapping("/exams/{id}/status")
    public String changeExamStatus(@PathVariable Long id, @RequestParam ExamStatus status, @AuthenticationPrincipal User user) {
        try {
            examService.updateStatus(id, status, user);
        } catch (Exception ignored) {
        }
        return "redirect:/admin/exams/" + id;
    }

    @PostMapping("/exams/{id}/delete")
    public String deleteExam(@PathVariable Long id, @AuthenticationPrincipal User user) {
        examService.delete(id, user);
        return "redirect:/admin/exams";
    }

    // ========================
    // PRACTICES
    // ========================

    @GetMapping("/practices")
    public String practices(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("practices", practiceService.findAll(null, false, null, user, PageRequest.of(0, 50)));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "practices");
        return "admin/practices";
    }

    @GetMapping("/practices/{id}")
    public String practiceDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("practice", practiceService.findResponseById(id, user));
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "practices");
        return "admin/practice-detail";
    }

    @GetMapping("/practices/new")
    public String newPracticeForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("allSubjects", subjectRepository.findAll());
        model.addAttribute("workModes", WorkMode.values());
        model.addAttribute("submissionTypes", SubmissionType.values());
        model.addAttribute("editMode", false);
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "practices");
        return "admin/practice-form";
    }

    @PostMapping("/practices")
    public String createPractice(@RequestParam String name,
                                  @RequestParam(required = false) String description,
                                  @RequestParam(required = false) String resourceUrl,
                                  @RequestParam(required = false) String requirements,
                                  @RequestParam WorkMode workMode,
                                  @RequestParam(defaultValue = "false") boolean schedulingRequired,
                                  @RequestParam(required = false) Set<SubmissionType> allowedSubmissionTypes,
                                  @RequestParam(required = false) Long subjectId,
                                  Model model, @AuthenticationPrincipal User user) {
        try {
            Set<SubmissionType> types = (allowedSubmissionTypes == null) ? Set.of(SubmissionType.TEXT) : allowedSubmissionTypes;
            practiceService.create(new PracticeRequest(name, description, resourceUrl, requirements, workMode, schedulingRequired, types, subjectId), user);
            return "redirect:/admin/practices";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("allSubjects", subjectRepository.findAll());
            model.addAttribute("workModes", WorkMode.values());
            model.addAttribute("submissionTypes", SubmissionType.values());
            model.addAttribute("editMode", false);
            model.addAttribute("currentUser", user);
            model.addAttribute("activePage", "practices");
            return "admin/practice-form";
        }
    }

    @GetMapping("/practices/{id}/edit")
    public String editPracticeForm(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("practice", practiceService.findResponseById(id, user));
        model.addAttribute("allSubjects", subjectRepository.findAll());
        model.addAttribute("workModes", WorkMode.values());
        model.addAttribute("submissionTypes", SubmissionType.values());
        model.addAttribute("editMode", true);
        model.addAttribute("currentUser", user);
        model.addAttribute("activePage", "practices");
        return "admin/practice-form";
    }

    @PostMapping("/practices/{id}")
    public String updatePractice(@PathVariable Long id,
                                  @RequestParam String name, @RequestParam(required = false) String description,
                                  @RequestParam(required = false) String resourceUrl, @RequestParam(required = false) String requirements,
                                  @RequestParam WorkMode workMode, @RequestParam(defaultValue = "false") boolean schedulingRequired,
                                  @RequestParam(required = false) Set<SubmissionType> allowedSubmissionTypes,
                                  @RequestParam(required = false) Long subjectId,
                                  Model model, @AuthenticationPrincipal User user) {
        try {
            Set<SubmissionType> types = (allowedSubmissionTypes == null) ? Set.of(SubmissionType.TEXT) : allowedSubmissionTypes;
            practiceService.update(id, new PracticeRequest(name, description, resourceUrl, requirements, workMode, schedulingRequired, types, subjectId), user);
            return "redirect:/admin/practices";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("practice", practiceService.findResponseById(id, user));
            model.addAttribute("allSubjects", subjectRepository.findAll());
            model.addAttribute("workModes", WorkMode.values());
            model.addAttribute("submissionTypes", SubmissionType.values());
            model.addAttribute("editMode", true);
            model.addAttribute("currentUser", user);
            model.addAttribute("activePage", "practices");
            return "admin/practice-form";
        }
    }

    @PostMapping("/practices/{id}/delete")
    public String deletePractice(@PathVariable Long id, @AuthenticationPrincipal User user) {
        practiceService.delete(id, user);
        return "redirect:/admin/practices";
    }

    // ========================
    // Helpers
    // ========================

    private List<User> findUsersByRole(RoleName roleName) {
        return userRepository.findAll().stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> r.getRoleName() == roleName))
                .collect(Collectors.toList());
    }
}
