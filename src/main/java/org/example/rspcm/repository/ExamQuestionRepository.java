package org.example.rspcm.repository;

import org.example.rspcm.model.entity.ExamQuestion;
import org.example.rspcm.model.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

    @Query("select eq from ExamQuestion eq where eq.exam.id = :examId")
    List<Question> findQuestionsByExamId(Long examId);
}
