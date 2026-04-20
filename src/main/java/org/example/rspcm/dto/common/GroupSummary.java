package org.example.rspcm.dto.common;

import org.example.rspcm.model.enums.GroupLanguage;

public record GroupSummary(
        Long id,
        String name,
        GroupLanguage language
) {
}
