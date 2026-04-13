package org.example.rspcm.mapper;

import org.example.rspcm.dto.practice.PracticeTeamResponse;
import org.example.rspcm.model.entity.AppUser;
import org.example.rspcm.model.entity.PracticeTeam;

import java.util.Set;
import java.util.stream.Collectors;

public final class PracticeTeamMapper {
    private PracticeTeamMapper() {
    }

    public static PracticeTeamResponse toResponse(PracticeTeam team) {
        Set<Long> memberIds = team.getMembers().stream().map(AppUser::getId).collect(Collectors.toSet());
        return new PracticeTeamResponse(team.getId(), team.getPractice().getId(), team.getName(), memberIds);
    }
}
