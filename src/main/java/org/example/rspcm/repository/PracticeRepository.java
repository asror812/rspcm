package org.example.rspcm.repository;

import org.example.rspcm.model.entity.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long> {

    @Query("select p from Practice p join p.exams e where e.id = :examId")
    List<Practice> findPracticesByExamId(Long examId);
}
