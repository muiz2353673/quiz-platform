package com.quizapp.quiz_platform.service;

import com.quizapp.quiz_platform.model.Question;
import com.quizapp.quiz_platform.model.Quiz;
import com.quizapp.quiz_platform.model.User;
import com.quizapp.quiz_platform.repository.QuestionRepository;
import com.quizapp.quiz_platform.repository.QuizRepository;
import com.quizapp.quiz_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Only seed if no users exist
        if (userRepository.count() == 0) {
            seedDatabase();
        }
    }

    private void seedDatabase() {
        // Create sample users
        User recruiter1 = createUser("recruiter1", "password123", "RECRUITER");
        User recruiter2 = createUser("recruiter2", "password123", "RECRUITER");
        User candidate1 = createUser("candidate1", "password123", "CANDIDATE");
        User candidate2 = createUser("candidate2", "password123", "CANDIDATE");
        User candidate3 = createUser("candidate3", "password123", "CANDIDATE");

        // Create sample quizzes
        Quiz javaQuiz = createQuiz("Java Programming Basics", 
            "Test your knowledge of Java programming fundamentals", 30, recruiter1);
        
        Quiz springQuiz = createQuiz("Spring Framework", 
            "Assessment on Spring Boot and Spring Framework concepts", 45, recruiter1);
        
        Quiz databaseQuiz = createQuiz("Database Design", 
            "Test your understanding of database design principles", 25, recruiter2);

        // Create questions for Java Quiz
        createJavaQuestions(javaQuiz);
        
        // Create questions for Spring Quiz
        createSpringQuestions(springQuiz);
        
        // Create questions for Database Quiz
        createDatabaseQuestions(databaseQuiz);

        System.out.println("✅ Database seeded successfully!");
        System.out.println("📝 Sample Users Created:");
        System.out.println("   Recruiters: recruiter1, recruiter2 (password: password123)");
        System.out.println("   Candidates: candidate1, candidate2, candidate3 (password: password123)");
        System.out.println("📊 Sample Quizzes Created:");
        System.out.println("   - Java Programming Basics (3 questions)");
        System.out.println("   - Spring Framework (3 questions)");
        System.out.println("   - Database Design (3 questions)");
    }

    private User createUser(String username, String password, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

    private Quiz createQuiz(String title, String description, int duration, User recruiter) {
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setDurationInMinutes(duration);
        quiz.setRecruiter(recruiter);
        return quizRepository.save(quiz);
    }

    private void createJavaQuestions(Quiz quiz) {
        List<Question> questions = Arrays.asList(
            createQuestion(quiz, 
                "What is the correct way to declare a variable in Java?", 
                "int x = 5;", "var x = 5;", "let x = 5;", "const x = 5;", "A"),
            createQuestion(quiz, 
                "Which keyword is used to inherit from a class in Java?", 
                "extends", "implements", "inherits", "super", "A"),
            createQuestion(quiz, 
                "What is the default value of an int variable in Java?", 
                "0", "null", "undefined", "1", "A")
        );
        questionRepository.saveAll(questions);
    }

    private void createSpringQuestions(Quiz quiz) {
        List<Question> questions = Arrays.asList(
            createQuestion(quiz, 
                "What annotation is used to mark a class as a Spring component?", 
                "@Component", "@Service", "@Repository", "@Controller", "A"),
            createQuestion(quiz, 
                "Which Spring Boot starter is used for web applications?", 
                "spring-boot-starter-web", "spring-boot-starter-data-jpa", 
                "spring-boot-starter-security", "spring-boot-starter-test", "A"),
            createQuestion(quiz, 
                "What is the default embedded server in Spring Boot?", 
                "Tomcat", "Jetty", "Undertow", "WildFly", "A")
        );
        questionRepository.saveAll(questions);
    }

    private void createDatabaseQuestions(Quiz quiz) {
        List<Question> questions = Arrays.asList(
            createQuestion(quiz, 
                "What is a primary key in a database?", 
                "A unique identifier for each record", "A foreign key reference", 
                "An index on a column", "A constraint on data type", "A"),
            createQuestion(quiz, 
                "Which SQL command is used to create a new table?", 
                "CREATE TABLE", "BUILD TABLE", "MAKE TABLE", "GENERATE TABLE", "A"),
            createQuestion(quiz, 
                "What is normalization in database design?", 
                "The process of organizing data to reduce redundancy", 
                "The process of adding more data", 
                "The process of creating indexes", 
                "The process of backing up data", "A")
        );
        questionRepository.saveAll(questions);
    }

    private Question createQuestion(Quiz quiz, String questionText, String optionA, 
                                   String optionB, String optionC, String optionD, String correctOption) {
        Question question = new Question();
        question.setQuiz(quiz);
        question.setQuestionText(questionText);
        question.setOptionA(optionA);
        question.setOptionB(optionB);
        question.setOptionC(optionC);
        question.setOptionD(optionD);
        question.setCorrectOption(correctOption);
        return question;
    }
} 