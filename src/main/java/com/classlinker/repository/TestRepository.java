package com.classlinker.repository;

import com.classlinker.model.Test;
import com.classlinker.model.TestMark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TestRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Test> testMapper = (rs, rowNum) -> {
        Test t = new Test();
        t.setId(rs.getInt("id"));
        t.setTestName(rs.getString("test_name"));
        t.setTotalMarks(rs.getInt("total_marks"));
        t.setCreatedAt(rs.getString("created_at"));
        return t;
    };

    private final RowMapper<TestMark> markMapper = (rs, rowNum) -> {
        TestMark m = new TestMark();
        m.setId(rs.getInt("id"));
        m.setStudentName(rs.getString("student_name"));
        m.setRollNumber(rs.getString("roll_number"));
        m.setMarks(rs.getInt("marks"));
        m.setTotalMarks(rs.getInt("total_marks"));
        m.setTestId(rs.getInt("test_id"));
        m.setStudentId(rs.getInt("student_id"));
        return m;
    };

    public List<Test> findAll() {
        return jdbcTemplate.query(
            "SELECT id, test_name, total_marks, created_at FROM tests ORDER BY created_at DESC",
            testMapper
        );
    }

    public void add(String testName, int totalMarks, int lecturerId) {
        jdbcTemplate.update(
            "INSERT INTO tests (test_name, total_marks, lecturer_id) VALUES (?, ?, ?)",
            testName, totalMarks, lecturerId
        );
        int testId = jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Integer.class);
        List<Integer> studentIds = jdbcTemplate.queryForList(
            "SELECT id FROM users WHERE role = 'student'", Integer.class
        );
        for (int sid : studentIds) {
            jdbcTemplate.update(
                "INSERT INTO test_marks (test_id, student_id, marks) VALUES (?, ?, 0)",
                testId, sid
            );
        }
    }

    public List<TestMark> getMarks(int testId) {
        return jdbcTemplate.query("""
            SELECT tm.id, u.name AS student_name, u.roll_number, tm.marks, t.total_marks, tm.test_id, tm.student_id
            FROM test_marks tm JOIN users u ON tm.student_id = u.id JOIN tests t ON tm.test_id = t.id
            WHERE tm.test_id = ? ORDER BY u.name
            """, markMapper, testId);
    }

    public void updateMark(int markId, int marks) {
        jdbcTemplate.update("UPDATE test_marks SET marks = ? WHERE id = ?", marks, markId);
    }

    public List<TestMark> getStudentMarks(int studentId) {
        return jdbcTemplate.query("""
            SELECT tm.id, t.test_name AS student_name, '' AS roll_number, tm.marks, t.total_marks, tm.test_id, tm.student_id
            FROM test_marks tm JOIN tests t ON tm.test_id = t.id
            WHERE tm.student_id = ? ORDER BY t.created_at DESC
            """, (rs, rowNum) -> {
                TestMark m = new TestMark();
                m.setId(rs.getInt("id"));
                m.setTestName(rs.getString("student_name"));
                m.setMarks(rs.getInt("marks"));
                m.setTotalMarks(rs.getInt("total_marks"));
                m.setTestId(rs.getInt("test_id"));
                m.setStudentId(rs.getInt("student_id"));
                return m;
            }, studentId
        );
    }

    public Test findById(int id) {
        List<Test> list = jdbcTemplate.query(
            "SELECT id, test_name, total_marks, created_at FROM tests WHERE id = ?",
            testMapper, id
        );
        return list.isEmpty() ? null : list.get(0);
    }
}
