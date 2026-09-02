# services/query-service/Dockerfile
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY common/ /app/common/
COPY services/query-service/pom.xml /app/services/query-service/
RUN mvn dependency:go-offline -B -f services/query-service/pom.xml

COPY services/query-service/src /app/services/query-service/src
RUN mvn package -DskipTests -f services/query-service/pom.xml

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser:appgroup

COPY --from=builder /app/services/query-service/target/*.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", \
  "-XX:+UseG1GC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]