
# Reports Controller
Aplikacja pozwalająca na obsługę zgłoszeń w sieci telekomunikacyjnej, rejestrację użytkowników (klientów, osób technicznych oraz administratorów). Przy jej pomocy klienci są wstanie zgłosić usterkę powstałą w sieci przy pomocy zgłoszenia. Następnie zgłoszenie te można przypisać do konkretnej osoby technicznej i obserwować proces postępów jej wykonania.

## Użyte technologie 

**Frontend:** TypeScript, React

**Backend:** Java, PostgreSQL, Docker


## Lokalne uruchomienie

Klonowanie projektu

```bash
   git clone https://github.com/kacperkuczminski/ReportsController
```

Przejdź do katalogu

```bash
   cd ReportsController
```

Zainstaluj zależności oraz uruchom część frontendową

```bash
   cd ui
   npm install
   npm run start
```

Przejdź do folderu z bazą danych oraz uruchom kontener docker
```bash
   cd ..\database\
   docker compose up
```

Przejdź do głównego katalogu i uruchom aplikację (backend)
```bash
   cd ..
   .\mvnw spring-boot:run 
```

## W planach

- Możliwość dodania zdjęcia do zgłoszenia
- Ciemny/jasny motyw
- Wysyłanie powiadomień o nowym zgłoszeniu do serwisanta
- Przepisanie forntend'u z JavaScript na TypeScript

## Autor

- [Kacper Kuczmiński](https://www.github.com/kacperkuczminski)

