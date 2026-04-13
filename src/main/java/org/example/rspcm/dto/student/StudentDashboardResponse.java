package org.example.rspcm.dto.student;

import org.example.rspcm.dto.subject.SubjectResponse;

import java.util.List;

public record StudentDashboardResponse(
        List<SubjectResponse> subjects,
        List<StudentTaskItem> practices,
        List<StudentTaskItem> exams
) {
}
