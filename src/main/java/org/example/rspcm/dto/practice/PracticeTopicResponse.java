package org.example.rspcm.dto.practice;

import org.example.rspcm.dto.common.PracticeSummary;

public record PracticeTopicResponse(
        Long id,
        PracticeSummary practice,
        String title,
        String description
) {
}
