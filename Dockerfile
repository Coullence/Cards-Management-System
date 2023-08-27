# Stage 1: Build stage
FROM openjdk:17-alpine AS builder

WORKDIR /app

# Copy only the gradle files needed for dependency resolution
COPY build.gradle .
COPY settings.gradle .
COPY gradle/ gradle/

# Copy the gradlew script and give it executable permissions
COPY gradlew .
RUN chmod +x gradlew

# Run the Gradle command to resolve and cache dependencies
RUN ./gradlew --no-daemon dependencies

# Copy the entire project source code
COPY . .

# Build the application
RUN ./gradlew --no-daemon build

# Stage 2: Final stage
FROM openjdk:17-alpine

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/build/libs/cards_m_service.jar .

# Set JVM memory options
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=uat

CMD ["java", "-jar", "cards_m_service.jar"]
