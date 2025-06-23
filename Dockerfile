# Use lightweight Java base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built jar file
COPY target/*.jar FLEXPAY-0.0.1-SNAPSHOT.jar

# Expose port (same as server.port in Spring Boot)
EXPOSE 8085

# Run the app
ENTRYPOINT ["java", "-jar", "FLEXPAY-0.0.1-SNAPSHOT.jar"]
