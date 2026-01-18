# JWT Bearer Token Authentication - Testing Guide

## Overview
The REST API (`/api/**`) now uses JWT Bearer Token authentication for admins only.
The web UI (Thymeleaf) continues to use session-based authentication.

---

## Authentication Flow

### 1. Login (Get JWT Token)
**Endpoint:** `POST /api/auth/login`

**Request:**
```bash
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password"
  }'
```

**Successful Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTczNDU0NzIwMCwiZXhwIjoxNzM0NjMzNjAwfQ.signature...",
  "username": "admin",
  "role": "ADMIN",
  "expiresIn": 86400,
  "tokenType": "Bearer"
}
```

**Error Response (400 Bad Request - Invalid Credentials):**
```json
{
  "error": "Invalid credentials or user is not an admin"
}
```

**Error Response (403 Forbidden - Non-Admin User):**
```json
{
  "error": "Only administrators can access the REST API"
}
```

---

### 2. Use Token to Access Protected Resources
**Copy the token from login response and use in Authorization header:**

```bash
curl -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  http://localhost:9090/api/admin/etudiants
```

---

## API Endpoints (Admin Only)

### Etudiant Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/etudiants` | Get all students |
| GET | `/api/admin/etudiants/{id}` | Get student by ID |
| GET | `/api/admin/etudiants/matricule/{matricule}` | Get student by matricule |
| GET | `/api/admin/etudiants/search?keyword=xxx` | Search students |
| GET | `/api/admin/etudiants/{id}/moyenne` | Get student average |
| POST | `/api/admin/etudiants` | Create new student |
| PUT | `/api/admin/etudiants/{id}` | Update student |
| DELETE | `/api/admin/etudiants/{id}` | Delete student |

### Formateur Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/formateurs` | Get all teachers |
| GET | `/api/admin/formateurs/{id}` | Get teacher by ID |
| GET | `/api/admin/formateurs/search?keyword=xxx` | Search teachers |
| GET | `/api/admin/formateurs/specialite/{specialite}` | Get teachers by speciality |
| POST | `/api/admin/formateurs` | Create new teacher |
| PUT | `/api/admin/formateurs/{id}` | Update teacher |
| DELETE | `/api/admin/formateurs/{id}` | Delete teacher |

### Cours Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/cours` | Get all courses |
| GET | `/api/admin/cours/{id}` | Get course by ID |
| GET | `/api/admin/cours/code/{code}` | Get course by code |
| GET | `/api/admin/cours/search?keyword=xxx` | Search courses |
| POST | `/api/admin/cours` | Create new course |
| PUT | `/api/admin/cours/{id}` | Update course |
| DELETE | `/api/admin/cours/{id}` | Delete course |
| POST | `/api/admin/cours/{coursId}/formateur/{formateurId}` | Assign teacher |
| POST | `/api/admin/cours/{coursId}/groupe/{groupeId}` | Assign group |
| DELETE | `/api/admin/cours/{coursId}/groupe/{groupeId}` | Remove group |
| GET | `/api/admin/cours/{id}/stats` | Get course statistics |

### Inscription Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/inscriptions` | Get all registrations |
| GET | `/api/admin/inscriptions/{id}` | Get registration by ID |
| GET | `/api/admin/inscriptions/etudiant/{etudiantId}` | Get registrations by student |
| GET | `/api/admin/inscriptions/cours/{coursId}` | Get registrations by course |
| POST | `/api/admin/inscriptions` | Create registration |
| PUT | `/api/admin/inscriptions/{id}` | Update registration |
| PUT | `/api/admin/inscriptions/{id}/confirmer` | Confirm registration |
| PUT | `/api/admin/inscriptions/{id}/annuler` | Cancel registration |
| DELETE | `/api/admin/inscriptions/{id}` | Delete registration |

### Note Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/notes` | Get all grades |
| GET | `/api/admin/notes/{id}` | Get grade by ID |
| GET | `/api/admin/notes/etudiant/{etudiantId}` | Get grades by student |
| GET | `/api/admin/notes/cours/{coursId}` | Get grades by course |
| POST | `/api/admin/notes` | Create grade |
| PUT | `/api/admin/notes/{id}` | Update grade |
| DELETE | `/api/admin/notes/{id}` | Delete grade |
| GET | `/api/admin/notes/etudiant/{etudiantId}/moyenne` | Get student average |
| GET | `/api/admin/notes/cours/{coursId}/stats` | Get course grade statistics |

### Seance Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/seances` | Get all sessions |
| GET | `/api/admin/seances/{id}` | Get session by ID |
| GET | `/api/admin/seances/cours/{coursId}` | Get sessions by course |
| POST | `/api/admin/seances` | Create session |
| PUT | `/api/admin/seances/{id}` | Update session |
| DELETE | `/api/admin/seances/{id}` | Delete session |

### Authentication & Validation
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Admin login (get JWT token) |
| POST | `/api/auth/validate` | Validate JWT token |

---

## Testing Examples

### Using cURL

**1. Login and get token:**
```bash
TOKEN=$(curl -s -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}' | jq -r '.token')
```

**2. Use token to get all students:**
```bash
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:9090/api/admin/etudiants
```

**3. Create a new student:**
```bash
curl -X POST http://localhost:9090/api/admin/etudiants \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean.dupont@example.com",
    "groupe": {"id": 1}
  }'
```

---

### Using Postman

1. **Create a new request**
   - Method: POST
   - URL: `http://localhost:9090/api/auth/login`
   - Body (JSON):
   ```json
   {
     "username": "admin",
     "password": "password"
   }
   ```
   - Send → Copy the `token` value

2. **Access protected endpoint**
   - Method: GET
   - URL: `http://localhost:9090/api/admin/etudiants`
   - Authorization tab:
     - Type: Bearer Token
     - Token: Paste your token
   - Send

---

### Using VS Code REST Client Extension

Create `api-test.http`:

```http
@baseUrl = http://localhost:9090
@username = admin
@password = password
@token = 

### Login - Get JWT Token
POST {{baseUrl}}/api/auth/login
Content-Type: application/json

{
  "username": "{{username}}",
  "password": "{{password}}"
}

### Get all students (Requires JWT Token)
GET {{baseUrl}}/api/admin/etudiants
Authorization: Bearer {{token}}

### Get student by ID
GET {{baseUrl}}/api/admin/etudiants/1
Authorization: Bearer {{token}}

### Search students
GET {{baseUrl}}/api/admin/etudiants/search?keyword=dupont
Authorization: Bearer {{token}}

### Create new student
POST {{baseUrl}}/api/admin/etudiants
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean.dupont@example.com",
  "groupe": {
    "id": 1
  }
}

### Update student
PUT {{baseUrl}}/api/admin/etudiants/1
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "nom": "Dupont Updated",
  "prenom": "Jean",
  "email": "jean.updated@example.com"
}

### Delete student
DELETE {{baseUrl}}/api/admin/etudiants/1
Authorization: Bearer {{token}}

### Get all teachers
GET {{baseUrl}}/api/admin/formateurs
Authorization: Bearer {{token}}

### Get all courses
GET {{baseUrl}}/api/admin/cours
Authorization: Bearer {{token}}

### Assign teacher to course
POST {{baseUrl}}/api/admin/cours/1/formateur/1
Authorization: Bearer {{token}}

### Get course statistics
GET {{baseUrl}}/api/admin/cours/1/stats
Authorization: Bearer {{token}}

### Test without token (should fail)
GET {{baseUrl}}/api/admin/etudiants

### Test with invalid token (should fail)
GET {{baseUrl}}/api/admin/etudiants
Authorization: Bearer invalid.token.here
```

---

## Error Responses

| Status | Error | Reason |
|--------|-------|--------|
| 200 OK | (token) | Successful login |
| 400 Bad Request | Invalid credentials | Wrong username/password |
| 401 Unauthorized | Unauthorized | Missing or invalid JWT token |
| 403 Forbidden | Only administrators can access | Non-admin user tried to login |
| 404 Not Found | Resource not found | ID doesn't exist |
| 405 Method Not Allowed | - | Using wrong HTTP method |
| 500 Internal Server Error | - | Server error |

---

## JWT Token Structure

JWT tokens have 3 parts: `header.payload.signature`

**Payload contains:**
- `sub`: username
- `role`: user role (ADMIN)
- `iat`: issued at timestamp
- `exp`: expiration timestamp (24 hours from issue)

**Token expires in:** 24 hours (86400 seconds)

---

## Security Notes

1. **Always use HTTPS** in production
2. **Keep token secret** - don't expose in logs
3. **Token expires** after 24 hours - need to re-login
4. **Only admins** can access REST API
5. **Web UI** (Thymeleaf) uses separate session-based auth
6. **CORS** may need configuration for external frontend

---

## Common Issues & Solutions

### Issue: "401 Unauthorized"
- Check token is valid and not expired
- Verify token is in `Authorization: Bearer <token>` format
- Ensure correct URL with `/api/admin/` prefix

### Issue: "403 Forbidden"
- User account must have ADMIN role
- Check in database that user's role is set to ADMIN

### Issue: "Invalid credentials"
- Verify username and password are correct
- Check user exists in database

### Issue: "Token not recognized"
- Ensure you copied entire token value
- Check for extra spaces or line breaks
- Token must be in Authorization header exactly as: `Bearer <token>`
