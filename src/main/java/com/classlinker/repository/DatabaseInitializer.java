package com.classlinker.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        createTables();
        createDefaultLecturer();
    }

    private void createTables() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                role TEXT NOT NULL,
                phone TEXT,
                roll_number TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS announcements (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                content TEXT NOT NULL,
                lecturer_id INTEGER,
                created_at TIMESTAMP DEFAULT (datetime('now','localtime')),
                updated_at TIMESTAMP DEFAULT (datetime('now','localtime')),
                FOREIGN KEY (lecturer_id) REFERENCES users(id)
            )
        """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS resources (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                description TEXT,
                file_type TEXT,
                file_link TEXT,
                lecturer_id INTEGER,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (lecturer_id) REFERENCES users(id)
            )
        """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS assignments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT,
                due_date DATE,
                total_marks INTEGER,
                lecturer_id INTEGER,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (lecturer_id) REFERENCES users(id)
            )
        """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS assignment_submissions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                assignment_id INTEGER,
                student_id INTEGER,
                status TEXT DEFAULT 'Not Submitted',
                marks INTEGER DEFAULT 0,
                submitted_at TIMESTAMP,
                FOREIGN KEY (assignment_id) REFERENCES assignments(id),
                FOREIGN KEY (student_id) REFERENCES users(id)
            )
        """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS tests (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                test_name TEXT NOT NULL,
                total_marks INTEGER,
                lecturer_id INTEGER,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (lecturer_id) REFERENCES users(id)
            )
        """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS test_marks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                test_id INTEGER,
                student_id INTEGER,
                marks INTEGER DEFAULT 0,
                FOREIGN KEY (test_id) REFERENCES tests(id),
                FOREIGN KEY (student_id) REFERENCES users(id)
            )
        """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                student_id INTEGER,
                question TEXT NOT NULL,
                answer TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                answered_at TIMESTAMP,
                FOREIGN KEY (student_id) REFERENCES users(id)
            )
        """);
    }

    private void createDefaultLecturer() {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE email = 'admin@123'", Integer.class);
        if (count == null || count == 0) {
            String hashedPw = hashPassword("1234");
            jdbcTemplate.update(
                "INSERT INTO users (name, email, password, role, phone) VALUES (?, ?, ?, ?, ?)",
                "Default Lecturer", "admin@123", hashedPw, "lecturer", "1234567890"
            );
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
