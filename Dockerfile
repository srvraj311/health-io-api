FROM openjdk:21-slim
# Expose the port that the application will run on
WORKDIR /ap
# Copy the packaged JAR file into the container
COPY target/health-io-api-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
# Command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "health-io-api-0.0.1-SNAPSHOT.jar"]
