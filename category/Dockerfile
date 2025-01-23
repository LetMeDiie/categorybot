# Шаг 1: Сборка приложения
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /app/category

COPY pom.xml mvnw ./
RUN mvn dependency:go-offline

COPY ./src ./src
RUN mvn clean package -DskipTests

# Шаг 2: Финальный контейнер
FROM eclipse-temurin:17
WORKDIR /app/category
EXPOSE 8080

COPY --from=builder /app/category/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
