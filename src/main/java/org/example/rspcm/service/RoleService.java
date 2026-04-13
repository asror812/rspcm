package org.example.rspcm.service;

import org.example.rspcm.exception.BadRequestException;
import org.example.rspcm.model.entity.AppRole;
import org.example.rspcm.model.enums.RoleName;
import org.example.rspcm.repository.AppRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final AppRoleRepository roleRepository;

    public Set<AppRole> resolveRoles(Set<RoleName> roleNames) {
        return roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new BadRequestException("Role not found: " + name)))
                .collect(Collectors.toSet());
    }
}
