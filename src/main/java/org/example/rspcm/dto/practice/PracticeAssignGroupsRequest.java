package org.example.rspcm.dto.practice;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record PracticeAssignGroupsRequest(
        @NotEmpty Set<Long> groupIds
) {
}
