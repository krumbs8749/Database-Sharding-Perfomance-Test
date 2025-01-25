# Use an OpenJDK 17 base image
FROM eclipse-temurin:17-jre

# Create app directory
WORKDIR /app

# Copy the built JAR into the container
COPY target/Sharding-1.0-SNAPSHOT-shaded.jar app.jar

# Default to listening on 8080 in the container
EXPOSE 8080

# Optionally set some memory or GC opts via ENV or CMD
# We'll override the port via command line or environment
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
