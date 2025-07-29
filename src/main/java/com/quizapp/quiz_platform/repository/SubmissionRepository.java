package com.quizapp.quiz_platform.repository;

import com.quizapp.quiz_platform.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByCandidateId(Long candidateId);
    List<Submission> findByQuizId(Long quizId);
    List<Submission> findByCandidateIdOrderBySubmissionTimeDesc(Long candidateId);
}
