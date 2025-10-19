# ---- Stage 1: Build the application ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencies first (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the jar file
RUN mvn clean package -DskipTests

# ---- Stage 2: Run the application ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy built jar from the previous stage
COPY --from=build /app/target/expensetracker-0.0.1-SNAPSHOT.jar app.jar

# Set environment variables
ENV MONGO_URI=${MONGO_URI}
ENV JWT_SECRET=${JWT_SECRET}
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
