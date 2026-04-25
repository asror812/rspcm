package org.example.rspcm.mapper;

import org.example.rspcm.dto.user.UserResponse;
import org.example.rspcm.model.entity.Role;
import org.example.rspcm.model.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {
    private UserMapper() {
    }

    public static UserResponse toResponse(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserResponse(
                user.getId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.isEnabled(), user.isDeleted(), roles);
    }
}
