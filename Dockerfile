FROM maven:3.9.5-eclipse-temurin-21 AS builder

# Set the working directory in the container
WORKDIR /app
COPY pom.xml ./
COPY src /app/src
COPY build-jar.sh ./
RUN chmod +x build-jar.sh && ./build-jar.sh  # Make script executable and run
FROM openjdk:21-slim
CMD ["echo", "Build complete"]
# Copy the packaged JAR file into the container
COPY target/*.jar .
# Expose the port that the application will run on
EXPOSE 8080
# Command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "health-io-api.jar"]
