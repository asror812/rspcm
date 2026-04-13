package org.example.rspcm.dto.group;

import org.example.rspcm.model.enums.GroupLanguage;

import java.util.Set;

public record GroupResponse(
        Long id,
        String name,
        String description,
        GroupLanguage language,
        Set<Long> teacherIds,
        Set<Long> studentIds
) {
}
