package org.example.rspcm.repository;

import org.example.rspcm.model.entity.Role;
import org.example.rspcm.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
