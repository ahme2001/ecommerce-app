# =========================
# Build stage
# =========================
FROM maven:3.9.16-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom and download dependencies first (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests


# =========================
# Run stage
# =========================
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]