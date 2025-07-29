package com.quizapp.quiz_platform.repository;

import com.quizapp.quiz_platform.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
