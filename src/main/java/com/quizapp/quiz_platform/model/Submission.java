package com.quizapp.quiz_platform.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;
    private int totalQuestions;
    private double percentage;

    private LocalDateTime submissionTime;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private User candidate;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
}
