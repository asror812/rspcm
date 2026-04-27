package org.example.rspcm.repository;

import org.example.rspcm.model.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select q from Question q join q.exams e where e.id = :examId")
    List<Question> findQuestionsByExamId(Long examId);
}
