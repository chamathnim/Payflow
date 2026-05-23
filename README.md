# PayFlow — Digital Payment Processing Platform

![User Service CI](https://github.com/chamathnim/payflow/actions/workflows/user-service.yaml/badge.svg)
![Wallet Service CI](https://github.com/chamathnim/payflow/actions/workflows/wallet-service.yaml/badge.svg)
![Transaction Service CI](https://github.com/chamathnim/payflow/actions/workflows/transaction-service.yaml/badge.svg)
![Notification Service CI](https://github.com/chamathnim/payflow/actions/workflows/notification-service.yaml/badge.svg)
![Audit Service CI](https://github.com/chamathnim/payflow/actions/workflows/audit-service.yaml/badge.svg)
![API Gateway CI](https://github.com/chamathnim/payflow/actions/workflows/api-gateway.yaml/badge.svg)

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

# PayFlow — AWS Deployment Architecture

> The following describes the production deployment architecture for PayFlow on AWS.

---

## Network Flow

```
Internet
   ↓
Route 53 (DNS)
   ↓
Application Load Balancer
   ↓
EC2 Instance (API Gateway :8080)
   ↓
Private Subnet (VPC)
├── EC2 — User Service
├── EC2 — Wallet Service
├── EC2 — Transaction Service
├── EC2 — Notification Service
└── EC2 — Audit Service
        ↓                    ↓
RDS PostgreSQL          Amazon MSK
(per service DB)        (Kafka)
```

---

## AWS Services Used

| Service | Purpose |
|---|---|
| EC2 | Host each microservice in Docker containers |
| RDS PostgreSQL | Managed database per service |
| Amazon MSK | Managed Kafka for event streaming |
| Application Load Balancer | Distribute traffic to API Gateway |
| Route 53 | DNS management |
| VPC | Private network — services not publicly accessible |
| Security Groups | Firewall rules per service |
| ECR | Store Docker images |
| Secrets Manager | Store JWT secret, DB passwords |
| CloudWatch | Logs and monitoring |

---

## Security Architecture

```
Public Subnet          Private Subnet
─────────────          ──────────────
Load Balancer    →     API Gateway
                 →     Microservices (no public IP)
                 →     RDS (no public access)
                 →     MSK (no public access)
```

---

## Deployment Strategy

- Each service runs as a Docker container on EC2.
- Docker images are stored securely in AWS ECR.
- GitHub Actions builds and pushes images to ECR on every push.
- Zero-downtime deployments using rolling updates.
- Auto Scaling Groups handle scaling for EC2 instances based on traffic.
- Database credentials and API keys are stored in AWS Secrets Manager — never in the codebase.

---

## Environment Variables in Production

All sensitive configuration parameters are injected from AWS Secrets Manager:

| Variable | Description |
|---|---|
| `JWT_SECRET` | JWT signing key |
| `DB_PASSWORD` | Database passwords |
| `KAFKA_BOOTSTRAP_SERVERS` | MSK connection string |

---

## Local Development vs Production

| Config | Local | Production |
|---|---|---|
| Database | Docker PostgreSQL | AWS RDS |
| Kafka | Docker Kafka | AWS MSK |
| Secrets | `application.yml` | AWS Secrets Manager |
| Network | `localhost` | AWS VPC Private Subnet |
| Images | Local Docker | AWS ECR |
| Gateway | `localhost:8080` | Load Balancer DNS |

## Author

**Chamath** — Java & Spring Boot Developer
[GitHub](https://github.com/chamathnim)
