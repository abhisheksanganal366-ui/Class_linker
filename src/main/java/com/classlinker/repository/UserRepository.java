package com.classlinker.repository;

import com.classlinker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setRole(rs.getString("role"));
        u.setPhone(rs.getString("phone"));
        u.setRollNumber(rs.getString("roll_number"));
        return u;
    };

    public Optional<User> findByEmailAndPassword(String email, String password) {
        String hashed = DatabaseInitializer.hashPassword(password);
        List<User> users = jdbcTemplate.query(
            "SELECT id, name, email, role, phone, roll_number FROM users WHERE email = ? AND password = ?",
            userRowMapper, email, hashed
        );
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE email = ?", Integer.class, email);
        return count != null && count > 0;
    }

    public void registerStudent(String name, String email, String password, String phone, String rollNumber) {
        String hashed = DatabaseInitializer.hashPassword(password);
        jdbcTemplate.update(
            "INSERT INTO users (name, email, password, role, phone, roll_number) VALUES (?, ?, ?, 'student', ?, ?)",
            name, email, hashed, phone, rollNumber
        );
    }

    public List<User> getAllStudents() {
        return jdbcTemplate.query(
            "SELECT id, name, email, role, phone, roll_number FROM users WHERE role = 'student' ORDER BY name",
            userRowMapper
        );
    }

    public Optional<User> findById(int id) {
        List<User> users = jdbcTemplate.query(
            "SELECT id, name, email, role, phone, roll_number FROM users WHERE id = ?",
            userRowMapper, id
        );
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public void addStudentByLecturer(String name, String email, String phone, String rollNumber) {
        registerStudent(name, email, "student123", phone, rollNumber);
    }

    public void updateStudent(int id, String name, String email, String phone, String rollNumber) {
        jdbcTemplate.update(
            "UPDATE users SET name = ?, email = ?, phone = ?, roll_number = ? WHERE id = ? AND role = 'student'",
            name, email, phone, rollNumber, id
        );
    }

    public void deleteStudent(int id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ? AND role = 'student'", id);
    }
}
