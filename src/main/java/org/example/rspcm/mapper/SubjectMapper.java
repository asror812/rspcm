package org.example.rspcm.mapper;

import org.example.rspcm.dto.common.UserSummary;
import org.example.rspcm.dto.subject.SubjectResponse;
import org.example.rspcm.model.entity.Subject;

import java.util.Set;
import java.util.stream.Collectors;

public final class SubjectMapper {
    private SubjectMapper() {
    }

    public static SubjectResponse toResponse(Subject subject) {
        Set<UserSummary> teachers = subject.getTeachers().stream()
                .map(SummaryMapper::toUserSummary)
                .collect(Collectors.toSet());
        return new SubjectResponse(subject.getId(), subject.getName(), subject.getDescription(), teachers);
    }
}
