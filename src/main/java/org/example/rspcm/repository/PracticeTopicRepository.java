package org.example.rspcm.repository;

import org.example.rspcm.model.entity.PracticeTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeTopicRepository extends JpaRepository<PracticeTopic, Long> {
    List<PracticeTopic> findByPracticeId(Long practiceId);
}
