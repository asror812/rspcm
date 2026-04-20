package org.example.rspcm.dto.common;

import java.time.LocalDateTime;

public record PracticeSummary(
        Long id,
        String name,
        LocalDateTime deadline
) {
}
