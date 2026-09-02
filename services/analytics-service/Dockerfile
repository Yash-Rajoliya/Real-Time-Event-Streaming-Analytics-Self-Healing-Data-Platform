# services/analytics-service/Dockerfile
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY common/ /app/common/
COPY services/analytics-service/pom.xml /app/services/analytics-service/
RUN mvn dependency:go-offline -B -f services/analytics-service/pom.xml

COPY services/analytics-service/src /app/services/analytics-service/src
RUN mvn package -DskipTests -f services/analytics-service/pom.xml

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser:appgroup

COPY --from=builder /app/services/analytics-service/target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", \
  "-XX:+UseG1GC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]