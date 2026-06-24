package com.classlinker.repository;

import com.classlinker.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Question> mapper = (rs, rowNum) -> {
        Question q = new Question();
        q.setId(rs.getInt("id"));
        q.setStudentId(rs.getInt("student_id"));
        q.setStudentName(rs.getString("student_name"));
        q.setRollNumber(rs.getString("roll_number"));
        q.setQuestion(rs.getString("question"));
        q.setAnswer(rs.getString("answer"));
        q.setCreatedAt(rs.getString("created_at"));
        q.setAnsweredAt(rs.getString("answered_at"));
        return q;
    };

    public List<Question> findAll() {
        return jdbcTemplate.query("""
            SELECT q.id, q.student_id, u.name AS student_name, u.roll_number,
                   q.question, q.answer, q.created_at, q.answered_at
            FROM questions q JOIN users u ON q.student_id = u.id
            ORDER BY q.created_at DESC
            """, mapper);
    }

    public void add(int studentId, String question) {
        jdbcTemplate.update(
            "INSERT INTO questions (student_id, question) VALUES (?, ?)",
            studentId, question
        );
    }

    public void answer(int questionId, String answer) {
        jdbcTemplate.update(
            "UPDATE questions SET answer = ?, answered_at = CURRENT_TIMESTAMP WHERE id = ?",
            answer, questionId
        );
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM questions WHERE id = ?", id);
    }

    public List<Question> getByStudent(int studentId) {
        return jdbcTemplate.query("""
            SELECT q.id, q.student_id, '' AS student_name, '' AS roll_number,
                   q.question, q.answer, q.created_at, q.answered_at
            FROM questions q WHERE q.student_id = ? ORDER BY q.created_at DESC
            """, mapper, studentId);
    }
}
