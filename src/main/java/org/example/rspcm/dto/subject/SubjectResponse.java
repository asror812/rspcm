package org.example.rspcm.dto.subject;

import java.util.Set;

public record SubjectResponse(
        Long id,
        String name,
        String description,
        Set<Long> studentIds,
        Set<Long> teacherIds
) {
}
