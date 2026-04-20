package org.example.rspcm.mapper;

import org.example.rspcm.dto.common.SubjectSummary;
import org.example.rspcm.dto.common.UserSummary;
import org.example.rspcm.dto.group.GroupResponse;
import org.example.rspcm.model.entity.StudyGroup;

import java.util.Set;
import java.util.stream.Collectors;

public final class GroupMapper {
    private GroupMapper() {
    }

    public static GroupResponse toResponse(StudyGroup group) {
        Set<SubjectSummary> subjects = group.getSubjects().stream().map(SummaryMapper::toSubjectSummary).collect(Collectors.toSet());
        Set<UserSummary> teachers = group.getTeachers().stream().map(SummaryMapper::toUserSummary).collect(Collectors.toSet());
        Set<UserSummary> students = group.getStudents().stream().map(SummaryMapper::toUserSummary).collect(Collectors.toSet());
        return new GroupResponse(group.getId(), group.getName(), group.getDescription(), group.getLanguage(), subjects, teachers, students);
    }
}
