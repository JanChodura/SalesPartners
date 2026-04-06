# SalesPartners

SalesPartners je Spring Boot aplikace pro správu obchodních partnerů a jejich kontaktů.

Projekt je postavený ve stylu OpenAPI-first. API kontrakt je definovaný v `src/main/resources/openapi.yaml`, při buildu se z něj generují Spring controllery a DTO a vlastní aplikační logika je implementovaná v adapterech, mapperech, service a repository vrstvě.

## Funkce

- správa obchodních partnerů
- správa kontaktů partnera
- validace vstupních dat i business pravidel
- kontrola duplicit partnerů podle ICO
- kontrola duplicit kontaktů podle e-mailu nebo telefonu v rámci jednoho partnera
- kontrola, že partner může mít pouze jeden primary kontakt
- Swagger UI pro ruční testování API
- jednoduchý živý HTML dashboard pro zobrazení partnerů a kontaktů

## Technologie

- Java 21
- Spring Boot 3.5
- Gradle
- OpenAPI Generator
- Springdoc Swagger UI
- JUnit 5
- in-memory persistence nad `HashMap`

## Struktura projektu

- `src/main/resources/openapi.yaml`
  OpenAPI kontrakt
- `src/main/java`
  aplikační logika, doménový model, service, repository, mapování a konfigurace
- `src/main/resources/static`
  jednoduchý živý dashboard
- `src/test/java`
  E2E a integrační testy

## Spuštění aplikace

### Požadavky

- JDK 21
- databáze není potřeba

### Spuštění z IDE

Spusť `SalesPartnersApp`.

### Spuštění z příkazové řádky

```powershell
.\gradlew.bat bootRun
```

Po startu jsou dostupné:

- aplikace a dashboard: `http://localhost:8080/`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- API: `http://localhost:8080/api`

## Build

```powershell
.\gradlew.bat clean build
```

OpenAPI zdrojové soubory se generují automaticky během buildu.

## Testy

```powershell
.\gradlew.bat test
```

Projekt obsahuje:

- E2E testy pro happy path scénáře API
- E2E testy pro nevalidní vstupy a chybové stavy
- integrační testy service vrstvy
- integrační testy doménových validací

## Profily

Aplikace používá dva profily:

- `app`
  výchozí runtime profil
- `test`
  profil používaný při testech

Připravená startovací data se načítají pouze pod profilem `app`.

## Přehled API

Hlavní endpointy:

- `POST /api/partners`
- `GET /api/partners`
- `GET /api/partners/{partnerId}`
- `PUT /api/partners/{partnerId}`
- `POST /api/partners/{partnerId}/contacts`
- `PUT /api/partners/{partnerId}/contacts/{contactId}`
- `DELETE /api/partners/{partnerId}/contacts/{contactId}`

Kompletní requesty a response jsou popsané ve Swagger UI nebo v `src/main/resources/openapi.yaml`.

## Validační pravidla

Příklady implementovaných pravidel:

- jméno partnera nesmí být prázdné
- partner musí obsahovat alespoň jeden identifikátor
- partner musí obsahovat alespoň jednu adresu
- ICO partnera musí být unikátní
- kontakt musí mít vyplněné `firstName` a `lastName`
- e-mail kontaktu musí mít při zadání validní formát
- telefon kontaktu musí při zadání odpovídat definovanému formátu
- partner může mít pouze jeden primary kontakt
- duplicitní kontakt je odmítnutý, pokud už u stejného partnera existuje stejný e-mail nebo telefon

## Ukázková data

Při spuštění aplikace pod profilem `app` se načtou připravená in-memory data z:

- `PreparedData`
- `LoadDatabase`

Tyto záznamy jsou záměrně odlišné od příkladů použitých v `openapi.yaml`.

## Poznámky

- persistence je aktuálně pouze in-memory
- po restartu aplikace se data ztratí
- dashboard je read-only a pravidelně se obnovuje z API
