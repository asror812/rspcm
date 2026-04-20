package org.example.rspcm.dto.subject;

import org.example.rspcm.dto.common.UserSummary;

import java.util.Set;

public record SubjectResponse(
        Long id,
        String name,
        String description,
        Set<UserSummary> teachers
) {
}
