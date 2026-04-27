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
        UserSummary createdBy = SummaryMapper.toUserSummary(practice.getCreatedBy());

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
                createdBy
        );
    }
}
