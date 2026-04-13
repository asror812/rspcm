package org.example.rspcm.config;

import org.example.rspcm.model.entity.AppRole;
import org.example.rspcm.model.enums.RoleName;
import org.example.rspcm.repository.AppRoleRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AppRoleRepository roleRepository;

    @Override
    public void run(String @NonNull ... args) {
        Arrays.stream(RoleName.values())
                .forEach(roleName -> roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(AppRole.builder().name(roleName).build())));
    }
}
