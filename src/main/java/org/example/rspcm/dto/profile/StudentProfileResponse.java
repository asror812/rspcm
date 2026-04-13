package org.example.rspcm.dto.profile;

public record StudentProfileResponse(
        Long id,
        Long userId,
        Integer course,
        String studentNumber,
        String phoneNumber,
        String notes
) {
}
