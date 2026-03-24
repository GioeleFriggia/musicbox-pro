# MusicBox Pro

Web app musicale full stack pronta per VS Code.

## Stack
- Frontend: React + Vite
- Backend: Spring Boot + JWT + JPA
- Database: PostgreSQL
- Audio streaming: Jamendo API
- Metadata artwork/release fallback: Discogs API

## Requisiti
- Node.js 20+
- Java 21
- Maven 3.9+
- Docker Desktop oppure PostgreSQL locale

## 1) Crea le chiavi API

### Jamendo
1. Vai su Jamendo Developers.
2. Crea un'app e copia il `client_id`.

### Discogs
1. Vai su Discogs Developers.
2. Genera un personal access token.
3. Scegli uno User-Agent tipo `MusicBoxPro/1.0 +mailto:tuamail@example.com`.

## 2) Configura le variabili ambiente

### Backend
Copia `backend/.env.example` in `backend/.env` e imposta i valori.

### Frontend
Copia `frontend/.env.example` in `frontend/.env`.

## 3) Avvia PostgreSQL con Docker

Dalla root del progetto:

```bash
docker compose up -d
```

## 4) Avvia il backend

```bash
cd backend
mvn spring-boot:run
```

Su Windows PowerShell:

```powershell
cd backend
mvn spring-boot:run
```

Backend su `http://localhost:8080`

## 5) Avvia il frontend

In un nuovo terminale:

```bash
cd frontend
npm install
npm run dev
```

Frontend su `http://localhost:5173`

## Login demo
- Email: `admin@musicbox.local`
- Password: `Admin123!`

## Funzioni incluse
- registrazione e login JWT
- ricerca brani Jamendo
- dettagli con enrich Discogs
- player audio HTML5
- preferiti
- playlist utente
- dashboard libreria

## Note importanti
- Jamendo offre soprattutto musica indipendente e Creative Commons.
- Discogs viene usato come arricchimento metadata e copertine fallback.
- Se Discogs non trova match, l'app continua a funzionare usando i dati Jamendo.
