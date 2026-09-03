# services/storage-connector/Dockerfile
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY common/ /app/common/
COPY services/storage-connector/pom.xml /app/services/storage-connector/
RUN mvn dependency:go-offline -B -f services/storage-connector/pom.xml

COPY services/storage-connector/src /app/services/storage-connector/src
RUN mvn package -DskipTests -f services/storage-connector/pom.xml

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser:appgroup

COPY --from=builder /app/services/storage-connector/target/*.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java", \
  "-XX:+UseG1GC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]