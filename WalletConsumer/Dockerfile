# Используем базовый образ с Java
FROM openjdk:17

# Создаем директорию для приложения
WORKDIR /app

# Копируем JAR-файл вашего приложения в контейнер
COPY /target/WalletConsumer-0.0.1-SNAPSHOT.jar /app/WalletConsumer-0.0.1-SNAPSHOT.jar

# Команда для запуска приложения
CMD ["java", "-jar", "WalletConsumer-0.0.1-SNAPSHOT.jar"]

