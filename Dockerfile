# ====== ÉTAPE 1 : BUILD ======
FROM gradle:8.14-jdk21 AS build
WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle gradle
RUN gradle dependencies --no-daemon || true

COPY src src
RUN gradle bootJar --no-daemon -x test

# ====== ÉTAPE 2 : RUNTIME ======
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
