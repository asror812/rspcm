package org.example.rspcm.service;

import org.example.rspcm.dto.student.StudentDashboardResponse;
import org.example.rspcm.dto.student.StudentTaskItem;
import org.example.rspcm.mapper.SubjectMapper;
import org.example.rspcm.model.entity.User;
import org.example.rspcm.model.entity.Exam;
import org.example.rspcm.model.entity.Practice;
import org.example.rspcm.repository.ExamRepository;
import org.example.rspcm.repository.PracticeRepository;
import org.example.rspcm.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentDashboardService {

    private final CurrentUserService currentUserService;
    private final SubjectRepository subjectRepository;
    private final PracticeRepository practiceRepository;
    private final ExamRepository examRepository;

    public StudentDashboardResponse getMyDashboard() {
        User student = currentUserService.getCurrentUser();

        return new StudentDashboardResponse(
                subjectRepository.findDistinctByGroupsStudentsId(student.getId()).stream().map(SubjectMapper::toResponse).toList(),
                practiceRepository.findDistinctByGroupsStudentsIdOrTargetStudentsId(student.getId(), student.getId()).stream()
                        .map(this::toPracticeTask)
                        .toList(),
                examRepository.findDistinctByGroupsStudentsIdOrTargetStudentsId(student.getId(), student.getId()).stream()
                        .map(this::toExamTask)
                        .toList()
        );
    }

    private StudentTaskItem toPracticeTask(Practice practice) {
        return new StudentTaskItem(practice.getId(), practice.getName(), practice.getDeadline(), "PRACTICE");
    }

    private StudentTaskItem toExamTask(Exam exam) {
        return new StudentTaskItem(exam.getId(), exam.getTitle(), exam.getEndAt(), "EXAM");
    }
}
