package org.example.rspcm.mapper;

import org.example.rspcm.dto.practice.PracticalTaskAssignmentResponse;
import org.example.rspcm.model.entity.PracticalTaskAssignment;

public final class PracticalTaskAssignmentMapper {
    private PracticalTaskAssignmentMapper() {
    }

    public static PracticalTaskAssignmentResponse toResponse(PracticalTaskAssignment assignment) {
        return new PracticalTaskAssignmentResponse(
                assignment.getId(),
                SummaryMapper.toExamSummary(assignment.getExam()),
                SummaryMapper.toPracticeSummary(assignment.getPracticalTask()),
                assignment.getStudent() == null ? null : SummaryMapper.toUserSummary(assignment.getStudent()),
                assignment.getTeam() == null ? null : SummaryMapper.toPracticeTeamSummary(assignment.getTeam()),
                assignment.getChosenAt(),
                assignment.getSubmittedAt(),
                assignment.getStatus(),
                assignment.getScore(),
                assignment.getTeacherComment()
        );
    }
}
