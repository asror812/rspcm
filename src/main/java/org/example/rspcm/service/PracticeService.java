package org.example.rspcm.service;

import org.example.rspcm.dto.practice.PracticeRequest;
import org.example.rspcm.exception.ErrorCodes;
import org.example.rspcm.exception.ErrorMessageException;
import org.example.rspcm.exception.NotFoundException;
import org.example.rspcm.model.entity.PracticalTask;
import org.example.rspcm.model.enums.WorkMode;
import org.example.rspcm.repository.PracticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PracticeService {

    private final PracticeRepository practiceRepository;
    private final CurrentUserService currentUserService;

    public List<PracticalTask> findAll() {
        return practiceRepository.findAll();
    }

    public PracticalTask findById(Long id) {
        return practiceRepository.findById(id).orElseThrow(() -> new NotFoundException("PracticalTask topilmadi: " + id));
    }

    @Transactional
    public PracticalTask create(PracticeRequest request) {
        validateTeamConfig(request.workMode(), request.teamSize());
        PracticalTask practicalTask = PracticalTask.builder()
                .name(request.name())
                .description(request.description())
                .resourceUrl(request.resourceUrl())
                .requirements(request.requirements())
                .deadline(request.deadline())
                .workMode(request.workMode())
                .teamSize(request.teamSize())
                .schedulingRequired(request.schedulingRequired())
                .createdBy(currentUserService.getCurrentUser())
                .build();
        return practiceRepository.save(practicalTask);
    }

    @Transactional
    public PracticalTask update(Long id, PracticeRequest request) {
        validateTeamConfig(request.workMode(), request.teamSize());
        PracticalTask practicalTask = findById(id);
        practicalTask.setName(request.name());
        practicalTask.setDescription(request.description());
        practicalTask.setResourceUrl(request.resourceUrl());
        practicalTask.setRequirements(request.requirements());
        practicalTask.setDeadline(request.deadline());
        practicalTask.setWorkMode(request.workMode());
        practicalTask.setTeamSize(request.teamSize());
        practicalTask.setSchedulingRequired(request.schedulingRequired());
        return practiceRepository.save(practicalTask);
    }

    @Transactional
    public PracticalTask assignGroups(Long practiceId) {
        PracticalTask practicalTask = findById(practiceId);
        return practiceRepository.save(practicalTask);
    }

    @Transactional
    public void delete(Long id) {
        PracticalTask practicalTask = findById(id);
        practiceRepository.delete(practicalTask);
    }

    private void validateTeamConfig(WorkMode mode, Integer teamSize) {
        if (mode == WorkMode.TEAM && (teamSize == null || teamSize < 2)) {
            throw new ErrorMessageException("TEAM mode uchun teamSize kamida 2 bo'lishi kerak", ErrorCodes.BadRequest);
        }
        if (mode == WorkMode.INDIVIDUAL && teamSize != null) {
            throw new ErrorMessageException("INDIVIDUAL mode uchun teamSize bo'sh bo'lishi kerak", ErrorCodes.BadRequest);
        }
    }
}
