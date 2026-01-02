# MongoDB + Spring AI Conversational Agent 🤖🍃

This repository demonstrates how to build a **production-grade conversational AI backend** using **Spring Boot, Spring AI (MCP), and MongoDB**.

The system enables users to ask **natural-language questions over MongoDB data**, receive **accurate, real-time answers**, and continue conversations across multiple turns — while **strictly preventing hallucinations**.

---

## 🎯 Use Case

Operational and transactional data stored in MongoDB is often difficult to query without:
- Understanding schemas
- Writing complex queries
- Navigating dashboards or logs

This project solves that by providing a **conversational API** that:
- Accepts plain-English questions
- Translates intent into MongoDB-backed queries
- Maintains conversational context
- Returns only verified data-driven answers

---

## 💬 Example Conversation
    ```
    Q: How many orders are there for eworld?
    A: There are 2 orders.
    Q: What are their order IDs?
    A: The order IDs are ...
    ```

---

## 🧠 AI Architecture & Patterns

### 1️⃣ Retrieval-Augmented Generation (RAG)
- The LLM never fabricates data
- All facts come from MongoDB query results
- The LLM is used only for:
  - Intent understanding
  - Tool selection
  - Response reasoning and formatting

### 2️⃣ Tool-Augmented Reasoning (Spring AI MCP)
- MongoDB access is exposed through domain-specific tools
- The LLM decides:
  - When to call a tool
  - Which parameters to pass
- Tool outputs are injected back into the conversation

### 3️⃣ Conversational Memory Pattern
- Each conversation is identified by a `conversationId`
- User and assistant messages are stored in MongoDB
- Full message history is replayed on each request
- Memory is bounded to prevent token overflow

### 4️⃣ Grounded Response Enforcement
- System prompt forbids hallucinations
- If data is missing, the agent explicitly states it
- Ensures accuracy, auditability, and trust

### 5️⃣ Stateless API, Stateful Intelligence
- REST APIs remain stateless
- Conversation state is persisted in MongoDB
- Enables horizontal scaling and resilience

---

## ✨ Key Features

- 🧠 Multi-turn conversational context
- 🗄️ MongoDB-grounded answers
- 🔌 Spring AI MCP tool integration
- 📊 Operational insights and diagnostics
- 🚀 Production-ready architecture

---

## 🧱 Tech Stack

| Layer | Technology |
|------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| AI Orchestration | Spring AI (MCP) |
| LLM | OpenAI (GPT-4o / Mini / Nano) |
| Database | MongoDB |
| API | REST (Spring WebMVC) |
| Build Tool | Maven |

---

## 🔗 API Usage

### Chat Endpoint

**POST** `/agent/chat`

#### Request

```json
{
  "conversationId": "user-123",
  "message": "How many orders do we have for eworld?"
}
```

#### Response
```json
{
  "answer": "There are 2 orders for eworld."
}
```
