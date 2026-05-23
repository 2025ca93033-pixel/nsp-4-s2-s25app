# NSP-4-S2-S25App

> **DevOps Assignment 1** · BITS WILP  
> LLM-powered Q&A web app · React JS + Java Spring Boot + Hugging Face API

---

## Tech Stack

| Layer     | Technology                          |
|-----------|-------------------------------------|
| Frontend  | React JS 18, Axios                  |
| Backend   | Java 17, Spring Boot 3.2, Maven     |
| AI / LLM  | Hugging Face `google/flan-t5-large` |
| API Style | REST (JSON)                         |

---

## Project Structure

```
nsp-4-s2-s25app/
├── backend/                          # Spring Boot
│   ├── pom.xml
│   └── src/main/java/com/nsp/app/
│       ├── NspApplication.java       # Entry point
│       ├── config/CorsConfig.java    # CORS for React
│       ├── controller/
│       │   └── LLMController.java    # POST /api/ask, GET /api/health
│       ├── model/
│       │   ├── AskRequest.java
│       │   └── AskResponse.java
│       └── service/
│           └── LLMService.java       # Calls Hugging Face API
│
└── frontend/                         # React JS
    ├── package.json
    ├── public/index.html
    └── src/
        ├── App.js                    # Main chat page
        ├── App.css
        ├── index.js
        ├── services/api.js           # Axios calls to Spring Boot
        └── components/
            ├── ChatMessage.js/.css   # Chat bubble
            └── TypingIndicator.js/.css
```

---

## Prerequisites

- Java 17+
- Node.js 18+ & npm
- A free [Hugging Face account](https://huggingface.co/settings/tokens) (for API token)
We have to add token while running in local, as we can't keep token in github
-- VS Code or Intellij software is needed

---

## Setup & Run

### 1. Get a Hugging Face Token

1. Sign up at https://huggingface.co
2. Go to **Settings → Access Tokens → New Token** (read access is enough)
3. Copy the token (starts with `hf_...`)

---

### 2. Start the Backend (Spring Boot)

```bash
cd backend

# Set your HF token (Linux/macOS)
export HF_TOKEN=hf_your_token_here

# OR edit src/main/resources/application.properties directly:
# huggingface.api.token=hf_your_token_here

mvn spring-boot:run
```

Backend runs at: **http://localhost:8080**

Test it:
```bash
# Health check
curl http://localhost:8080/api/health

# Ask a question
curl -X POST http://localhost:8080/api/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "What is DevOps?"}'
```

---

### 3. Start the Frontend (React)

```bash
cd frontend
npm install
npm start
```

App opens at: **http://localhost:3000**

> The `"proxy": "http://localhost:8080"` in `package.json` forwards
> `/api/*` requests to Spring Boot automatically — no CORS issues in dev.

---

## API Reference

### `POST /api/ask`

**Request:**
```json
{
  "question": "What is a CI/CD pipeline?",
  "context": "Answer in simple terms for a beginner."
}
```

**Response:**
```json
{
  "question": "What is a CI/CD pipeline?",
  "answer": "A CI/CD pipeline is an automated process...",
  "model": "google/flan-t5-large",
  "timestamp": "2026-05-22T10:30:00",
  "responseTimeMs": 3420,
  "success": true,
  "errorMessage": null
}
```

### `GET /api/health`
```json
{
  "status": "UP",
  "app": "NSP-4-S2-S25App",
  "model": "google/flan-t5-large",
  "timestamp": "2026-05-22T10:30:00"
}
```

---

## Running Tests (Backend)

```bash
cd backend
mvn test
```

---

## Git Workflow (Assignment 1)

This repo uses the **Feature Branch Workflow**:

```
main
 ├── feature/llm-integration     ← Dev 1
 └── feature/input-validation    ← Dev 2
```

Branches merge to `main` only after a **Pull Request review**.  
See the Git strategy document for the full merge conflict demo.

---

## Troubleshooting

| Problem | Fix |
|---|---|
| `503` from LLM | Model is loading on HF free tier. Wait 20–30s and retry. |
| CORS error | Make sure Spring Boot is on port 8080 and the `proxy` in `package.json` matches. |
| `hf_YOUR_TOKEN_HERE` error | Replace the placeholder in `application.properties` with your real token. |
| Frontend blank page | Check browser console — likely a failed `/api/health` fetch if backend is down. |
