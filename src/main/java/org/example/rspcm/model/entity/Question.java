package org.example.rspcm.model.entity;

import jakarta.persistence.*;
import org.example.rspcm.model.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @Column(length = 4000)
    private String optionsJson;

    @Column(length = 2000)
    private String correctAnswer;

    private Integer maxScore;

    @ManyToMany(mappedBy = "questions")
    private List<Exam> exams;
}
