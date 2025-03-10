FROM maven:3.9.2-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

RUN ls -l /app/target


FROM eclipse-temurin:17-jre AS runtime

WORKDIR /app

COPY --from=build /app/target/CoffeService-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/app_user_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
