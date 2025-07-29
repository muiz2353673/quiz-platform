package com.quizapp.quiz_platform.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    private String correctOption; // e.g. "A", "B", "C", or "D"

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
}
