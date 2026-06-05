package org.example.rspcm.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TeacherSelfProfileUpdateRequest(
        String firstName,
        String lastName,
        @Email(message = "Некорректный формат электронной почты") String email,
        @Size(min = 6, message = "Новый пароль должен содержать минимум {min} символов")
        String newPassword,
        String currentPassword,
        String phoneNumber,
        LocalDate birthDate
) {
}
