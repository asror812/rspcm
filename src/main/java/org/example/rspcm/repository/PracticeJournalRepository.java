package org.example.rspcm.repository;

import org.example.rspcm.model.entity.PracticeJournal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeJournalRepository extends JpaRepository<PracticeJournal, Long> {
    List<PracticeJournal> findByStudentId(Long studentId);
    List<PracticeJournal> findByPracticeId(Long practiceId);
}
