package org.example.rspcm.service;

import org.example.rspcm.dto.question.QuestionRequest;
import org.example.rspcm.exception.ErrorCodes;
import org.example.rspcm.exception.ErrorMessageException;
import org.example.rspcm.exception.NotFoundException;
import org.example.rspcm.model.entity.Question;
import org.example.rspcm.repository.ExamRepository;
import org.example.rspcm.repository.PracticeRepository;
import org.example.rspcm.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;
    private final PracticeRepository practiceRepository;

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new NotFoundException("Question topilmadi: " + id));
    }

    @Transactional
    public Question create(QuestionRequest request) {
        validateOwner(request.examId(), request.practiceId());
        Question question = Question.builder()
                .text(request.text())
                .type(request.type())
                .optionsJson(request.optionsJson())
                .correctAnswer(request.correctAnswer())
                .maxScore(request.maxScore())
                .exam(request.examId() == null ? null : examRepository.findById(request.examId())
                        .orElseThrow(() -> new NotFoundException("Exam topilmadi: " + request.examId())))
                .practice(request.practiceId() == null ? null : practiceRepository.findById(request.practiceId())
                        .orElseThrow(() -> new NotFoundException("Practice topilmadi: " + request.practiceId())))
                .build();
        return questionRepository.save(question);
    }

    @Transactional
    public Question update(Long id, QuestionRequest request) {
        validateOwner(request.examId(), request.practiceId());
        Question question = findById(id);
        question.setText(request.text());
        question.setType(request.type());
        question.setOptionsJson(request.optionsJson());
        question.setCorrectAnswer(request.correctAnswer());
        question.setMaxScore(request.maxScore());
        question.setExam(request.examId() == null ? null : examRepository.findById(request.examId())
                .orElseThrow(() -> new NotFoundException("Exam topilmadi: " + request.examId())));
        question.setPractice(request.practiceId() == null ? null : practiceRepository.findById(request.practiceId())
                .orElseThrow(() -> new NotFoundException("Practice topilmadi: " + request.practiceId())));
        return questionRepository.save(question);
    }

    @Transactional
    public void delete(Long id) {
        Question question = findById(id);
        questionRepository.delete(question);
    }

    private void validateOwner(Long examId, Long practiceId) {
        if ((examId == null && practiceId == null) || (examId != null && practiceId != null)) {
            throw new ErrorMessageException("Question faqat exam yoki practice bilan bog'lanishi kerak", ErrorCodes.BadRequest);
        }
    }
}
