# Используем базовый образ с Java
FROM openjdk:17

# Создаем директорию для приложения
WORKDIR /app

# Копируем JAR-файл вашего приложения в контейнер
COPY /target/TestApplication-0.0.1-SNAPSHOT.jar /app/TestApplication-0.0.1-SNAPSHOT.jar

# Команда для запуска приложения
CMD ["sh", "-c", "sleep 30 && java -jar TestApplication-0.0.1-SNAPSHOT.jar"]

