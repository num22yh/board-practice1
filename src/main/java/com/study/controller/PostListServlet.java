package com.study.controller;


import com.study.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// TODO : Front-Controller 패턴 적용하기

@WebServlet("/postList")
public class PostListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostDAO postDAO = new PostDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        AttachmentDAO attachmentDAO = new AttachmentDAO();

        String keyword = request.getParameter("keyword");
        int categoryId = request.getParameter("categoryId") != null && !request.getParameter("categoryId").isEmpty()
                ? Integer.parseInt(request.getParameter("categoryId"))
                : 0;

        LocalDate endDateLocal = LocalDate.now();
        LocalDate startDateLocal = endDateLocal.minusYears(1);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        String startDateParam = request.getParameter("startDate");
        String startDate = startDateParam != null && !startDateParam.isEmpty()
                ? startDateParam
                : startDateLocal.format(dateFormatter);

        // 종료일 처리 (시간 포함)
        String endDateParam = request.getParameter("endDate");
        LocalDateTime endDateTime = endDateParam != null && !endDateParam.isEmpty()
                ? LocalDate.parse(endDateParam, dateFormatter).atTime(LocalTime.MAX)
                : endDateLocal.atTime(LocalTime.MAX);
        String endDateForQuery = endDateTime.format(dateTimeFormatter);
        String endDateDisplay = endDateTime.toLocalDate().format(dateFormatter); // JSP에는 날짜 부분만 추출해서 표기

        int page = request.getParameter("page") != null && !request.getParameter("page").isEmpty()
                ? Integer.parseInt(request.getParameter("page"))
                : 1;
        int pageSize = 10;

        List<Post> posts = null;
        int postCount = 0;
        List<Category> categories = null;
        try {

            posts = postDAO.getPosts(keyword, categoryId, startDate, endDateForQuery, page, pageSize);
            postCount = postDAO.getPostCount(keyword, categoryId, startDate, endDateForQuery);
            categories = categoryDAO.getCategories();

            for (Post post : posts) {
                String categoryName = categoryDAO.getCategoryNameById(post.getCategory_id());
                post.setCategoryName(categoryName);

                List<Attachment> attachments = attachmentDAO.getAttachmentsByPostId(post.getId());
                post.setAttachments(attachments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "게시글 목록 불러오는 중 오류 발생");
            return;
        }

        int pageCount = (int) Math.ceil((double) postCount / pageSize);

        request.setAttribute("posts", posts);
        request.setAttribute("categories", categories);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("pageCount", pageCount);
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDateDisplay);
        request.getRequestDispatcher("/WEB-INF/views/postList.jsp").forward(request, response);
    }
}
