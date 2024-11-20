package com.study.model;

import com.study.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttachmentDAO {

    public void registerAttachment(Attachment attachment) throws SQLException {
        String sql = "INSERT INTO attachments (post_id, original_name, stored_name, logical_path, physical_path, size) VALUES (?,?,?,?,?,?)";
        try(Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, attachment.getPostId());
            ps.setString(2, attachment.getOriginalName());
            ps.setString(3, attachment.getStoredName());
            ps.setString(4, attachment.getLogicalPath());
            ps.setString(5, attachment.getPhysicalPath());
            ps.setLong(6, attachment.getSize());
            ps.executeUpdate();
        }
    }

    public List<Attachment> getAttachmentsByPostId(int postId) throws SQLException {
        List<Attachment> attachments = new ArrayList<>();
        String sql = "SELECT * FROM attachments WHERE post_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Attachment attachment = new Attachment();
                    attachment.setId(rs.getInt("id"));
                    attachment.setPostId(rs.getInt("post_id"));
                    attachment.setOriginalName(rs.getString("original_name"));
                    attachment.setStoredName(rs.getString("stored_name"));
                    attachment.setLogicalPath(rs.getString("logical_path"));
                    attachment.setPhysicalPath(rs.getString("physical_path"));
                    attachment.setSize(rs.getLong("size"));
                    attachments.add(attachment);
                }
            }
        }
        return attachments;
    }


    public Attachment getAttachmentByLogicalPath(String logicalPath) throws SQLException {
        String sql = "SELECT * FROM attachments WHERE logical_path = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, logicalPath);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Attachment attachment = new Attachment();
                    attachment.setId(rs.getInt("id"));
                    attachment.setOriginalName(rs.getString("original_name"));
                    attachment.setStoredName(rs.getString("stored_name"));
                    attachment.setLogicalPath(rs.getString("logical_path"));
                    attachment.setPhysicalPath(rs.getString("physical_path"));
                    attachment.setSize(rs.getLong("size"));
                    return attachment;
                }
            }
        }
        return null;
    }
}
