package org.example.rspcm.dto.profile;

import jakarta.validation.constraints.Min;

import java.util.Set;

public record TeacherProfileUpdateRequest(
        String academicDegree,
        @Min(0) Integer experienceYears,
        Set<Long> teachingSubjectIds
) {
}
