package org.example.rspcm.service;

import org.example.rspcm.dto.practice.PracticeRequest;
import org.example.rspcm.exception.ErrorCodes;
import org.example.rspcm.exception.ErrorMessageException;
import org.example.rspcm.exception.NotFoundException;
import org.example.rspcm.model.entity.User;
import org.example.rspcm.model.entity.Practice;
import org.example.rspcm.model.entity.StudyGroup;
import org.example.rspcm.model.enums.WorkMode;
import org.example.rspcm.repository.AppUserRepository;
import org.example.rspcm.repository.PracticeRepository;
import org.example.rspcm.repository.StudyGroupRepository;
import org.example.rspcm.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PracticeService {

    private final PracticeRepository practiceRepository;
    private final StudyGroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final AppUserRepository userRepository;
    private final CurrentUserService currentUserService;

    public List<Practice> findAll() {
        return practiceRepository.findAll();
    }

    public Practice findById(Long id) {
        return practiceRepository.findById(id).orElseThrow(() -> new NotFoundException("Practice topilmadi: " + id));
    }

    @Transactional
    public Practice create(PracticeRequest request) {
        validateTeamConfig(request.workMode(), request.teamSize());
        Practice practice = Practice.builder()
                .name(request.name())
                .description(request.description())
                .resourceUrl(request.resourceUrl())
                .requirements(request.requirements())
                .deadline(request.deadline())
                .workMode(request.workMode())
                .teamSize(request.teamSize())
                .calendarRequired(request.calendarRequired())
                .groups(resolveGroups(request.groupIds()))
                .targetStudents(resolveStudents(request.studentIds()))
                .createdBy(currentUserService.getCurrentUser())
                .subject(request.subjectId() == null ? null : subjectRepository.findById(request.subjectId())
                        .orElseThrow(() -> new NotFoundException("Subject topilmadi: " + request.subjectId())))
                .build();
        return practiceRepository.save(practice);
    }

    @Transactional
    public Practice update(Long id, PracticeRequest request) {
        validateTeamConfig(request.workMode(), request.teamSize());
        Practice practice = findById(id);
        practice.setName(request.name());
        practice.setDescription(request.description());
        practice.setResourceUrl(request.resourceUrl());
        practice.setRequirements(request.requirements());
        practice.setDeadline(request.deadline());
        practice.setWorkMode(request.workMode());
        practice.setTeamSize(request.teamSize());
        practice.setCalendarRequired(request.calendarRequired());
        practice.setGroups(resolveGroups(request.groupIds()));
        practice.setTargetStudents(resolveStudents(request.studentIds()));
        practice.setSubject(request.subjectId() == null ? null : subjectRepository.findById(request.subjectId())
                .orElseThrow(() -> new NotFoundException("Subject topilmadi: " + request.subjectId())));
        return practiceRepository.save(practice);
    }

    @Transactional
    public Practice assignGroups(Long practiceId, Set<Long> groupIds) {
        Practice practice = findById(practiceId);
        practice.setGroups(resolveGroups(groupIds));
        return practiceRepository.save(practice);
    }

    @Transactional
    public void delete(Long id) {
        Practice practice = findById(id);
        practiceRepository.delete(practice);
    }

    private void validateTeamConfig(WorkMode mode, Integer teamSize) {
        if (mode == WorkMode.TEAM && (teamSize == null || teamSize < 2)) {
            throw new ErrorMessageException("TEAM mode uchun teamSize kamida 2 bo'lishi kerak", ErrorCodes.BadRequest);
        }
        if (mode == WorkMode.INDIVIDUAL && teamSize != null) {
            throw new ErrorMessageException("INDIVIDUAL mode uchun teamSize bo'sh bo'lishi kerak", ErrorCodes.BadRequest);
        }
    }

    private Set<StudyGroup> resolveGroups(Set<Long> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(groupRepository.findAllById(groupIds));
    }

    private Set<User> resolveStudents(Set<Long> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(userRepository.findAllById(studentIds));
    }
}
