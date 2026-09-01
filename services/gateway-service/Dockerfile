# services/gateway-service/Dockerfile

# Stage 1: Build Application
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Cache Maven dependencies
COPY pom.xml .
COPY common/ /app/common/
COPY services/gateway-service/pom.xml /app/services/gateway-service/
RUN mvn dependency:go-offline -B -f services/gateway-service/pom.xml

# Build the Gateway binary
COPY services/gateway-service/src /app/services/gateway-service/src
RUN mvn package -DskipTests -f services/gateway-service/pom.xml

# Stage 2: Runtime Lightweight Container
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Unprivileged non-root service execution
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser:appgroup

# Copy compiled artifact from builder stage
COPY --from=builder /app/services/gateway-service/target/*.jar app.jar

EXPOSE 8080 8081

# JVM memory limits tuned for containerized reactive runtimes
ENTRYPOINT ["java", \
  "-XX:+UseG1GC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]