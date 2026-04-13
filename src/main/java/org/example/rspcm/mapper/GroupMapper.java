package org.example.rspcm.mapper;

import org.example.rspcm.dto.group.GroupResponse;
import org.example.rspcm.model.entity.AppUser;
import org.example.rspcm.model.entity.StudyGroup;

import java.util.Set;
import java.util.stream.Collectors;

public final class GroupMapper {
    private GroupMapper() {
    }

    public static GroupResponse toResponse(StudyGroup group) {
        Set<Long> teacherIds = group.getTeachers().stream().map(AppUser::getId).collect(Collectors.toSet());
        Set<Long> studentIds = group.getStudents().stream().map(AppUser::getId).collect(Collectors.toSet());
        return new GroupResponse(group.getId(), group.getName(), group.getDescription(), group.getLanguage(), teacherIds, studentIds);
    }
}
