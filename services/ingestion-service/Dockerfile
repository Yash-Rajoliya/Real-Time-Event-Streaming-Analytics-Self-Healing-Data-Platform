# services/ingestion-service/Dockerfile
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY common/ /app/common/
COPY services/ingestion-service/pom.xml /app/services/ingestion-service/
RUN mvn dependency:go-offline -B -f services/ingestion-service/pom.xml

COPY services/ingestion-service/src /app/services/ingestion-service/src
RUN mvn package -DskipTests -f services/ingestion-service/pom.xml

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser:appgroup

COPY --from=builder /app/services/ingestion-service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", \
  "-XX:+UseG1GC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]