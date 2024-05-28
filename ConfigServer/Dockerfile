FROM openjdk:18-slim AS builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
#RUN dos2unix ./gradlew
RUN ./gradlew bootJAR

FROM openjdk:18-slim
WORKDIR /app
COPY --from=builder build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]