package org.example.rspcm.service;

import org.example.rspcm.dto.question.QuestionRequest;
import org.example.rspcm.exception.NotFoundException;
import org.example.rspcm.model.entity.Question;
import org.example.rspcm.model.entity.QuestionOption;
import org.example.rspcm.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new NotFoundException("Question topilmadi: " + id));
    }

    @Transactional
    public Question create(QuestionRequest request) {
        Question question = Question.builder()
                .text(request.text())
                .type(request.type())
                .options(new ArrayList<>())
                .build();
        applyOptions(question, request);

        return questionRepository.save(question);
    }

    @Transactional
    public Question update(Long id, QuestionRequest request) {
        Question question = findById(id);
        question.setText(request.text());
        question.setType(request.type());
        applyOptions(question, request);
        return questionRepository.save(question);
    }

    @Transactional
    public void delete(Long id) {
        Question question = findById(id);
        questionRepository.delete(question);
    }

    private void applyOptions(Question question, QuestionRequest request) {
        question.getOptions().clear();
        if (request.options() == null || request.options().isEmpty()) {
            return;
        }
        int i = 0;
        for (var optionRequest : request.options()) {
            QuestionOption option = new QuestionOption();
            option.setQuestion(question);
            option.setText(optionRequest.text());
            option.setCorrect(optionRequest.correct());
            option.setOrderIndex(optionRequest.orderIndex() == null ? i : optionRequest.orderIndex());
            question.getOptions().add(option);
            i++;
        }
    }
}
