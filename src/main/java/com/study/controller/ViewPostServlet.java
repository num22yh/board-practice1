package com.study.controller;

import com.study.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
@WebServlet("/viewPost")
public class ViewPostServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostDAO postDAO = new PostDAO();
        AttachmentDAO attachmentDAO = new AttachmentDAO();
        int postId;

        try {
            postId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "올바른 게시글 ID가 필요합니다.");
            return;
        }

        try {
            // 조회수 증가
            postDAO.incrementViewCount(postId);

            // 게시글 정보 가져오기
            Post post = postDAO.getPostById(postId);
            if (post == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "해당 게시글을 찾을 수 없습니다.");
                return;
            }

            // 카테고리 이름 가져오기
            CategoryDAO categoryDAO = new CategoryDAO();
            String categoryName = categoryDAO.getCategoryNameById(post.getCategory_id());
            post.setCategoryName(categoryName);

            // 첨부 파일 정보 가져오기
            List<Attachment> attachments = attachmentDAO.getAttachmentsByPostId(postId);
            post.setAttachments(attachments);

            // 댓글 가져오기
            CommentDAO commentDAO = new CommentDAO();
            List<Comment> comments = commentDAO.getCommentsByPostId(postId);
            request.setAttribute("comments", comments);

            // 게시글과 댓글 정보를 JSP로 전달
            request.setAttribute("post", post);
            request.getRequestDispatcher("/WEB-INF/views/viewPost.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "게시글 불러오는 중 오류 발생");
        }
    }
}
