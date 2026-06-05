package org.example.rspcm.dto.profile;

import org.example.rspcm.dto.common.GroupSummary;
import org.example.rspcm.dto.common.UserSummary;

import java.time.LocalDate;

public record StudentProfileResponse(
        Long id,
        UserSummary user,
        Integer course,
        GroupSummary group,
        LocalDate birthDate
) {
}
