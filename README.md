# TodoList

Jedna z moich pierwszych aplikacji w Spring Boocie: lista zadań z kontami użytkowników.
Każdy widzi tylko swoje zadania i może je dodawać, edytować, odhaczać oraz usuwać. Widoki są
renderowane po stronie serwera w Thymeleaf, a logowanie obsługuje Spring Security z sesjami
i hasłami w BCrypt.

## Uruchomienie

Wystarczy Java 17+. Maven przyjdzie z wrappera.

```bash
./mvnw spring-boot:run
```

Aplikacja wstaje na http://localhost:8080. Przy starcie tworzą się dwa konta demo:
`admin` / `admin` i `user` / `user`. Można też od razu założyć własne konto.

Dane trafiają do plikowej bazy H2 w katalogu `data/`, więc przetrwają restart. Podgląd bazy
jest pod `/h2-console` (JDBC URL `jdbc:h2:file:./data/todolist`, użytkownik `sa`, bez hasła).

## Trasy

| Metoda | Ścieżka | Co robi |
|--------|---------|---------|
| GET/POST | `/register` | rejestracja |
| GET | `/login` | logowanie |
| GET | `/tasks` | lista zadań zalogowanego użytkownika |
| GET | `/tasks/new` | formularz nowego zadania |
| POST | `/tasks/{id}` | zapis zadania |
| POST | `/tasks/{id}/toggle` | zmiana statusu |
| POST | `/tasks/{id}/delete` | usunięcie |

## Licencja

MIT
