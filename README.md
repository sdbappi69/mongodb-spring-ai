# MongoDB + Spring AI Conversational Agent 🤖🍃

This project demonstrates how to build a **production-grade conversational AI agent** using **Spring Boot, Spring AI (MCP), and MongoDB**.

The agent answers natural-language questions by **querying MongoDB in real time**, maintains **multi-turn conversation context**, and strictly avoids hallucinating data.

---

## ✨ Key Capabilities

- 🧠 **Multi-turn Conversational Memory**
  - Maintains context across multiple user questions
  - Example:
    ```
    Q: How many orders are there for eworld?
    A: There are 2 orders.
    Q: What are their order IDs?
    A: The order IDs are ...
    ```

- 🗄️ **MongoDB-Grounded Answers**
  - Every response is backed by real MongoDB queries
  - No fabricated or inferred data

- 🔌 **Spring AI MCP Tools**
  - Vendor-based queries
  - Status-based filtering
  - Store-level lookups

- 📊 **Operational Insights**
  - Error analysis
  - Workflow state explanation
  - Ordered vs shipped comparison
  - Identification of failed or stuck records

- 🚀 **Modern Stack**
  - Java 21
  - Spring Boot 3
  - Spring AI 1.x

---

## 🧱 Tech Stack

| Layer | Technology |
|------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| AI Orchestration | Spring AI (MCP) |
| LLM | OpenAI (GPT-4o / Mini / Nano) |
| Database | MongoDB |
| Build Tool | Maven |
| API | REST (Spring WebMVC) |
