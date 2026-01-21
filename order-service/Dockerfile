# -------- Stage 1: Build --------
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy root and module POMs first (best caching)
COPY pom.xml .
COPY common/pom.xml common/pom.xml
COPY order-service/pom.xml order-service/pom.xml
COPY product-service/pom.xml product-service/pom.xml
COPY user-service/pom.xml user-service/pom.xml

# Download dependencies (cached layer)
RUN mvn -B -pl order-service -am dependency:go-offline

# Copy full source
COPY common ./common
COPY order-service ./order-service
COPY product-service ./product-service
COPY user-service ./user-service

# Build ONLY order-service
RUN mvn -pl order-service -am clean package -DskipTests


# -------- Stage 2: Runtime --------
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /app/order-service/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
