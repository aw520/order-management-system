# -------- Stage 1: Build --------
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom first (better layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests


# -------- Stage 2: Runtime --------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /app/target/*jar app.jar

EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
