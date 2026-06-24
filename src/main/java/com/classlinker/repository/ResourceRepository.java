package com.classlinker.repository;

import com.classlinker.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResourceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Resource> mapper = (rs, rowNum) -> {
        Resource r = new Resource();
        r.setId(rs.getInt("id"));
        r.setName(rs.getString("name"));
        r.setDescription(rs.getString("description"));
        r.setFileType(rs.getString("file_type"));
        r.setFileLink(rs.getString("file_link"));
        r.setCreatedAt(rs.getString("created_at"));
        return r;
    };

    public List<Resource> findAll() {
        return jdbcTemplate.query(
            "SELECT id, name, description, file_type, file_link, created_at FROM resources ORDER BY created_at DESC",
            mapper
        );
    }

    public List<Resource> search(String term) {
        return jdbcTemplate.query(
            "SELECT id, name, description, file_type, file_link, created_at FROM resources WHERE name LIKE ? ORDER BY created_at DESC",
            mapper, "%" + term + "%"
        );
    }

    public void add(String name, String description, String fileType, String fileLink, int lecturerId) {
        jdbcTemplate.update(
            "INSERT INTO resources (name, description, file_type, file_link, lecturer_id) VALUES (?, ?, ?, ?, ?)",
            name, description, fileType, fileLink, lecturerId
        );
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM resources WHERE id = ?", id);
    }
}
