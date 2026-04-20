package org.example.rspcm.repository;

import org.example.rspcm.model.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    List<StudyGroup> findByStudentsId(Long studentId);
    Optional<StudyGroup> findByName(String name);
}
