package org.example.rspcm.mapper;

import org.example.rspcm.dto.exam.ExamQuestionResponse;
import org.example.rspcm.model.entity.ExamQuestion;

public final class ExamQuestionMapper {
    private ExamQuestionMapper() {
    }

    public static ExamQuestionResponse toResponse(ExamQuestion examQuestion) {
        return new ExamQuestionResponse(
                examQuestion.getId(),
                SummaryMapper.toExamSummary(examQuestion.getExam()),
                SummaryMapper.toQuestionSummary(examQuestion.getQuestion()),
                examQuestion.getScore(),
                examQuestion.getOrderIndex()
        );
    }
}
