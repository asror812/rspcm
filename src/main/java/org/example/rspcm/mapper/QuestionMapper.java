package org.example.rspcm.mapper;

import org.example.rspcm.dto.question.QuestionResponse;
import org.example.rspcm.model.entity.Question;

public final class QuestionMapper {
    private QuestionMapper() {
    }

    public static QuestionResponse toResponse(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getText(),
                question.getType(),
                question.getOptionsJson(),
                question.getCorrectAnswer(),
                question.getMaxScore(),
                question.getExam() == null ? null : SummaryMapper.toExamSummary(question.getExam()),
                question.getPractice() == null ? null : SummaryMapper.toPracticeSummary(question.getPractice())
        );
    }
}
