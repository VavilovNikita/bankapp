# BankApp

## 🔧 Запуск
- Установи PostgreSQL, Redis
- `./mvnw spring-boot:run`
- Swagger: `http://localhost:8080/swagger-ui.html`

## 🧱 Используемые технологии
- Spring Boot, Security, JPA
- Redis, Flyway, JWT
- Maven, Swagger, Lombok

## 💡 Логика
- Пользователь не может менять чужие данные
- Email/Phone — уникальны
- Баланс растёт на +10% до 207% раз в 30 сек
- Потокобезопасный трансфер с валидацией

## ✅ Тестирование
- Unit-тесты на трансферы
- Интеграционные тесты через Testcontainers
