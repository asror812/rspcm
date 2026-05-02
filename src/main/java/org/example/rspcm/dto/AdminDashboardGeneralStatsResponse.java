package org.example.rspcm.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDashboardGeneralStatsResponse {
    private Long totalStudents;
    private Long totalStudyGroups;
    private Long pendingReports;
    private Long approvedReports;
    private Long totalGroups;
}
