package com.quizapp.quiz_platform.controller;

import com.quizapp.quiz_platform.model.Quiz;
import com.quizapp.quiz_platform.model.Question;
import com.quizapp.quiz_platform.model.User;
import com.quizapp.quiz_platform.model.Submission;
import com.quizapp.quiz_platform.repository.QuizRepository;
import com.quizapp.quiz_platform.repository.QuestionRepository;
import com.quizapp.quiz_platform.repository.UserRepository;
import com.quizapp.quiz_platform.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruiter")
public class RecruiterQuizController {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;

    @GetMapping("/quizzes")
    public String listQuizzes(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User recruiter = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        model.addAttribute("quizzes", quizRepository.findAll().stream()
                .filter(q -> q.getRecruiter().getId().equals(recruiter.getId())).toList());
        return "recruiter/quizzes";
    }

    @GetMapping("/quizzes/new")
    public String newQuizForm(Model model) {
        model.addAttribute("quiz", new Quiz());
        return "recruiter/newQuiz";
    }

    @PostMapping("/quizzes")
    public String createQuiz(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute Quiz quiz) {
        User recruiter = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        quiz.setRecruiter(recruiter);
        quizRepository.save(quiz);
        return "redirect:/recruiter/quizzes";
    }

    @GetMapping("/quizzes/{id}/questions")
    public String viewQuestions(@PathVariable Long id, Model model) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        List<Question> questions = questionRepository.findByQuizId(id);
        
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);
        return "recruiter/questions";
    }

    @GetMapping("/quizzes/{id}/questions/new")
    public String newQuestionForm(@PathVariable Long id, Model model) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        Question question = new Question();
        question.setQuiz(quiz);
        
        model.addAttribute("quiz", quiz);
        model.addAttribute("question", question);
        return "recruiter/newQuestion";
    }

    @PostMapping("/quizzes/{id}/questions")
    public String createQuestion(@PathVariable Long id, @ModelAttribute Question question) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        
        // Create a new Question entity to avoid optimistic locking issues
        Question newQuestion = new Question();
        newQuestion.setQuestionText(question.getQuestionText());
        newQuestion.setOptionA(question.getOptionA());
        newQuestion.setOptionB(question.getOptionB());
        newQuestion.setOptionC(question.getOptionC());
        newQuestion.setOptionD(question.getOptionD());
        newQuestion.setCorrectOption(question.getCorrectOption());
        newQuestion.setQuiz(quiz);
        
        questionRepository.save(newQuestion);
        return "redirect:/recruiter/quizzes/" + id + "/questions";
    }

    @GetMapping("/quizzes/{id}/results")
    public String viewQuizResults(@PathVariable Long id, Model model) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        
        // Get all submissions for this quiz
        List<Submission> submissions = submissionRepository.findByQuizId(id);
        
        // Calculate statistics
        double averageScore = 0.0;
        double highestScore = 0.0;
        double lowestScore = 100.0;
        
        if (!submissions.isEmpty()) {
            averageScore = submissions.stream()
                    .mapToDouble(s -> s.getPercentage())
                    .average()
                    .orElse(0.0);
            
            highestScore = submissions.stream()
                    .mapToDouble(s -> s.getPercentage())
                    .max()
                    .orElse(0.0);
            
            lowestScore = submissions.stream()
                    .mapToDouble(s -> s.getPercentage())
                    .min()
                    .orElse(100.0);
        }
        
        model.addAttribute("quiz", quiz);
        model.addAttribute("submissions", submissions);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("highestScore", highestScore);
        model.addAttribute("lowestScore", lowestScore);
        return "recruiter/quizResults";
    }

    @GetMapping("/quizzes/{id}/edit")
    public String editQuizForm(@PathVariable Long id, Model model) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        
        model.addAttribute("quiz", quiz);
        return "recruiter/editQuiz";
    }

    @PostMapping("/quizzes/{id}/edit")
    public String updateQuiz(@PathVariable Long id, @ModelAttribute Quiz quiz) {
        Quiz existingQuiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        
        // Update the existing quiz with new values
        existingQuiz.setTitle(quiz.getTitle());
        existingQuiz.setDescription(quiz.getDescription());
        existingQuiz.setDurationInMinutes(quiz.getDurationInMinutes());
        
        quizRepository.save(existingQuiz);
        return "redirect:/recruiter/quizzes";
    }
}
