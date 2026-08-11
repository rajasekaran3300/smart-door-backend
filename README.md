# Smart Door Security System — Backend

Spring Boot 3 / Java 21 REST API for the ESP32 + reed-switch door monitor.

## Run locally

1. Start MySQL (or use `docker-compose up mysql`).
2. Create schema: Flyway migrations run automatically on startup (`V1`–`V6` in `src/main/resources/db/migration`).
3. Set env vars (or edit `application.yml` defaults): `DB_HOST`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `CORS_ORIGINS`.
4. `./mvnw spring-boot:run`

API docs: `http://localhost:8080/swagger-ui.html`

## Run everything with Docker

```
docker-compose up --build
```

## Registering a door device

`door_device` rows aren't created via API in this MVP — insert one manually (or via a future admin endpoint) so the ESP32 has a `device_code` + `device_secret` to authenticate with:

```sql
INSERT INTO door_device (house_id, device_code, device_name, device_secret, is_active)
VALUES (1, 'ESP32-FRONT-001', 'Front Door', 'a-long-random-shared-secret', true);
```

## Key endpoints

| Method | Path | Auth | Purpose |
|---|---|---|---|
| POST | /api/auth/register | none | House name, username, password, confirm password |
| POST | /api/auth/login | none | Returns access + refresh JWT |
| POST | /api/auth/refresh | none | Rotate access token |
| POST | /api/auth/logout | JWT | Revokes refresh tokens |
| POST | /api/door/event | device secret | ESP32 posts OPEN/CLOSED |
| GET  | /api/door/status | JWT | Current status + last opened/closed + today's counts |
| GET  | /api/door/history?from=&to= | JWT | Date-range history |
| GET  | /api/door/history/today | JWT | Today's events |
| GET  | /api/notifications/unread | JWT | Unread notifications |
| PUT  | /api/notifications/{id}/read | JWT | Mark as read |
