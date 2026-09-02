# services/schema-registry/Dockerfile
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY common/ /app/common/
COPY services/schema-registry/pom.xml /app/services/schema-registry/
RUN mvn dependency:go-offline -B -f services/schema-registry/pom.xml

COPY services/schema-registry/src /app/services/schema-registry/src
RUN mvn package -DskipTests -f services/schema-registry/pom.xml

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser:appgroup

COPY --from=builder /app/services/schema-registry/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", \
  "-XX:+UseG1GC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]