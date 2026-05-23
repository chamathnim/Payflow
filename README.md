# PayFlow — Digital Payment Processing Platform

A production-grade, cloud-native payment processing platform built with Java, Spring Boot Microservices, Kafka, Docker, and AWS.

> Built as a portfolio project to demonstrate real-world backend and DevOps engineering skills.

---

## Architecture Overview

```text
Client
 │
 ▼
API Gateway (Spring Cloud Gateway)
 │
 ├──▶ User Service        (Auth, JWT, Registration)
 ├──▶ Wallet Service      (Balances, Top-up)
 └──▶ Transaction Service (Transfers, Fraud Checks, Idempotency)
 │
 ▼ (Kafka Events)
 ├──▶ Notification Service (Email/SMS Alerts)
 └──▶ Audit Service        (Immutable Event Log)
```
---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3, Spring Cloud |
| API Gateway | Spring Cloud Gateway |
| Auth | Spring Security, JWT |
| Database | PostgreSQL (per service) |
| Messaging | Apache Kafka |
| Caching | Redis |
| Containerization | Docker, Docker Compose |
| CI/CD | GitHub Actions |
| Cloud | AWS (EC2, RDS, MSK) |
| Orchestration | Kubernetes (Minikube) |
| IaC | Terraform |
| Monitoring | Prometheus, Grafana |

---

## Microservices

| Service | Responsibility | Port |
|---|---|---|
| API Gateway | Route + auth filter all requests | 8080 |
| User Service | Register, login, JWT issuance | 8081 |
| Wallet Service | Manage balances, top-up | 8082 |
| Transaction Service | Transfers, idempotency, fraud | 8083 |
| Notification Service | Kafka consumer, alerts | 8084 |
| Audit Service | Immutable Kafka event log | 8085 |

---

## Project Roadmap

- [x] Phase 1 — Project setup and repository structure
- [x] Phase 2 — User Service (Registration, Login, JWT)
- [x] Phase 3 — Wallet Service (Balance, Top-up)
- [x] Phase 4 — Transaction Service (Transfers, Idempotency, Fraud)
- [x] Phase 5 — Kafka (Notification + Audit Services)
- [x] Phase 6 — API Gateway (Routing + Auth Filter)
- [x] Phase 7 — Docker Compose (Full local stack)
- [ ] Phase 8 — GitHub Actions CI/CD
- [ ] Phase 9 — AWS Deployment (EC2 + RDS)
- [ ] Phase 10 — Kubernetes + Terraform + Monitoring

---

## Getting Started (Local)

> Full setup instructions will be added as each phase is completed.

### Prerequisites
- Java 17+
- Docker + Docker Compose
- Maven

---

## Author

**Chamath** — Java & Spring Boot Developer
[GitHub](https://github.com/YOUR_USERNAME)
