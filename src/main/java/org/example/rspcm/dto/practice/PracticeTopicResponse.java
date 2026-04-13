package org.example.rspcm.dto.practice;

public record PracticeTopicResponse(
        Long id,
        Long practiceId,
        String title,
        String description
) {
}
