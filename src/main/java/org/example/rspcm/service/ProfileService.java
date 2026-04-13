package org.example.rspcm.service;

import org.example.rspcm.dto.profile.StudentProfileUpdateRequest;
import org.example.rspcm.dto.profile.TeacherProfileUpdateRequest;
import org.example.rspcm.exception.NotFoundException;
import org.example.rspcm.model.entity.AppUser;
import org.example.rspcm.model.entity.StudentProfile;
import org.example.rspcm.model.entity.Subject;
import org.example.rspcm.model.entity.TeacherProfile;
import org.example.rspcm.repository.AppUserRepository;
import org.example.rspcm.repository.StudentProfileRepository;
import org.example.rspcm.repository.SubjectRepository;
import org.example.rspcm.repository.TeacherProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final AppUserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public StudentProfile getStudentProfile(Long userId) {
        return studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Student profile topilmadi: " + userId));
    }

    public TeacherProfile getTeacherProfile(Long userId) {
        return teacherProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Teacher profile topilmadi: " + userId));
    }

    @Transactional
    public StudentProfile updateStudentProfile(Long userId, StudentProfileUpdateRequest request) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
                .orElseGet(() -> studentProfileRepository.save(StudentProfile.builder().user(getUser(userId)).build()));
        profile.setCourse(request.course());
        profile.setStudentNumber(request.studentNumber());
        profile.setPhoneNumber(request.phoneNumber());
        profile.setNotes(request.notes());
        return studentProfileRepository.save(profile);
    }

    @Transactional
    public TeacherProfile updateTeacherProfile(Long userId, TeacherProfileUpdateRequest request) {
        TeacherProfile profile = teacherProfileRepository.findByUserId(userId)
                .orElseGet(() -> teacherProfileRepository.save(TeacherProfile.builder().user(getUser(userId)).build()));
        profile.setAcademicDegree(request.academicDegree());
        profile.setExperienceYears(request.experienceYears());
        profile.setTeachingSubjects(resolveSubjects(request.teachingSubjectIds()));
        return teacherProfileRepository.save(profile);
    }

    private AppUser getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User topilmadi: " + userId));
    }

    private Set<Subject> resolveSubjects(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(subjectRepository.findAllById(ids));
    }
}
