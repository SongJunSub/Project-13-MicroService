FROM openjdk:18-slim AS builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN rm src/main/resources/bootstrap.yml
RUN mv src/main/resources/bootstrap-docker.yml src/main/resources/bootstrap.yml
RUN chmod +x ./gradlew
# Windows 전용 옵션
# RUN dos2unix ./gradlew
RUN ./gradlew bootJAR

FROM openjdk:18-slim
WORKDIR /app
COPY --from=builder build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]