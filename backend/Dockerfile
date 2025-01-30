# Use the official OpenJDK image as the base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven build files
# COPY pom.xml ./
COPY ../pom.xml ./
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Expose the application port
EXPOSE 8080

# Run the application
# CMD ["java", "-jar", "target/your-backend-application.jar"]
CMD ["java", "-jar", "target/full-stack-spring-boot.jar"]
