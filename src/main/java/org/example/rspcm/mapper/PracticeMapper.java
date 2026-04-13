package org.example.rspcm.mapper;

import org.example.rspcm.dto.practice.PracticeResponse;
import org.example.rspcm.model.entity.AppUser;
import org.example.rspcm.model.entity.Practice;
import org.example.rspcm.model.entity.StudyGroup;

import java.util.Set;
import java.util.stream.Collectors;

public final class PracticeMapper {
    private PracticeMapper() {
    }

    public static PracticeResponse toResponse(Practice practice) {
        Set<Long> groupIds = practice.getGroups().stream().map(StudyGroup::getId).collect(Collectors.toSet());
        Set<Long> studentIds = practice.getTargetStudents().stream().map(AppUser::getId).collect(Collectors.toSet());
        return new PracticeResponse(
                practice.getId(),
                practice.getName(),
                practice.getDescription(),
                practice.getResourceUrl(),
                practice.getRequirements(),
                practice.getDeadline(),
                practice.getWorkMode(),
                practice.getTeamSize(),
                practice.isCalendarRequired(),
                groupIds,
                studentIds,
                practice.getCreatedBy().getId(),
                practice.getSubject() == null ? null : practice.getSubject().getId()
        );
    }
}
