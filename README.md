# Lista Repozytoriów na GitHubie

Ta aplikacja Spring Boot dostarcza interfejs API do wyświetlania repozytoriów na GitHubie użytkownika, które nie są forkiem, wraz z informacjami na temat ich gałęzi.

## Opis

Aplikacja pobiera dane repozytoriów z interfejsu API GitHuba i filtruje repozytoria, które są forkami. Dla każdego repozytorium, które nie jest forkiem, pobiera informacje o jego gałęziach, w tym nazwę i ostatni SHA ostatniego commita na każdej gałęzi.

## Wymagania

- Java 21
- Maven
- Spring Boot 3.2.2

## Użycie

Aby użyć tej aplikacji, wykonaj następujące kroki:

1. Sklonuj repozytorium na swój komputer:

    ```bash
    git clone https://github.com/yourusername/github-repository-listing.git
    ```

2. Przejdź do katalogu projektu:

    ```bash
    cd github-repository-listing
    ```

3. Zbuduj projekt przy użyciu Maven:

    ```bash
    mvn clean install
    ```

4. Uruchom aplikację:

    ```bash
    java -jar target/github-repository-listing-1.0.0.jar
    ```

5. Po uruchomieniu aplikacji możesz uzyskać dostęp do punktu końcowego API w celu wylistowania repozytoriów:

    ```
    GET /repositories/{username}
    ```

    Zastąp `{username}` nazwą użytkownika GitHuba, dla którego chcesz wylistować repozytoria.

## Odpowiedź API

Punkt końcowy API zwraca odpowiedź w formacie JSON zawierającą następujące informacje dla każdego repozytorium, które nie jest forkiem:

- Nazwa Repozytorium
- Nazwa Użytkownika
- Gałęzie (w tym nazwę i ostatnie SHA ostatniego commita na każdej gałęzi)

Jeśli określony użytkownik GitHuba nie istnieje, API zwraca odpowiedź 404 w następującym formacie JSON:

```json
{
    "status": 404,
    "message": "Użytkownik nie znaleziony"
}
