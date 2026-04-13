package org.example.rspcm.mapper;

import org.example.rspcm.dto.subject.SubjectResponse;
import org.example.rspcm.model.entity.AppUser;
import org.example.rspcm.model.entity.Subject;

import java.util.Set;
import java.util.stream.Collectors;

public final class SubjectMapper {
    private SubjectMapper() {
    }

    public static SubjectResponse toResponse(Subject subject) {
        Set<Long> studentIds = subject.getStudents().stream().map(AppUser::getId).collect(Collectors.toSet());
        Set<Long> teacherIds = subject.getTeachers().stream().map(AppUser::getId).collect(Collectors.toSet());
        return new SubjectResponse(subject.getId(), subject.getName(), subject.getDescription(), studentIds, teacherIds);
    }
}
