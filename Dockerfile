# Use Java 17
FROM eclipse-temurin:17-jdk

# Set working directory inside the container
WORKDIR /app

# Copy project files
COPY . .

# Make mvnw executable
RUN chmod +x mvnw

# Build Spring Boot JAR (skip tests to speed up deploy)
RUN ./mvnw clean package -DskipTests

# Run the JAR file
CMD ["java", "-jar", "target/system2-corebank-0.0.1-SNAPSHOT.jar"]
