package org.example.rspcm.repository;

import org.example.rspcm.model.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByStudentId(Long studentId);
    List<Answer> findByQuestionId(Long questionId);
}
