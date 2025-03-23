# 🗝️ Phase 1B – Session-Based Authentication

This branch demonstrates the second step in learning authentication: using **session-based authentication** instead of sending credentials with every request.

In the previous phase (Lesson 1), we sent the **username and password on every API call**, exposing the risk of credential leaks and making the system stateless and insecure.

In this phase, we simulate a **session mechanism** where:

- The user logs in once using `/auth/login`
- A **random UUID-based session token** is generated and returned
- This token is stored in a server-side map (`sessionStore`)
- All future API calls require this token in the `Authorization` header

---

## 📦 What This Phase Teaches

✅ Why sending raw credentials repeatedly is insecure  
✅ How session tokens simulate "logged in" state  
✅ How servers can validate users without storing passwords in memory or requiring them every time

---

## 🔧 APIs

### 🔹 `POST /auth/register`
Registers a new user.

```json
{
  "username": "sarath",
  "password": "123456"
}
```

---

### 🔹 `POST /auth/login`
Authenticates the user and returns a **session token**.

```json
{
  "username": "sarath",
  "password": "123456"
}
```

✅ Response:
```
07f964b1-07dc-43a1-aef0-518c55a0c646
```

---

### 🔹 `POST /auth/items`
Returns a list of items if the request contains a **valid session token**.

**Headers:**
```
Authorization: 07f964b1-07dc-43a1-aef0-518c55a0c646
```

✅ Response:
```json
[
  "Ancient Sword",
  "Magic Scroll",
  "Healing Potion"
]
```

---

## 🧠 Why This Matters

Session-based auth is still used widely (especially in traditional web apps). While not perfect, it's a huge improvement over raw Basic Auth or credential-passing.

You’ll see:
- Tokens replace credentials
- The server keeps track of logged-in users
- Simpler state handling before moving to stateless JWTs

---

## 🏃‍♂️ How to Run

```bash
git clone https://github.com/sarathraveendran6/learning-authentication.git
cd learning-authentication
git checkout lesson-2-session-based
./mvnw spring-boot:run
```

---

## ✍️ Blog Post 
- [Why Session-Based Auth Was My Next Step After Basic Login](https://medium.com/@sarathraveendran6/why-session-based-auth-was-my-next-step-after-basic-login-a6d2e933eb12)

---

## 🔄 Previous Phase

- [Lesson 1: Pure Password-Based Authentication](https://github.com/sarathraveendran6/learning-authentication/tree/lesson-1-pure-password)

---

## 📬 License

MIT — clone, learn, remix, share!
