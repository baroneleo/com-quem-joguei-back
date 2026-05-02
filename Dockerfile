# Stage 1: build
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
COPY src ./src

# Gera o JAR bootável do Spring Boot (executa o repackage)
RUN mvn -B -DskipTests clean package spring-boot:repackage

# Stage 2: runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia explicitamente o jar bootável gerado
COPY --from=build /workspace/target/soccer-game-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
