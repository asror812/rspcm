package org.example.rspcm.mapper;

import org.example.rspcm.dto.profile.TeacherProfileResponse;
import org.example.rspcm.model.entity.Subject;
import org.example.rspcm.model.entity.TeacherProfile;

import java.util.Set;
import java.util.stream.Collectors;

public final class TeacherProfileMapper {
    private TeacherProfileMapper() {
    }

    public static TeacherProfileResponse toResponse(TeacherProfile profile) {
        Set<Long> subjectIds = profile.getTeachingSubjects().stream().map(Subject::getId).collect(Collectors.toSet());
        return new TeacherProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getAcademicDegree(),
                profile.getExperienceYears(),
                subjectIds
        );
    }
}
