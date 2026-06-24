# ClassLinker Web App

A full-stack web version of ClassLinker built with:
- **Backend:** Java 21 + Spring Boot 3
- **Frontend:** HTML + CSS + JavaScript (Thymeleaf templates)
- **Database:** SQLite

## How to Run

### Option 1 — Double-click RUN.bat
Just double-click `RUN.bat` in this folder.  
It will download Maven automatically on first run.

### Option 2 — Command line
```
cd classlinker-web
mvnw.cmd spring-boot:run
```

Then open your browser at: **http://localhost:8080**

## Default Login
| Role     | Email     | Password |
|----------|-----------|----------|
| Lecturer | admin@123 | 1234     |
| Student  | Register via app or add from Lecturer dashboard (default pw: student123) |

## Features
### Lecturer
- Announcements (Add / Edit / Delete)
- Resources (Add / Delete + link support)
- Assignments (Create + Manage submissions & marks)
- Test Marks (Add tests + Enter/Edit marks per student)
- Student Management (Add / Edit / Delete)
- Q&A (View & Answer student questions)

### Student
- View Announcements
- Search & View Resources
- View Assignments & submission status
- View Test Marks with percentage
- Ask Questions & view answers
