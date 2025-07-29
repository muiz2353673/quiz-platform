#!/bin/bash

# Quiz Platform Deployment Script
# This script builds and deploys the Quiz Platform application

set -e  # Exit on any error

echo "🚀 Starting Quiz Platform Deployment..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Java is installed
check_java() {
    print_status "Checking Java installation..."
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed. Please install Java 17 or higher."
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        print_error "Java 17 or higher is required. Current version: $JAVA_VERSION"
        exit 1
    fi
    
    print_success "Java version $(java -version 2>&1 | head -n 1) is installed"
}

# Check if Gradle is available
check_gradle() {
    print_status "Checking Gradle availability..."
    if [ ! -f "./gradlew" ]; then
        print_error "Gradle wrapper not found. Please ensure you're in the project root directory."
        exit 1
    fi
    
    print_success "Gradle wrapper found"
}

# Clean previous builds
clean_build() {
    print_status "Cleaning previous builds..."
    ./gradlew clean
    print_success "Build cleaned successfully"
}

# Build the application
build_app() {
    print_status "Building Quiz Platform application..."
    ./gradlew build -x test
    print_success "Application built successfully"
}

# Run tests (optional)
run_tests() {
    if [ "$1" = "--with-tests" ]; then
        print_status "Running tests..."
        ./gradlew test
        print_success "Tests completed successfully"
    else
        print_warning "Skipping tests. Use --with-tests to run tests"
    fi
}

# Start the application
start_app() {
    print_status "Starting Quiz Platform application..."
    
    # Check if port 8081 is already in use
    if lsof -Pi :8081 -sTCP:LISTEN -t >/dev/null ; then
        print_warning "Port 8081 is already in use. Stopping existing process..."
        lsof -ti:8081 | xargs kill -9
        sleep 2
    fi
    
    # Start the application in background
    nohup ./gradlew bootRun > app.log 2>&1 &
    APP_PID=$!
    
    # Wait for application to start
    print_status "Waiting for application to start..."
    for i in {1..30}; do
        if curl -s http://localhost:8081/login > /dev/null 2>&1; then
            print_success "Application started successfully!"
            print_success "Application URL: http://localhost:8081"
            print_success "H2 Console: http://localhost:8081/h2-console"
            print_success "Application PID: $APP_PID"
            print_success "Log file: app.log"
            return 0
        fi
        sleep 2
    done
    
    print_error "Application failed to start within 60 seconds"
    print_error "Check app.log for details"
    exit 1
}

# Docker deployment
deploy_docker() {
    print_status "Building Docker image..."
    docker build -t quiz-platform .
    print_success "Docker image built successfully"
    
    print_status "Starting Quiz Platform with Docker Compose..."
    docker-compose up -d
    print_success "Docker deployment completed"
    print_success "Application URL: http://localhost:8081"
}

# Stop the application
stop_app() {
    print_status "Stopping Quiz Platform application..."
    
    # Try to find and kill the application process
    if lsof -ti:8081 > /dev/null 2>&1; then
        lsof -ti:8081 | xargs kill -9
        print_success "Application stopped successfully"
    else
        print_warning "No application found running on port 8081"
    fi
}

# Show application status
show_status() {
    print_status "Checking application status..."
    
    if lsof -Pi :8081 -sTCP:LISTEN -t >/dev/null ; then
        print_success "Application is running on port 8081"
        print_success "Application URL: http://localhost:8081"
    else
        print_warning "Application is not running"
    fi
}

# Show help
show_help() {
    echo "Quiz Platform Deployment Script"
    echo ""
    echo "Usage: $0 [COMMAND]"
    echo ""
    echo "Commands:"
    echo "  build           Build the application"
    echo "  start           Build and start the application"
    echo "  stop            Stop the running application"
    echo "  restart         Restart the application"
    echo "  status          Show application status"
    echo "  docker          Deploy using Docker"
    echo "  clean           Clean build artifacts"
    echo "  --with-tests    Run tests during build"
    echo "  help            Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 start              # Build and start the application"
    echo "  $0 start --with-tests # Build with tests and start"
    echo "  $0 docker             # Deploy using Docker"
    echo "  $0 status             # Check if application is running"
}

# Main script logic
case "$1" in
    "build")
        check_java
        check_gradle
        clean_build
        build_app
        run_tests "$2"
        ;;
    "start")
        check_java
        check_gradle
        clean_build
        build_app
        run_tests "$2"
        start_app
        ;;
    "stop")
        stop_app
        ;;
    "restart")
        stop_app
        sleep 2
        check_java
        check_gradle
        start_app
        ;;
    "status")
        show_status
        ;;
    "docker")
        deploy_docker
        ;;
    "clean")
        clean_build
        ;;
    "help"|"--help"|"-h")
        show_help
        ;;
    *)
        print_error "Unknown command: $1"
        echo ""
        show_help
        exit 1
        ;;
esac

print_success "Deployment script completed!" 