package org.example.rspcm.mapper;

import org.example.rspcm.dto.common.GroupSummary;
import org.example.rspcm.dto.common.SubjectSummary;
import org.example.rspcm.dto.common.UserSummary;
import org.example.rspcm.dto.practice.PracticeResponse;
import org.example.rspcm.model.entity.Practice;

import java.util.Set;
import java.util.stream.Collectors;

public final class PracticeMapper {
    private PracticeMapper() {
    }

    public static PracticeResponse toResponse(Practice practice) {
        Set<GroupSummary> groups = practice.getGroups().stream().map(SummaryMapper::toGroupSummary).collect(Collectors.toSet());
        Set<UserSummary> students = practice.getTargetStudents().stream().map(SummaryMapper::toUserSummary).collect(Collectors.toSet());
        UserSummary createdBy = SummaryMapper.toUserSummary(practice.getCreatedBy());
        SubjectSummary subject = practice.getSubject() == null ? null : SummaryMapper.toSubjectSummary(practice.getSubject());
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
                groups,
                students,
                createdBy,
                subject
        );
    }
}
