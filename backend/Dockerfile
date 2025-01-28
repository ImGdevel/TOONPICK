FROM eclipse-temurin:17-jdk as builder

WORKDIR /app

COPY . /app

RUN chmod +x ./gradlew

RUN ./gradlew clean bootJar

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

