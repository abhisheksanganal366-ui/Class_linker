package com.classlinker.repository;

import com.classlinker.model.Assignment;
import com.classlinker.model.AssignmentSubmission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AssignmentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Assignment> assignMapper = (rs, rowNum) -> {
        Assignment a = new Assignment();
        a.setId(rs.getInt("id"));
        a.setTitle(rs.getString("title"));
        a.setDescription(rs.getString("description"));
        a.setDueDate(rs.getString("due_date"));
        a.setTotalMarks(rs.getInt("total_marks"));
        a.setCreatedAt(rs.getString("created_at"));
        return a;
    };

    private final RowMapper<AssignmentSubmission> subMapper = (rs, rowNum) -> {
        AssignmentSubmission s = new AssignmentSubmission();
        s.setId(rs.getInt("id"));
        s.setStudentName(rs.getString("student_name"));
        s.setRollNumber(rs.getString("roll_number"));
        s.setStatus(rs.getString("status"));
        s.setMarks(rs.getInt("marks"));
        s.setAssignmentId(rs.getInt("assignment_id"));
        s.setStudentId(rs.getInt("student_id"));
        return s;
    };

    public List<Assignment> findAll() {
        return jdbcTemplate.query(
            "SELECT id, title, description, due_date, total_marks, created_at FROM assignments ORDER BY due_date DESC",
            assignMapper
        );
    }

    public void add(String title, String description, String dueDate, int totalMarks, int lecturerId) {
        jdbcTemplate.update(
            "INSERT INTO assignments (title, description, due_date, total_marks, lecturer_id) VALUES (?, ?, ?, ?, ?)",
            title, description, dueDate, totalMarks, lecturerId
        );
        int assignmentId = jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Integer.class);
        List<Integer> studentIds = jdbcTemplate.queryForList(
            "SELECT id FROM users WHERE role = 'student'", Integer.class
        );
        for (int sid : studentIds) {
            jdbcTemplate.update(
                "INSERT INTO assignment_submissions (assignment_id, student_id, status) VALUES (?, ?, 'Not Submitted')",
                assignmentId, sid
            );
        }
    }

    public List<AssignmentSubmission> getSubmissions(int assignmentId) {
        return jdbcTemplate.query("""
            SELECT s.id, u.name AS student_name, u.roll_number, s.status, s.marks, s.assignment_id, s.student_id
            FROM assignment_submissions s JOIN users u ON s.student_id = u.id
            WHERE s.assignment_id = ? ORDER BY u.name
            """, subMapper, assignmentId);
    }

    public void updateSubmission(int submissionId, String status, int marks) {
        jdbcTemplate.update(
            "UPDATE assignment_submissions SET status = ?, marks = ?, submitted_at = CURRENT_TIMESTAMP WHERE id = ?",
            status, marks, submissionId
        );
    }

    public List<AssignmentSubmission> getStudentAssignments(int studentId) {
        return jdbcTemplate.query("""
            SELECT s.id, a.title AS student_name, '' AS roll_number, COALESCE(sub.status,'Not Submitted') AS status,
                   COALESCE(sub.marks,0) AS marks, a.id AS assignment_id, ? AS student_id
            FROM assignments a
            LEFT JOIN assignment_submissions sub ON a.id = sub.assignment_id AND sub.student_id = ?
            ORDER BY a.due_date DESC
            """, (rs, rowNum) -> {
                AssignmentSubmission s = new AssignmentSubmission();
                s.setId(rs.getInt("id"));
                s.setStudentName(rs.getString("student_name"));
                s.setStatus(rs.getString("status"));
                s.setMarks(rs.getInt("marks"));
                s.setAssignmentId(rs.getInt("assignment_id"));
                s.setStudentId(rs.getInt("student_id"));
                return s;
            }, studentId, studentId
        );
    }

    public Assignment findById(int id) {
        List<Assignment> list = jdbcTemplate.query(
            "SELECT id, title, description, due_date, total_marks, created_at FROM assignments WHERE id = ?",
            assignMapper, id
        );
        return list.isEmpty() ? null : list.get(0);
    }
}
