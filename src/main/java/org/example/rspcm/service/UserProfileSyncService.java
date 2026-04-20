package org.example.rspcm.service;

import org.example.rspcm.model.entity.*;
import org.example.rspcm.model.enums.RoleName;
import org.example.rspcm.repository.AdminProfileRepository;
import org.example.rspcm.repository.StudentProfileRepository;
import org.example.rspcm.repository.TeacherProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileSyncService {

    private final AdminProfileRepository adminProfileRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final StudentProfileRepository studentProfileRepository;

    @Transactional
    public void sync(User user) {
        Set<RoleName> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        syncAdmin(user, roleNames.contains(RoleName.ADMIN));
        syncTeacher(user, roleNames.contains(RoleName.TEACHER));
        syncStudent(user, roleNames.contains(RoleName.STUDENT));
    }

    private void syncAdmin(User user, boolean hasRole) {
        if (hasRole) {
            adminProfileRepository.findByUserId(user.getId())
                    .orElseGet(() -> adminProfileRepository.save(AdminProfile.builder().user(user).build()));
            return;
        }
        adminProfileRepository.deleteByUserId(user.getId());
    }

    private void syncTeacher(User user, boolean hasRole) {
        if (hasRole) {
            teacherProfileRepository.findByUserId(user.getId())
                    .orElseGet(() -> teacherProfileRepository.save(TeacherProfile.builder().user(user).build()));
            return;
        }
        teacherProfileRepository.deleteByUserId(user.getId());
    }

    private void syncStudent(User user, boolean hasRole) {
        if (hasRole) {
            studentProfileRepository.findByUserId(user.getId())
                    .orElseGet(() -> studentProfileRepository.save(StudentProfile.builder().user(user).build()));
            return;
        }
        studentProfileRepository.deleteByUserId(user.getId());
    }
}
