package com.study.controller;

import com.study.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@MultipartConfig
@WebServlet("/registerPost")
public class RegisterPostServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> categories = null;
        try{
            categories = categoryDAO.getCategories();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/registerPost.jsp").forward(request, response);
    }


    @Override
    protected  void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            validateRequest(request);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        }

        String categoryId = request.getParameter("categoryId");
        String author = request.getParameter("author");
        String password = request.getParameter("password");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        Post post = new Post();
        post.setCategory_id(Integer.parseInt(categoryId));
        post.setAuthor(author);
        post.setPassword(password);
        post.setTitle(title);
        post.setContent(content);
        post.setView_count(0);
        post.setCreatedAt(currentTime);
        post.setUpdatedAt(currentTime);

        PostDAO postDAO = new PostDAO();
        int postId;
        try{
            postId = postDAO.registerPost(post);
        }catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"게시글 등록 중 오류 발생");
            return;
        }

        // TODO : 파일 유효성 검사 추가하기
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";

        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }

        for (Part part : request.getParts()) {
            if (part.getName().equals("file") && part.getSize() > 0){
                String originalFileName = extractFileName(part);
                String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
                String filePath = uploadPath + File.separator + storedFileName;

                part.write(filePath);

                Attachment attachment = new Attachment();
                attachment.setPostId(postId);
                attachment.setOriginalName(originalFileName);
                attachment.setStoredName(storedFileName);
                attachment.setLogicalPath("uploads/" + storedFileName);
                attachment.setPhysicalPath(filePath);
                attachment.setSize(part.getSize());

                AttachmentDAO attachmentDAO = new AttachmentDAO();
                try {
                    attachmentDAO.registerAttachment(attachment);
                }catch (SQLException e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "첨부 파일 저장 중 오류가 발생");
                }


            }
        }

        response.sendRedirect("postList");

    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String content : contentDisp.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }
        return "";
    }

    private void validateRequest(HttpServletRequest request) {
        String categoryId = request.getParameter("categoryId");
        String author = request.getParameter("author");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        if (categoryId == null || categoryId.isEmpty()) {
            throw new IllegalArgumentException("카테고리를 선택안함");
        }

        if (author == null || author.isEmpty()) {
            throw new IllegalArgumentException("작성자 입력안함");
        }

        if (password == null || password.isEmpty() ||
                password.length() < 4 || password.length() > 16 ||
                !password.matches(".*[a-zA-Z].*") ||
                !password.matches(".*[0-9].*") ||
                !password.matches(".*[!@#$%^&*].*")) {
            throw new IllegalArgumentException("비밀번호 조건 불일치 : 영문, 숫자, 특수문자를 포함하여 4자 이상, 16자 미만이어야 함");
        }

        if (confirmPassword == null || !password.equals(confirmPassword)) {
            throw new IllegalArgumentException("비밀번호 확인 불일치");
        }

        if (title == null || title.length() < 4 || title.length() > 100) {
            throw new IllegalArgumentException("제목 조건 불일치 : 4자 이상, 100자 미만이어야 함");
        }

        if (content == null || content.length() < 4 || content.length() > 2000) {
            throw new IllegalArgumentException("내용 조건 불일치 : 4자 이상, 2000자 미만이어야 함");
        }
    }
}
