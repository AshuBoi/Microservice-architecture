# PhotoApp Lab — curl cookbook

Base URL assumes the **API Gateway** on `http://localhost:8082`.

## Register

```bash
curl -X POST http://localhost:8082/users-ws/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Ada",
    "lastName": "Lovelace",
    "email": "ada@example.com",
    "password": "Secret123!",
    "repeatPassword": "Secret123!"
  }'
```

## Login

```bash
curl -i -X POST http://localhost:8082/users-ws/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "ada@example.com",
    "password": "Secret123!"
  }'
```

Copy the `Authorization` / Bearer token from the response headers or body (depending on filter configuration) for protected calls.

## Account status (stub)

```bash
curl http://localhost:8082/account-ws/account/status/check
```

## Eureka dashboard

Open [http://localhost:8010](http://localhost:8010) to see registered instances after horizontal scale-up.
