package org.example.rspcm.dto.student;

import java.time.LocalDateTime;

public record StudentTaskItem(
        Long id,
        String title,
        LocalDateTime deadline,
        String type
) {
}
