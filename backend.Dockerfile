# Use the official OpenJDK image as the base image
FROM openjdk:21-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Set the working directory in the container
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy the entire backend source code
COPY . .

# Build the application
RUN mvn clean install

# Expose the port that the backend will run on
EXPOSE 8080

# Copy the wait-for-db script
COPY wait-for-db.sh /app/wait-for-db.sh
RUN chmod +x /app/wait-for-db.sh

# Run the application after waiting for PostgreSQL
CMD ["/app/wait-for-db.sh"]
