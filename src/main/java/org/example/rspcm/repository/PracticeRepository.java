package org.example.rspcm.repository;

import org.example.rspcm.model.entity.PracticalTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository extends JpaRepository<PracticalTask, Long> {

    @Query("select p from PracticalTask p join p.exams e where e.id = :examId")
    List<PracticalTask> findPracticesByExamId(Long examId);
}
