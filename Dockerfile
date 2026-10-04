# Stage 1: Build the JAR using Maven

FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace


COPY pom.xml .
RUN mvn dependency:go-offline -B


COPY src ./src
RUN mvn clean package -DskipTests -B


# Stage 2: Runtime image

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app


RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring


COPY --from=build /workspace/target/*.jar app.jar


EXPOSE 8080


ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]