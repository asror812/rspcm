package org.example.rspcm.mapper;

import org.example.rspcm.dto.profile.StudentProfileResponse;
import org.example.rspcm.model.entity.StudentProfile;

public final class StudentProfileMapper {
    private StudentProfileMapper() {
    }

    public static StudentProfileResponse toResponse(StudentProfile profile) {
        return new StudentProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getCourse(),
                profile.getStudentNumber(),
                profile.getPhoneNumber(),
                profile.getNotes()
        );
    }
}
