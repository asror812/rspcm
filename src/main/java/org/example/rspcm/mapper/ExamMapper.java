package org.example.rspcm.mapper;

import org.example.rspcm.dto.exam.ExamResponse;
import org.example.rspcm.model.entity.AppUser;
import org.example.rspcm.model.entity.Exam;
import org.example.rspcm.model.entity.StudyGroup;

import java.util.Set;
import java.util.stream.Collectors;

public final class ExamMapper {
    private ExamMapper() {
    }

    public static ExamResponse toResponse(Exam exam) {
        Set<Long> groupIds = exam.getGroups().stream().map(StudyGroup::getId).collect(Collectors.toSet());
        Set<Long> studentIds = exam.getTargetStudents().stream().map(AppUser::getId).collect(Collectors.toSet());
        return new ExamResponse(
                exam.getId(),
                exam.getTitle(),
                exam.getDescription(),
                exam.getStartAt(),
                exam.getEndAt(),
                exam.getMaxScore(),
                groupIds,
                studentIds,
                exam.getCreatedBy().getId(),
                exam.getSubject() == null ? null : exam.getSubject().getId()
        );
    }
}
