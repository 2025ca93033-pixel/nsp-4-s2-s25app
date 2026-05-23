# NSP-4-S2-S25App — AI Q&A Assistant

A full-stack AI-powered Q&A application built with React and Spring Boot, integrated with Hugging Face LLM API.

---

## Assignment Details

- **Course:** BITS WILP — DevOps
- **Topic:** Feature Branch Workflow Implementation
- **Goal:** Simulate 2 developers working on separate feature branches, create a merge conflict, resolve it, and merge to main only after Pull Request review.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React 18, Axios |
| Backend | Java 17, Spring Boot 3.2.5, Maven |
| AI API | Hugging Face — google/flan-t5-large |
| Version Control | Git + GitHub |
| IDE | Spring Tool Suite 4 (STS/Eclipse) |

---

## Project Structure

```
nsp-4-s2-s25app/
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/nsp/app/
│       ├── NspApplication.java
│       ├── config/CorsConfig.java
│       ├── controller/LLMController.java
│       ├── model/
│       │   ├── AskRequest.java
│       │   └── AskResponse.java
│       └── service/LLMService.java
└── frontend/
    ├── package.json
    └── src/
        ├── App.js
        ├── App.css
        ├── services/api.js
        └── components/
            ├── ChatMessage.js
            ├── ChatMessage.css
            ├── TypingIndicator.js
            └── TypingIndicator.css
```

---

## Getting Started

### Prerequisites
- Java 17
- Maven
- Node.js 18+
- npm
- Git

### 1. Clone the Repository
```bash
git clone https://github.com/YOUR-USERNAME/nsp-4-s2-s25app.git
cd nsp-4-s2-s25app
```

### 2. Configure Hugging Face Token
Open `backend/src/main/resources/application.properties`:
```properties
huggingface.api.token=YOUR_HF_TOKEN_HERE
huggingface.api.model=google/flan-t5-large
```
Get your free token from: https://huggingface.co/settings/tokens

### 3. Start the Backend
```bash
cd backend
./mvnw spring-boot:run
```
Backend starts on: http://localhost:8080

### 4. Start the Frontend
```bash
cd frontend
npm install
npm start
```
Frontend starts on: http://localhost:3000

---

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | /api/health | Check backend status |
| POST | /api/ask | Send question, get AI answer |

### Sample Request
```json
POST /api/ask
{
  "question": "What is DevOps?",
  "context": ""
}
```

### Sample Response
```json
{
  "question": "What is DevOps?",
  "answer": "DevOps is a combination of development and operations practices...",
  "model": "google/flan-t5-large",
  "responseTimeMs": 1500,
  "success": true,
  "timestamp": "2026-05-23T10:00:00"
}
```

---

## Feature Branch Workflow

This project follows a Feature Branch Workflow as part of the DevOps assignment.

### Branch Structure
```
main
├── feature/quick-prompts-ui        (Developer 1)
├── feature/response-time-display   (Developer 2)
└── feature/backend-mock-llm-service
```

### Developer 1 — feature/quick-prompts-ui
- Added Kubernetes and Agile methodology quick-prompt buttons
- Updated QUICK_PROMPTS array in App.js

### Developer 2 — feature/response-time-display
- Enhanced response time display with model name
- Updated ChatMessage.js and App.css

### Merge Conflict
Both developers edited the same line in `App.css` (header background property). Conflict was resolved manually by combining both changes and committed as a merge commit.

### Pull Request Process
- All features merged to main only via Pull Request
- PR reviewed before merge
- Branch protection rules applied on main

---

## Git Commands Used

```bash
# Create feature branch
git checkout -b feature/branch-name

# Stage and commit
git add .
git commit -m "feat: description"

# Push branch
git push origin feature/branch-name

# View branch graph
git log --oneline --graph --all

# Merge and resolve conflict
git merge feature/branch-name
git add .
git commit -m "merge: resolve conflict"
```

---

## Issues Fixed During Development

| Issue | Cause | Fix |
|---|---|---|
| 400 Bad Request | api.js missing | Created api.js |
| Calls on wrong port | No proxy config | Added proxy to package.json |
| question field null | Lombok not working in STS | Manual getters/setters |
| .builder() error | Lombok @Builder not working | Manual Builder class |
| ConnectException | DNS resolution failure | Mock LLM responses |

---

## Screenshots

> Add screenshots of your running application here.

---

## Author

- **Name:** Aaryan Katore , Rahul 
- **Course:** BITS WILP DevOps
- **Date:** May 2026
