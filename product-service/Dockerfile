# ---------- Build stage ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom first (for dependency caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests


# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy jar from build stage
COPY --from=builder /app/target/*.jar app.jar

# Expose app port
EXPOSE 8080

# JVM options (optional but recommended)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
