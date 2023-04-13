
# Reports Controller
Aplikacja pozwalająca na obsługę zgłoszeń w sieci telekomunikacyjnej, rejestrację użytkowników (klientów, osób technicznych oraz administratorów). Przy jej pomocy klienci są wstanie zgłosić usterkę powstałą w sieci przy pomocy zgłoszenia. Następnie zgłoszenie te można przypisać do konkretnej osoby technicznej i obserwować proces postępów jej wykonania.

## Użyte technologie 

**Frontend:** JavaScript, React

**Backend:** Java, PostgreSQL, Docker


## Lokalne uruchomienie

Clone the project

```bash
   git clone https://github.com/kacperkuczminski/ReportsController
```

Go to the project directory 

```bash
   cd ReportsController
```

Install dependencies and start the frontend

```bash
   cd ui
   npm install
   npm run start
```

Go to database directory and run docker container
```bash
   cd ..\database\
   docker compose up
```

Go to main directory and start backend
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

