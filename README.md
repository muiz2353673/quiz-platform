# 🎯 Quiz Platform - Complete Guide

A modern, feature-rich quiz management system built with Spring Boot, designed for recruiters to create and manage quizzes, and candidates to take assessments. This project demonstrates full-stack development with Java, Spring Boot, Thymeleaf, and Bootstrap.

## 📚 Table of Contents

1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Technology Stack](#technology-stack)
4. [Architecture](#architecture)
5. [Installation & Setup](#installation--setup)
6. [Usage Guide](#usage-guide)
7. [Code Structure](#code-structure)
8. [Database Design](#database-design)
9. [Security Implementation](#security-implementation)
10. [Deployment Options](#deployment-options)
11. [Troubleshooting](#troubleshooting)
12. [Contributing](#contributing)

## 🎯 Project Overview

The Quiz Platform is a comprehensive assessment system that allows:

- **Recruiters** to create, manage, and analyze quizzes
- **Candidates** to take assessments and view their results
- **Real-time scoring** and performance analytics
- **Secure authentication** with role-based access control

### 🎨 What Makes This Project Special

- **Full-Stack Development**: Complete web application from database to UI
- **Modern Architecture**: Spring Boot with clean separation of concerns
- **Security First**: Spring Security with proper authentication and authorization
- **Responsive Design**: Bootstrap-based UI that works on all devices
- **Production Ready**: Includes Docker, deployment scripts, and comprehensive documentation

## ✨ Features

### 🔐 Authentication & Authorization

- **User Registration**: Sign up as either a Recruiter or Candidate
- **Secure Login**: Spring Security with BCrypt password hashing
- **Role-Based Access**: Different permissions for different user types
- **Session Management**: Secure session handling with proper logout

### 👨‍💼 Recruiter Features

- **Quiz Management**: Create, edit, and manage multiple quizzes
- **Question Management**: Add multiple-choice questions with 4 options
- **Results Analysis**: View detailed candidate performance and statistics
- **Dashboard**: Overview of all quizzes with quick access to management tools

### 👨‍🎓 Candidate Features

- **Quiz Browser**: View and take available quizzes
- **Real-time Assessment**: Submit answers and get immediate results
- **Performance Tracking**: View submission history and scores
- **Dashboard**: Access to available quizzes and past submissions

### 🎨 User Interface

- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile
- **Bootstrap 5**: Modern, clean interface using Bootstrap framework
- **Intuitive Navigation**: Easy-to-use interface for both user types
- **Error Handling**: Clear error messages and validation feedback

## 🛠️ Technology Stack

### Backend

- **Java 17**: Modern Java with latest features
- **Spring Boot 3.5.4**: Rapid application development framework
- **Spring Security 6.5.2**: Authentication and authorization
- **Spring Data JPA**: Database access layer
- **H2 Database**: In-memory database for development

### Frontend

- **Thymeleaf**: Server-side template engine
- **Bootstrap 5.1.3**: CSS framework for responsive design
- **HTML5/CSS3**: Modern web standards

### Build & Deployment

- **Gradle**: Build automation tool
- **Docker**: Containerization for deployment
- **Git**: Version control

## 🏗️ Architecture

### MVC Pattern

The application follows the Model-View-Controller pattern:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│     View        │    │   Controller    │    │     Model       │
│  (Thymeleaf)    │◄──►│  (Spring MVC)   │◄──►│   (Entities)    │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │   Repository    │
                       │   (JPA/H2)      │
                       └─────────────────┘
```

### Package Structure

```
src/main/java/com/quizapp/quiz_platform/
├── controller/          # REST controllers (MVC)
├── model/              # Entity classes (Data models)
├── repository/         # Data access layer
├── service/            # Business logic
└── security/           # Security configuration
```

## 📋 Installation & Setup

### Prerequisites

Before running this application, ensure you have:

- **Java 17 or higher** installed
- **Gradle 7.0 or higher** (or use the included Gradle wrapper)
- **Git** for version control
- **Docker** (optional, for containerized deployment)

### Step-by-Step Installation

#### 1. Clone the Repository

```bash
git clone <repository-url>
cd quiz-platform
```

#### 2. Verify Java Installation

```bash
java -version
# Should show Java 17 or higher
```

#### 3. Run the Application

```bash
# Using the deployment script (recommended)
./deploy.sh start

# Or using Gradle directly
./gradlew bootRun
```

#### 4. Access the Application

- **Main Application**: http://localhost:8081
- **H2 Database Console**: http://localhost:8081/h2-console
  - JDBC URL: `jdbc:h2:file:./quizdb`
  - Username: `sa`
  - Password: (leave empty)

## 👥 Default Users

The application comes with pre-configured users for testing:

### Recruiter Account

- **Username**: `recruiter1`
- **Password**: `password123`
- **Role**: RECRUITER

### Candidate Account

- **Username**: `candidate1`
- **Password**: `password123`
- **Role**: CANDIDATE

## 📖 Usage Guide

### For Recruiters

#### 1. Login

- Navigate to http://localhost:8081
- Login with recruiter credentials
- You'll be redirected to the recruiter dashboard

#### 2. Create a Quiz

- Click "Create New Quiz" on the dashboard
- Fill in quiz details:
  - Title: "Programming Basics"
  - Description: "Test your programming knowledge"
  - Duration: 30 minutes
- Click "Create Quiz"

#### 3. Add Questions

- Navigate to the quiz you just created
- Click "Add Questions"
- For each question:
  - Question text: "What is the main method signature?"
  - Option A: "public static void main(String[] args)"
  - Option B: "public void main(String[] args)"
  - Option C: "static void main(String[] args)"
  - Option D: "void main(String[] args)"
  - Correct Option: A
- Click "Add Question" for each question

#### 4. View Results

- Click "View Results" on any quiz
- See candidate submissions and scores
- Analyze performance statistics

### For Candidates

#### 1. Login

- Navigate to http://localhost:8081
- Login with candidate credentials
- You'll see available quizzes on the dashboard

#### 2. Take a Quiz

- Click "Take Quiz" on any available quiz
- Read the instructions and click "Start Quiz"
- Answer all questions
- Click "Submit Quiz" when finished

#### 3. View Results

- See your score immediately after submission
- View detailed breakdown of correct/incorrect answers
- Check your submission history

## 🏗️ Code Structure

### Controllers (`controller/`)

Controllers handle HTTP requests and responses:

```java
@Controller
public class DashboardController {
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Handle dashboard logic
        return "dashboard";
    }
}
```

### Models (`model/`)

Entity classes representing database tables:

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;
    private String role;
    // getters, setters, constructors
}
```

### Repositories (`repository/`)

Data access layer using Spring Data JPA:

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
```

### Services (`service/`)

Business logic layer:

```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
```

### Security (`security/`)

Security configuration and user details service:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
            .requestMatchers("/login", "/signup").permitAll()
            .requestMatchers("/recruiter/**").hasRole("RECRUITER")
            .requestMatchers("/candidate/**").hasRole("CANDIDATE")
            .anyRequest().authenticated()
        );
        return http.build();
    }
}
```

## 🗄️ Database Design

### Users Table

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);
```

### Quiz Table

```sql
CREATE TABLE quiz (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration_in_minutes INTEGER NOT NULL,
    recruiter_id BIGINT,
    FOREIGN KEY (recruiter_id) REFERENCES users(id)
);
```

### Question Table

```sql
CREATE TABLE question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_text TEXT NOT NULL,
    optiona VARCHAR(255) NOT NULL,
    optionb VARCHAR(255) NOT NULL,
    optionc VARCHAR(255) NOT NULL,
    optiond VARCHAR(255) NOT NULL,
    correct_option CHAR(1) NOT NULL,
    quiz_id BIGINT,
    FOREIGN KEY (quiz_id) REFERENCES quiz(id)
);
```

### Submission Table

```sql
CREATE TABLE submission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    candidate_id BIGINT,
    quiz_id BIGINT,
    score INTEGER NOT NULL,
    total_questions INTEGER NOT NULL,
    percentage DECIMAL(5,2) NOT NULL,
    submission_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (candidate_id) REFERENCES users(id),
    FOREIGN KEY (quiz_id) REFERENCES quiz(id)
);
```

## 🔒 Security Implementation

### Password Encryption

Passwords are encrypted using BCrypt:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

### Role-Based Access Control

Different endpoints are protected based on user roles:

```java
.requestMatchers("/recruiter/**").hasRole("RECRUITER")
.requestMatchers("/candidate/**").hasRole("CANDIDATE")
```

### CSRF Protection

Cross-Site Request Forgery protection is configured:

```java
http.csrf(csrf -> csrf.disable()); // Disabled for development
```

## 🚀 Deployment Options

### 1. Local Development

```bash
./gradlew bootRun
```

### 2. Production Build

```bash
./gradlew build
java -jar build/libs/quiz-platform-0.0.1-SNAPSHOT.jar
```

### 3. Docker Deployment

```bash
# Build Docker image
docker build -t quiz-platform .

# Run container
docker run -p 8081:8081 quiz-platform

# Or use Docker Compose
docker-compose up -d
```

### 4. Cloud Deployment

The application can be deployed to:

- **Heroku**: Using the provided Procfile
- **AWS Elastic Beanstalk**: Using the deployment package
- **Google Cloud Platform**: Using App Engine
- **Azure**: Using Azure App Service

## 🐛 Troubleshooting

### Common Issues

#### 1. Port Already in Use

```bash
# Check what's using port 8081
lsof -i :8081

# Kill the process
kill -9 <PID>
```

#### 2. Database Lock

```bash
# Remove database file and restart
rm quizdb.mv.db
./gradlew bootRun
```

#### 3. Build Failures

```bash
# Clean and rebuild
./gradlew clean build
```

#### 4. Java Version Issues

```bash
# Check Java version
java -version

# Should be Java 17 or higher
```

### Logs and Debugging

```bash
# View application logs
tail -f app.log

# Run with debug logging
./gradlew bootRun --debug
```

## 🤝 Contributing

### How to Contribute

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java coding conventions
- Add comments for complex logic
- Write unit tests for new features
- Update documentation when needed

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:

- Create an issue in the repository
- Check the troubleshooting section above
- Review the application logs

## 🔄 Version History

- **v1.0.0**: Initial release with basic quiz functionality
- **v1.1.0**: Added user authentication and role-based access
- **v1.2.0**: Enhanced UI with Bootstrap and responsive design
- **v1.3.0**: Improved error handling and validation

## 🎓 Learning Resources

### Spring Boot

- [Spring Boot Official Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)

### Frontend

- [Bootstrap Documentation](https://getbootstrap.com/docs/)
- [Thymeleaf Tutorial](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)

### Database

- [H2 Database Documentation](http://www.h2database.com/html/main.html)
- [JPA/Hibernate Guide](https://hibernate.org/orm/documentation/)

---

**Built with ❤️ using Spring Boot and Bootstrap**

This project demonstrates modern web development practices and can serve as a foundation for building similar applications.
