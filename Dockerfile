# Use OpenJDK 17 slim image as base
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY build/libs/quiz-platform-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8081
EXPOSE 8081

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 