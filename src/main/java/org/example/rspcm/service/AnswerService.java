package org.example.rspcm.service;

import org.example.rspcm.dto.answer.AnswerRequest;
import org.example.rspcm.dto.answer.AnswerScoreRequest;
import org.example.rspcm.exception.ErrorCodes;
import org.example.rspcm.exception.ErrorMessageException;
import org.example.rspcm.exception.NotFoundException;
import org.example.rspcm.model.entity.Answer;
import org.example.rspcm.model.entity.User;
import org.example.rspcm.repository.AnswerRepository;
import org.example.rspcm.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final CurrentUserService currentUserService;

    public List<Answer> findAll() {
        return answerRepository.findAll();
    }

    public List<Answer> findMine() {
        return answerRepository.findByStudentId(currentUserService.getCurrentUser().getId());
    }

    public Answer findById(Long id) {
        return answerRepository.findById(id).orElseThrow(() -> new NotFoundException("Answer topilmadi: " + id));
    }

    @Transactional
    public Answer create(AnswerRequest request) {
        User student = currentUserService.getCurrentUser();

        Answer answer = Answer.builder()
                .question(questionRepository.findById(request.questionId())
                        .orElseThrow(() -> new NotFoundException("Question topilmadi: " + request.questionId())))
                .student(student)
                .answerText(request.answerText())
                .answerUrl(request.answerUrl())
                .filePath(request.filePath())
                .selectedOption(request.selectedOption())
                .submittedAt(LocalDateTime.now())
                .build();

        return answerRepository.save(answer);
    }

    @Transactional
    public Answer updateMine(Long id, AnswerRequest request) {
        Answer answer = findById(id);
        User current = currentUserService.getCurrentUser();
        if (!answer.getStudent().getId().equals(current.getId())) {
            throw new ErrorMessageException("Faqat o'zingizning javobingizni o'zgartira olasiz", ErrorCodes.BadRequest);
        }
        answer.setQuestion(questionRepository.findById(request.questionId())
                .orElseThrow(() -> new NotFoundException("Question topilmadi: " + request.questionId())));
        answer.setAnswerText(request.answerText());
        answer.setAnswerUrl(request.answerUrl());
        answer.setFilePath(request.filePath());
        answer.setSelectedOption(request.selectedOption());
        answer.setSubmittedAt(LocalDateTime.now());
        return answerRepository.save(answer);
    }

    @Transactional
    public Answer score(Long id, AnswerScoreRequest request) {
        Answer answer = findById(id);
        answer.setScore(request.score());
        answer.setFeedback(request.feedback());
        return answerRepository.save(answer);
    }

    @Transactional
    public void deleteMine(Long id) {
        Answer answer = findById(id);
        User current = currentUserService.getCurrentUser();
        if (!answer.getStudent().getId().equals(current.getId())) {
            throw new ErrorMessageException("Faqat o'zingizning javobingizni o'chira olasiz", ErrorCodes.BadRequest);
        }
        answerRepository.delete(answer);
    }
}
