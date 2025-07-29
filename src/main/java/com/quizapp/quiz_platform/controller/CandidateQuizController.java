package com.quizapp.quiz_platform.controller;

import com.quizapp.quiz_platform.model.Quiz;
import com.quizapp.quiz_platform.model.Question;
import com.quizapp.quiz_platform.model.Submission;
import com.quizapp.quiz_platform.model.User;
import com.quizapp.quiz_platform.repository.QuizRepository;
import com.quizapp.quiz_platform.repository.QuestionRepository;
import com.quizapp.quiz_platform.repository.SubmissionRepository;
import com.quizapp.quiz_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/candidate")
public class CandidateQuizController {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    @GetMapping("/quizzes")
    public String listQuizzes(Model model) {
        List<Quiz> quizzes = quizRepository.findAll();
        model.addAttribute("quizzes", quizzes);
        return "candidate/quizzes";
    }

    @GetMapping("/quizzes/{id}/take")
    public String takeQuiz(@PathVariable Long id, Model model) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        List<Question> questions = questionRepository.findByQuizId(id);
        
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);
        return "candidate/takeQuiz";
    }

    @PostMapping("/quizzes/{id}/submit")
    public String submitQuiz(@PathVariable Long id, 
                           @RequestParam Map<String, String> answers, 
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        User candidate = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Question> questions = questionRepository.findByQuizId(id);
        
        int correctAnswers = 0;
        int totalQuestions = questions.size();
        
        for (Question question : questions) {
            String userAnswer = answers.get("question_" + question.getId());
            if (userAnswer != null && userAnswer.equals(question.getCorrectOption())) {
                correctAnswers++;
            }
        }
        
        double score = totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0;
        
        // Save submission to database
        Submission submission = new Submission();
        submission.setCandidate(candidate);
        submission.setQuiz(quiz);
        submission.setScore(correctAnswers);
        submission.setTotalQuestions(totalQuestions);
        submission.setPercentage(score);
        submission.setSubmissionTime(LocalDateTime.now());
        submissionRepository.save(submission);
        
        model.addAttribute("quiz", quiz);
        model.addAttribute("score", score);
        model.addAttribute("correctAnswers", correctAnswers);
        model.addAttribute("totalQuestions", totalQuestions);
        
        return "candidate/quizResult";
    }

    @GetMapping("/submissions")
    public String viewSubmissions(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User candidate = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Submission> submissions = submissionRepository.findByCandidateIdOrderBySubmissionTimeDesc(candidate.getId());
        model.addAttribute("submissions", submissions);
        return "candidate/submissions";
    }

    @GetMapping("/results")
    public String viewResults(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User candidate = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Submission> submissions = submissionRepository.findByCandidateIdOrderBySubmissionTimeDesc(candidate.getId());
        
        // Calculate statistics
        double averageScore = submissions.stream()
                .mapToDouble(s -> s.getPercentage())
                .average()
                .orElse(0.0);
        
        double bestScore = submissions.stream()
                .mapToDouble(s -> s.getPercentage())
                .max()
                .orElse(0.0);
        
        model.addAttribute("submissions", submissions);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("bestScore", bestScore);
        return "candidate/results";
    }
}
