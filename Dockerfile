FROM openjdk:21-slim
# Expose the port that the application will run on
WORKDIR /app
EXPOSE 8080
# Copy the packaged JAR file into the container
COPY target/health-io-api-0.0.1-SNAPSHOT.jar .
# Command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "health-io-api-0.0.1-SNAPSHOT.jar"]
