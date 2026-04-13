package org.example.rspcm.service;

import org.example.rspcm.dto.practice.PracticeTopicRequest;
import org.example.rspcm.exception.NotFoundException;
import org.example.rspcm.model.entity.PracticeTopic;
import org.example.rspcm.repository.PracticeRepository;
import org.example.rspcm.repository.PracticeTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PracticeTopicService {

    private final PracticeTopicRepository topicRepository;
    private final PracticeRepository practiceRepository;

    public List<PracticeTopic> getByPracticeId(Long practiceId) {
        return topicRepository.findByPracticeId(practiceId);
    }

    public PracticeTopic findById(Long id) {
        return topicRepository.findById(id).orElseThrow(() -> new NotFoundException("Topic topilmadi: " + id));
    }

    @Transactional
    public PracticeTopic create(Long practiceId, PracticeTopicRequest request) {
        PracticeTopic topic = PracticeTopic.builder()
                .practice(practiceRepository.findById(practiceId)
                        .orElseThrow(() -> new NotFoundException("Practice topilmadi: " + practiceId)))
                .title(request.title())
                .description(request.description())
                .build();
        return topicRepository.save(topic);
    }

    @Transactional
    public PracticeTopic update(Long id, PracticeTopicRequest request) {
        PracticeTopic topic = findById(id);
        topic.setTitle(request.title());
        topic.setDescription(request.description());
        return topicRepository.save(topic);
    }

    @Transactional
    public void delete(Long id) {
        topicRepository.delete(findById(id));
    }
}
