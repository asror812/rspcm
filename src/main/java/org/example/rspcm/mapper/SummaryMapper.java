package org.example.rspcm.mapper;

import org.example.rspcm.dto.common.ExamSummary;
import org.example.rspcm.dto.common.GroupSummary;
import org.example.rspcm.dto.common.PracticeSummary;
import org.example.rspcm.dto.common.PracticeTeamSummary;
import org.example.rspcm.dto.common.QuestionSummary;
import org.example.rspcm.dto.common.SubjectSummary;
import org.example.rspcm.dto.common.UserSummary;
import org.example.rspcm.model.entity.*;

public final class SummaryMapper {
    private SummaryMapper() {
    }

    public static UserSummary toUserSummary(User user) {
        return new UserSummary(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public static SubjectSummary toSubjectSummary(Subject subject) {
        return new SubjectSummary(subject.getId(), subject.getName(), subject.getDescription());
    }

    public static GroupSummary toGroupSummary(StudyGroup group) {
        return new GroupSummary(group.getId(), group.getName(), group.getLanguage());
    }

    public static PracticeSummary toPracticeSummary(PracticalTask practicalTask) {
        return new PracticeSummary(practicalTask.getId(), practicalTask.getName(), practicalTask.getDeadline());
    }

    public static ExamSummary toExamSummary(Exam exam) {
        return new ExamSummary(exam.getId(), exam.getTitle(), exam.getEndAt());
    }

    public static QuestionSummary toQuestionSummary(Question question) {
        return new QuestionSummary(question.getId(), question.getText(), question.getType());
    }

    public static PracticeTeamSummary toPracticeTeamSummary(PracticeTeam team) {
        return new PracticeTeamSummary(team.getId(), team.getName());
    }
}
