package org.example.rspcm.mapper;

import org.example.rspcm.dto.practice.PracticeTopicResponse;
import org.example.rspcm.model.entity.PracticeTopic;

public final class PracticeTopicMapper {
    private PracticeTopicMapper() {
    }

    public static PracticeTopicResponse toResponse(PracticeTopic topic) {
        return new PracticeTopicResponse(topic.getId(), topic.getPractice().getId(), topic.getTitle(), topic.getDescription());
    }
}
