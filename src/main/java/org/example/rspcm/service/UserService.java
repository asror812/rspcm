package org.example.rspcm.service;

import org.example.rspcm.dto.user.UserCreateRequest;
import org.example.rspcm.dto.user.UserUpdateRequest;
import org.example.rspcm.exception.BadRequestException;
import org.example.rspcm.exception.NotFoundException;
import org.example.rspcm.model.entity.AppUser;
import org.example.rspcm.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileSyncService userProfileSyncService;

    public List<AppUser> findAll() {
        return userRepository.findAll();
    }

    public AppUser findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User topilmadi: " + id));
    }

    @Transactional
    public AppUser create(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email allaqachon mavjud");
        }
        AppUser user = AppUser.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enabled(request.enabled())
                .roles(roleService.resolveRoles(request.roles()))
                .build();
        AppUser saved = userRepository.save(user);
        userProfileSyncService.sync(saved);
        return saved;
    }

    @Transactional
    public AppUser update(Long id, UserUpdateRequest request) {
        AppUser user = findById(id);
        user.setFullName(request.fullName());
        user.setEnabled(request.enabled());
        user.setRoles(roleService.resolveRoles(request.roles()));
        AppUser saved = userRepository.save(user);
        userProfileSyncService.sync(saved);
        return saved;
    }

    @Transactional
    public void delete(Long id) {
        AppUser user = findById(id);
        userRepository.delete(user);
    }
}
