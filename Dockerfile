FROM openjdk:21-slim
# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/health-io-api-0.0.1-SNAPSHOT.jar /app/health-io-api.jar

# Expose the port that the application will run on
EXPOSE 8080

RUN mvn clean package

# Command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "health-io-api.jar"]
