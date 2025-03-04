# ---- STAGE 1: BUILD ----
FROM maven:3.9.2-eclipse-temurin-17 AS build

WORKDIR /app

# Скопировать pom.xml и скачать зависимости для оффлайн-режима
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Скопировать исходный код
COPY src ./src

# Собрать проект и пропустить тесты
RUN mvn clean package -DskipTests

# Проверка содержимого директории target для отладки (можно удалить, если не нужно)
RUN ls -l /app/target

# ---- STAGE 2: RUNTIME ----
FROM eclipse-temurin:17-jre AS runtime

WORKDIR /app

# Копировать скомпилированный JAR-файл из стадии сборки
COPY --from=build /app/target/CoffeService-0.0.1-SNAPSHOT.jar app.jar

# Устанавливаем переменные окружения для PostgreSQL (вы можете изменить их, если нужно)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/app_user_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres

# Порт для вашего приложения
EXPOSE 8080

# Запуск приложения с учётом переменных окружения
ENTRYPOINT ["java", "-jar", "app.jar"]
