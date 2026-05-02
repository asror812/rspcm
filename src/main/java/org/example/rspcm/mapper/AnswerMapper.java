package org.example.rspcm.mapper;

import org.example.rspcm.dto.answer.AnswerResponse;
import org.example.rspcm.model.entity.StudentAnswer;

public final class AnswerMapper {
    private AnswerMapper() {
    }

    public static AnswerResponse toResponse(StudentAnswer answer) {
        return new AnswerResponse(
                answer.getId(),
                SummaryMapper.toQuestionSummary(answer.getExamQuestion().getQuestion()),
                answer.getTextAnswer(),
                answer.getSelectedOptions().stream().map(option -> option.getQuestionOption().getId()).toList(),
                answer.getScore(),
                answer.getCorrect(),
                answer.getAnsweredAt()
        );
    }
}
