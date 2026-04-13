package org.example.rspcm.dto.practice;

import java.util.Set;

public record PracticeTeamResponse(
        Long id,
        Long practiceId,
        String name,
        Set<Long> memberIds
) {
}
