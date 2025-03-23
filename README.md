This repository documents my personal journey of learning **OAuth 2.0** by incrementally building authentication systems from scratch — starting with the worst possible method and evolving toward modern standards like JWT and OAuth 2.0.

Each **phase** is captured in its own Git branch so you can explore them independently.

---

## 📚 Learning Phases

| Phase | Description | Branch |
|-------|-------------|--------|
| 1️⃣ Phase 1A | [Pure Password-Based Authentication](https://github.com/sarathraveendran6/learning-authentication/tree/lesson-1-pure-password) <br>Every request includes raw credentials. No sessions, no tokens — just pain. | `lesson-1-pure-password` |
| 2️⃣ Phase 1B | Session-Based Authentication *(Coming soon)* <br>Login once, receive a session token, and use it to access protected routes. | `lesson-2-session-based` |
| 3️⃣ Phase 2 | JWT-Based Authentication *(Coming soon)* | `lesson-3-jwt` |
| 4️⃣ Phase 3 | OAuth 2.0 Roles & Flows *(Coming soon)* | `lesson-4-oauth` |

---

## 🛠 How to Explore Each Phase

To check out and run a phase:

```bash
git clone https://github.com/sarathraveendran6/learning-authentication.git
cd learning-authentication
git checkout lesson-1-pure-password
./mvnw spring-boot:run
```

Replace the branch name with any phase you'd like to explore.

---

## ✍️ Blog Series

This project is accompanied by a blog series published on Medium:

- [Why I Started With the Worst Way to Authenticate](https://medium.com/@sarathraveendran6/why-i-started-with-the-worst-way-to-authenticate-and-why-you-should-too-acd5ef1715f0)

More articles coming soon as each phase progresses.

---

## 🙌 Contributing

This is a personal learning project, but feel free to fork, follow along, and suggest improvements!

---

## 📬 License

MIT — clone, learn, remix, and share!