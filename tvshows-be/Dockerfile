# ---- Build stage (use JDK 17 to satisfy Gradle toolchain) ----
FROM gradle:8.7-jdk17 AS build
WORKDIR /app

# Copy everything (simplest + avoids missing settings.gradle/build.gradle)
COPY . .
RUN chmod +x gradlew || true

# Build the fat jar (skip tests for speed)
RUN ./gradlew --no-daemon clean bootJar -x test

# ---- Run stage (JRE 17) ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 5665
ENV SERVER_PORT=5665
ENTRYPOINT ["java","-jar","/app/app.jar"]
