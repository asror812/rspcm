package org.example.rspcm.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record StudentProfileUpdateRequest(
        @Min(value = 1, message = "Значение должно быть не меньше {value}") @Max(value = 8, message = "Значение должно быть не больше {value}") Integer course,
        Long groupId,
        String firstName,
        String lastName,
        @Email(message = "Некорректный формат электронной почты") String email,
        @Size(min = 6, message = "Новый пароль должен содержать минимум {min} символов") String newPassword,
        String currentPassword,
        LocalDate birthDate
) {
}
