package org.example.rspcm.dto.profile;

import java.util.Set;

public record TeacherProfileResponse(
        Long id,
        Long userId,
        String academicDegree,
        Integer experienceYears,
        Set<Long> teachingSubjectIds
) {
}
