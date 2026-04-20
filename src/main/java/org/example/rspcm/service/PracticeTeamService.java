package org.example.rspcm.service;

import org.example.rspcm.dto.practice.PracticeTeamRequest;
import org.example.rspcm.exception.ErrorCodes;
import org.example.rspcm.exception.ErrorMessageException;
import org.example.rspcm.exception.NotFoundException;
import org.example.rspcm.model.entity.User;
import org.example.rspcm.model.entity.Practice;
import org.example.rspcm.model.entity.PracticeTeam;
import org.example.rspcm.model.enums.WorkMode;
import org.example.rspcm.repository.AppUserRepository;
import org.example.rspcm.repository.PracticeRepository;
import org.example.rspcm.repository.PracticeTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PracticeTeamService {

    private final PracticeTeamRepository teamRepository;
    private final PracticeRepository practiceRepository;
    private final AppUserRepository userRepository;

    public List<PracticeTeam> getByPracticeId(Long practiceId) {
        return teamRepository.findByPracticeId(practiceId);
    }

    @Transactional
    public PracticeTeam create(PracticeTeamRequest request) {
        Practice practice = practiceRepository.findById(request.practiceId())
                .orElseThrow(() -> new NotFoundException("Practice topilmadi: " + request.practiceId()));
        if (practice.getWorkMode() != WorkMode.TEAM) {
            throw new ErrorMessageException("Bu practice individual rejimda", ErrorCodes.BadRequest);
        }
        Set<User> members = new HashSet<>(userRepository.findAllById(request.memberIds()));
        Integer teamSize = practice.getTeamSize();
        if (teamSize != null && members.size() > teamSize) {
            throw new ErrorMessageException("Jamoa a'zolari soni teamSize dan oshib ketdi", ErrorCodes.BadRequest);
        }

        PracticeTeam team = PracticeTeam.builder()
                .practice(practice)
                .name(request.name())
                .members(members)
                .build();
        return teamRepository.save(team);
    }
}
