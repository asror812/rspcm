package org.example.rspcm.repository;

import org.example.rspcm.model.entity.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long> {
    List<Practice> findDistinctByGroupsStudentsIdOrTargetStudentsId(Long groupStudentId, Long targetStudentId);
}
