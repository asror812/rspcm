package org.example.rspcm.mapper;

import org.example.rspcm.dto.answer.AnswerResponse;
import org.example.rspcm.model.entity.Answer;

public final class AnswerMapper {
    private AnswerMapper() {
    }

    public static AnswerResponse toResponse(Answer answer) {
        return new AnswerResponse(
                answer.getId(),
                SummaryMapper.toQuestionSummary(answer.getQuestion()),
                SummaryMapper.toUserSummary(answer.getStudent()),
                answer.getAnswerText(),
                answer.getAnswerUrl(),
                answer.getFilePath(),
                answer.getSelectedOption(),
                answer.getScore(),
                answer.getFeedback(),
                answer.getSubmittedAt()
        );
    }
}
