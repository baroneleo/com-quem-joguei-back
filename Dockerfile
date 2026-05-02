# Use multi-stage build to produce a slimmer final image
# Stage 1: build (optional if you build outside the container)
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

# Stage 2: runtime
FROM eclipse-temurin:17-jre-jammy
ARG JAR_FILE=target/*.jar
WORKDIR /app
# Copy jar from build stage (if built in container) otherwise ensure target/*.jar exists before docker build
COPY --from=build /workspace/${JAR_FILE} app.jar
# If you prefer to build locally and only copy, comment the two lines above and use:
# COPY target/your-artifact-name.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
