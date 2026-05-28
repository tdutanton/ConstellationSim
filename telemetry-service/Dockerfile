# Build stage
FROM gradle:8.14.1-jdk21 AS builder
WORKDIR /app

# Кэши зависимостей
COPY build.gradle.kts settings.gradle.kts ./
COPY --chown=gradle:gradle . .
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
RUN gradle dependencies --refresh-dependencies && \
    gradle build -x test --no-daemon

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Создание непривилегированного пользователя
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Копирование только JAR из builder
COPY --from=builder --chown=appuser:appgroup /app/build/libs/*.jar app.jar

# Открытие порт
EXPOSE 9091

# Запуск от непривилегированного пользователя
USER appuser
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]