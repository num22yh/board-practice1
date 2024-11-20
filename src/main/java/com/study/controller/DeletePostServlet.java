package com.study.controller;

import com.study.model.PostDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;


// TODO : 삭제 기능 완성 , 수정 기능 추가

@WebServlet("/deletePost")
public class DeletePostServlet extends HttpServlet {
    private PostDAO postDAO = new PostDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int postId = Integer.parseInt(request.getParameter("postId"));
        String password = request.getParameter("password");

        try {
            boolean isDeleted = postDAO.deletePostById(postId, password);
            if (isDeleted) {
                response.sendRedirect("postList");
            } else {

                request.setAttribute("error", "비밀번호가 일치하지 않습니다");
                request.getRequestDispatcher("viewPost").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("게시글 삭제 중 오류 발생", e);
        }
    }
}
