package com.study.controller;

import com.study.model.Comment;
import com.study.model.CommentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/addComment")
public class AddCommentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int postId = Integer.parseInt(request.getParameter("postId"));
        String author = request.getParameter("author");
        String content = request.getParameter("content");

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setAuthor(author);
        comment.setContent(content);

        CommentDAO commentDAO = new CommentDAO();
        try {
            commentDAO.addComment(comment);
            response.sendRedirect("viewPost?id=" + postId);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "댓글 저장 중 오류 발생");
        }
    }
}
