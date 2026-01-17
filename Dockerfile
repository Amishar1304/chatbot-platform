# Dockerfile for Spring Boot app
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Build project using Maven wrapper
RUN ./mvnw clean package -DskipTests

# Run the JAR
ENTRYPOINT ["java","-jar","target/*.jar"]
