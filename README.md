# 🌦️ Weather Service - Zai Code Challenge

A Spring Boot-based HTTP service that reports real-time weather information for **Melbourne**, with automatic **failover** between weather providers and **caching** for performance and reliability.

---

## 📋 Requirements

- Java 17
- Maven
- Internet connection for accessing external weather APIs
- WeatherStack and OpenWeatherMap API keys

---

## 🔍 Features

- ✅ Fetches temperature and wind speed from **WeatherStack (primary)** or **OpenWeatherMap (failover)**
- ✅ Unified JSON response: `{"temperature_degrees": 24, "wind_speed": 15}`
- ✅ Caching (3 seconds) using **Caffeine**
- ✅ Resilient failover if primary provider is down
- ✅ Clean architecture following **SOLID** principles
- ✅ **Unit tested** with Mockito and JUnit 5
- ✅ Minimal UI to display weather info nicely

---

## 📦 Technologies Used

- Spring Boot 3
- Spring Web
- Caffeine Cache
- Jackson (for JSON parsing)
- JUnit 5 + Mockito
- Thymeleaf (for basic UI)

---

## 🏃 Running the Project Locally

### 1. Clone the repo

```bash
git clone https://github.com/your-username/weather-service.git
cd weather-service
