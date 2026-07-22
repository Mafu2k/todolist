# TodoList

Aplikacja webowa do zarządzania zadaniami z rejestracją i logowaniem użytkowników.
Każdy zalogowany użytkownik ma własną, prywatną listę zadań. Backend zbudowany
w Spring Boot z uwierzytelnianiem opartym o Spring Security, widoki renderowane
po stronie serwera w Thymeleaf.

## Funkcjonalności

- Rejestracja i logowanie użytkownika (Spring Security, sesje)
- Zadania przypisane do konkretnego użytkownika — każdy widzi tylko swoje
- Dodawanie, edycja i usuwanie zadań
- Oznaczanie zadania jako wykonane / do zrobienia (toggle)
- Walidacja formularzy
- Widoki serwerowe (Thymeleaf) z integracją zabezpieczeń

## Stack

- Java + Spring Boot
- Spring Security (uwierzytelnianie i autoryzacja)
- Spring Data JPA + baza H2 (in-memory)
- Thymeleaf (+ `thymeleaf-extras-springsecurity6`)
- Bean Validation
- Maven

## Struktura projektu

```
src/main/java/org/example/todolist/
├── TodolistApplication.java
├── SecurityConfig.java             # konfiguracja Spring Security
├── AuthController.java             # rejestracja / logowanie
├── TaskController.java             # CRUD zadań
├── Task.java, User.java            # encje JPA
├── TaskRepository.java, UserRepository.java
├── UserService.java                # rejestracja i logika użytkowników
└── CustomUserDetailsService.java   # ładowanie użytkownika dla Security
```

## Główne trasy

| Metoda | Ścieżka             | Opis                          |
|--------|---------------------|-------------------------------|
| GET    | `/login`            | formularz logowania           |
| GET/POST | `/register`       | rejestracja użytkownika       |
| GET    | `/tasks`            | lista zadań użytkownika       |
| GET    | `/tasks/new`        | formularz nowego zadania      |
| POST   | `/tasks/{id}`       | zapis / aktualizacja zadania  |
| POST   | `/tasks/{id}/toggle`| zmiana statusu wykonania      |
| POST   | `/tasks/{id}/delete`| usunięcie zadania             |

## Uruchomienie

Wymagania: Java 17+ oraz Maven (lub dołączony `mvnw`).

```bash
./mvnw spring-boot:run
```

Aplikacja dostępna pod `http://localhost:8080` — zacznij od rejestracji konta.

## Autor

Łukasz Janicki

## Licencja

MIT — szczegóły w pliku [LICENSE](LICENSE).
