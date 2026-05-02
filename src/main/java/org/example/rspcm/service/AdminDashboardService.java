package org.example.rspcm.service;

import lombok.RequiredArgsConstructor;
import org.example.rspcm.dto.AdminDashboardGeneralStatsResponse;
import org.example.rspcm.dto.group.GroupResponse;
import org.example.rspcm.model.entity.User;
import org.example.rspcm.repository.StudentProfileRepository;
import org.example.rspcm.repository.StudyGroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final StudyGroupRepository studyGroupRepository;
    private final StudentProfileRepository studentProfileRepository;


    public AdminDashboardGeneralStatsResponse getGeneralStats() {
        Long studentCount = studentProfileRepository.count();
        Long groupCount = studyGroupRepository.count();

        return AdminDashboardGeneralStatsResponse.builder()
                .totalStudents(studentCount)
                .totalGroups(groupCount)
                .pendingReports(null)
                .approvedReports(null)
                .build();
    }


    public Page<AdminDashboardGeneralStatsResponse> getRecentReports(User user) {
        return null;
    }

    public List<GroupResponse> getOwnStudyGroups(User user) {

        return null;
    }
}
