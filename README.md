# Spring Cloud PhotoApp Lab

Learning / portfolio case study for classic Spring Cloud patterns:

- **Eureka** service discovery (`PhotoAppDiscoveryService` on `:8010`)
- **API Gateway** edge routing + JWT filter (`ApiGateway` on `:8082`)
- **Users** microservice with JWT login + H2 (`PhotoAppApiUsers`)
- **Account Management** status stub (`PhotoAppAccountManagement`)

> This is a **case study** for v1 — not a public production API. Prefer the portfolio landing page for interactive Eureka scale + Config refresh demos.

## Architecture

```text
Client / curl / Postman
        │
        ▼
 API Gateway (:8082)
   • lb://users-ws via Eureka
   • path rewrite /users-ws/** → /**
   • AuthorizationHeaderFilter on protected routes
        │
        ▼
 Eureka Discovery (:8010)
   ┌────────────┴────────────┐
   ▼                         ▼
 users-ws                 account-ws
 JWT + H2                 /account/status/check
```

### Horizontal scale (how the story works)

1. Start multiple `users-ws` instances (different ports / pods).
2. Each instance registers with Eureka as `users-ws`.
3. Gateway resolves `lb://users-ws` and load-balances requests across healthy instances.
4. New pods appear in the registry; subsequent requests can land on them.

### Spring Cloud Config (documented pattern)

The gateway imports `configserver:http://localhost:8012`. In a full Config Server setup:

1. Secrets / shared properties live in a central git-backed Config Server.
2. Services cache those properties at bootstrap.
3. Rotate a secret centrally, then `POST /actuator/refresh` (with `@RefreshScope`) so beans reload **without** a full redeploy.

v1 visualizes this on the portfolio case-study page even when Config Server is not running as a public demo.

## Run locally (Java 17)

Recommended order:

```bash
# 1) Discovery
cd PhotoAppDiscoveryService && ./mvnw spring-boot:run

# 2) Users + Account (separate terminals)
cd PhotoAppApiUsers && ./mvnw spring-boot:run
cd PhotoAppAccountManagement && ./mvnw spring-boot:run

# 3) Gateway
cd ApiGateway && ./mvnw spring-boot:run
```

Windows: use `mvnw.cmd` instead of `./mvnw`.

**Known lab caveats** (document honestly for interviewers):

- Gateway expects a Config Server import — remove or run Config Server if startup fails.
- Users security may pin a gateway IP — adjust `gateway.ip` for your machine.
- Account service is a stub; H2 is in-memory.

## API docs

See [docs/curl.md](docs/curl.md) and [docs/postman/PhotoApp-Lab.postman_collection.json](docs/postman/PhotoApp-Lab.postman_collection.json).

## Portfolio

Interactive case study: linked from [AshuBoi portfolio](https://github.com/AshuBoi/ashuboi-portfolio) → Projects → Spring Cloud PhotoApp Lab.
