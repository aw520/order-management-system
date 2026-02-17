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

COPY product-service/entrypoint.sh /entrypoint.sh

# Give the script execution permissions
RUN chmod +x /entrypoint.sh

EXPOSE 8080

# Tell the container to run the script instead of running 'java -jar' directly
ENTRYPOINT ["/entrypoint.sh"]