# 🧪 Learning Authentication – From Scratch to OAuth 2.0

This is a hands-on learning project to understand **OAuth 2.0** by starting from scratch — beginning with simple password-based authentication and evolving toward modern secure practices.

---

## 🚀 Phase 1A: Pure Password-Based Authentication

In this phase, **every API call requires the username and password**. This naive approach helps demonstrate the limitations of raw password-based auth and why more robust solutions like sessions, tokens, and OAuth exist.

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Spring Security (for password hashing only)
- H2 In-Memory Database

---

## 📌 Available APIs

### 🔹 POST `/auth/register`
Registers a new user.

**Request Body**
```json
{
  "username": "sarath",
  "password": "123456"
}
```

---

### 🔹 POST `/auth/login`
Verifies credentials.

**Request Body**
```json
{
  "username": "sarath",
  "password": "123456"
}
```

**Response**
```
Login successful
```

---

### 🔹 POST `/items`
Returns a list of items **only if credentials are valid**.

**Request Body**
```json
{
  "username": "sarath",
  "password": "123456"
}
```

**Response**
```json
[
  "Ancient Sword",
  "Magic Scroll",
  "Healing Potion"
]
```

---

## ⚠️ Why This Phase Matters

This phase demonstrates how raw password-based auth:

- Repeats credentials on every request
- Increases attack surface (passwords flying around)
- Makes the client responsible for storing credentials

It naturally motivates the transition to:
- Sessions
- Tokens (JWT)
- OAuth and delegated auth

---

## 🧠 Learning Roadmap

> This is part of a personal initiative to **build up understanding of OAuth 2.0** from the ground up.

Next steps:

- ✅ Phase 1A: Password-based auth (current)
- 🔜 Phase 1B: Session-based auth
- 🔜 Phase 2: Stateless auth with JWT
- 🔜 Phase 3: Introducing OAuth 2.0 roles (Client, Auth Server, Resource Server)
- 🔜 Phase 4: Authorization Code Flow
- 🔜 Phase 5: PKCE and refresh tokens

---

## ▶️ How to Run

Clone the repo and run:

```bash
./mvnw spring-boot:run
```

App will be available at `http://localhost:8080`

---

## ✍️ Blog Series

Stay tuned for the blog series documenting this journey.

```
From Plain Passwords to OAuth: A Hands-On Learning Path
```

---

## 📁 Repository Structure

| Folder/File       | Description                                |
|-------------------|--------------------------------------------|
| `src/`            | Main Java source code                      |
| `AuthController`  | Handles registration, login, and item auth |
| `User` entity     | Represents users in the system             |

---

## 📬 License

MIT — feel free to fork, learn, and build on top!
