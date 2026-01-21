# -------- Build stage --------
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy root and module POMs first (best caching)
COPY pom.xml .
COPY common/pom.xml common/pom.xml
COPY order-service/pom.xml order-service/pom.xml
COPY product-service/pom.xml product-service/pom.xml
COPY user-service/pom.xml user-service/pom.xml

# Download dependencies (cached layer)
RUN mvn -B -pl product-service -am dependency:go-offline

# Copy full source
COPY common ./common
COPY order-service ./order-service
COPY product-service ./product-service
COPY user-service ./user-service

# Build ONLY product-service
RUN mvn -pl product-service -am clean package -DskipTests


# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy jar from build stage
COPY --from=builder /app/product-service/target/*.jar app.jar

# Expose app port
EXPOSE 8080

# JVM options (optional but recommended)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
