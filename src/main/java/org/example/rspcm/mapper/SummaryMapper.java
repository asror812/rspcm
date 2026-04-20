package org.example.rspcm.mapper;

import org.example.rspcm.dto.common.ExamSummary;
import org.example.rspcm.dto.common.GroupSummary;
import org.example.rspcm.dto.common.PracticeSummary;
import org.example.rspcm.dto.common.PracticeTeamSummary;
import org.example.rspcm.dto.common.QuestionSummary;
import org.example.rspcm.dto.common.SubjectSummary;
import org.example.rspcm.dto.common.UserSummary;
import org.example.rspcm.model.entity.Exam;
import org.example.rspcm.model.entity.Practice;
import org.example.rspcm.model.entity.PracticeTeam;
import org.example.rspcm.model.entity.Question;
import org.example.rspcm.model.entity.StudyGroup;
import org.example.rspcm.model.entity.Subject;
import org.example.rspcm.model.entity.User;

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

    public static PracticeSummary toPracticeSummary(Practice practice) {
        return new PracticeSummary(practice.getId(), practice.getName(), practice.getDeadline());
    }

    public static ExamSummary toExamSummary(Exam exam) {
        return new ExamSummary(exam.getId(), exam.getTitle(), exam.getEndAt());
    }

    public static QuestionSummary toQuestionSummary(Question question) {
        return new QuestionSummary(question.getId(), question.getText(), question.getType(), question.getMaxScore());
    }

    public static PracticeTeamSummary toPracticeTeamSummary(PracticeTeam team) {
        return new PracticeTeamSummary(team.getId(), team.getName());
    }
}
