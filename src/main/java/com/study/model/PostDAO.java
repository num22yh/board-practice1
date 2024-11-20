package com.study.model;

import com.study.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
    public int registerPost(Post post) throws SQLException {
        String sql = "INSERT INTO posts (category_id, author, password, title, content, view_count, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[] {"id"})) {
            ps.setInt(1, post.getCategory_id());
            ps.setString(2, post.getAuthor());
            ps.setString(3, post.getPassword());
            ps.setString(4, post.getTitle());
            ps.setString(5, post.getContent());
            ps.setInt(6, post.getView_count());
            ps.setTimestamp(7, post.getCreatedAt());
            ps.setTimestamp(8, post.getUpdatedAt());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("게시글 아이디 가져올 수 없음.");
    }

    public List<Post> getPosts(String keyword, int categoryId, String startDate, String endDate, int page, int pageSize) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM posts WHERE 1=1");

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (title LIKE ? OR author LIKE ? OR content LIKE ?)");
        }
        if (categoryId > 0) {
            sql.append(" AND category_id = ?");
        }
        if (startDate != null && endDate != null) {
            sql.append(" AND created_at BETWEEN ? AND ?");
        }
        sql.append(" ORDER BY created_at DESC LIMIT ?, ?");

        List<Post> posts = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + keyword + "%");
                ps.setString(paramIndex++, "%" + keyword + "%");
                ps.setString(paramIndex++, "%" + keyword + "%");
            }
            if (categoryId > 0) {
                ps.setInt(paramIndex++, categoryId);
            }
            if (startDate != null && endDate != null) {
                ps.setString(paramIndex++, startDate);
                ps.setString(paramIndex++, endDate);
            }
            ps.setInt(paramIndex++, (page - 1) * pageSize);
            ps.setInt(paramIndex, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post();
                    post.setId(rs.getInt("id"));
                    post.setCategory_id(rs.getInt("category_id"));
                    post.setAuthor(rs.getString("author"));
                    post.setTitle(rs.getString("title"));
                    post.setView_count(rs.getInt("view_count"));
                    post.setCreatedAt(rs.getTimestamp("created_at"));
                    post.setUpdatedAt(rs.getTimestamp("updated_at"));
                    posts.add(post);
                }
            }
        }
        return posts;
    }

    public int getPostCount(String keyword, int categoryId, String startDate, String endDate) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM posts WHERE 1=1");

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (title LIKE ? OR author LIKE ? OR content LIKE ?)");
        }
        if (categoryId > 0) {
            sql.append(" AND category_id = ?");
        }
        if (startDate != null && endDate != null) {
            sql.append(" AND created_at BETWEEN ? AND ?");
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (keyword != null && !keyword.isEmpty()) {
                ps.setString(paramIndex++, "%" + keyword + "%");
                ps.setString(paramIndex++, "%" + keyword + "%");
                ps.setString(paramIndex++, "%" + keyword + "%");
            }
            if (categoryId > 0) {
                ps.setInt(paramIndex++, categoryId);
            }
            if (startDate != null && endDate != null) {
                ps.setString(paramIndex++, startDate);
                ps.setString(paramIndex, endDate);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("게시글 수를 가져올 수 없습니다.");
    }

    public Post getPostById(int postId) throws SQLException {
        String sql = "SELECT * FROM posts WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Post post = new Post();
                    post.setId(rs.getInt("id"));
                    post.setCategory_id(rs.getInt("category_id"));
                    post.setAuthor(rs.getString("author"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setView_count(rs.getInt("view_count"));
                    post.setCreatedAt(rs.getTimestamp("created_at"));
                    post.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return post;
                }
            }
        }
        return null; // 게시글이 없으면 null 반환
    }

    public void incrementViewCount(int postId) throws SQLException {
        String sql = "UPDATE posts SET view_count = view_count + 1 WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postId);
            ps.executeUpdate();
        }
    }

    public boolean deletePostById(int postId, String password) throws SQLException {
        String sql = "DELETE FROM posts WHERE id = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postId);
            ps.setString(2, password);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }





}
