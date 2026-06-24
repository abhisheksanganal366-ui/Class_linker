package com.classlinker.repository;

import com.classlinker.model.Announcement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnnouncementRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Announcement> mapper = (rs, rowNum) -> {
        Announcement a = new Announcement();
        a.setId(rs.getInt("id"));
        a.setTitle(rs.getString("title"));
        a.setContent(rs.getString("content"));
        a.setCreatedAt(rs.getString("created_at"));
        a.setLecturerName(rs.getString("lecturer_name"));
        a.setLecturerId(rs.getInt("lecturer_id"));
        return a;
    };

    public List<Announcement> findAll() {
        return jdbcTemplate.query("""
            SELECT a.id, a.title, a.content, a.created_at, u.name AS lecturer_name, a.lecturer_id
            FROM announcements a JOIN users u ON a.lecturer_id = u.id
            ORDER BY a.created_at DESC
            """, mapper);
    }

    public void add(String title, String content, int lecturerId) {
        jdbcTemplate.update(
            "INSERT INTO announcements (title, content, lecturer_id) VALUES (?, ?, ?)",
            title, content, lecturerId
        );
    }

    public void update(int id, String title, String content) {
        jdbcTemplate.update(
            "UPDATE announcements SET title = ?, content = ?, updated_at = datetime('now','localtime') WHERE id = ?",
            title, content, id
        );
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM announcements WHERE id = ?", id);
    }

    public Announcement findById(int id) {
        List<Announcement> list = jdbcTemplate.query("""
            SELECT a.id, a.title, a.content, a.created_at, u.name AS lecturer_name, a.lecturer_id
            FROM announcements a JOIN users u ON a.lecturer_id = u.id
            WHERE a.id = ?
            """, mapper, id);
        return list.isEmpty() ? null : list.get(0);
    }
}
