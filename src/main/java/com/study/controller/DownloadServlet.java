package com.study.controller;

import com.study.model.Attachment;
import com.study.model.AttachmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
@WebServlet("/downloadAttachment")
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AttachmentDAO attachmentDAO = new AttachmentDAO();
        String logicalPath = request.getParameter("path");

        if (logicalPath == null || logicalPath.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "파일 경로 누락");
            return;
        }

        try {

            Attachment attachment = attachmentDAO.getAttachmentByLogicalPath(logicalPath);
            if (attachment == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "해당 첨부파일을 찾을 수 없음");
                return;
            }

            File file = new File(attachment.getPhysicalPath());
            if (!file.exists()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "해당 파일이 존재하지 않음");
                return;
            }

            response.setContentType("application/octet-stream");
            String encodedFileName = URLEncoder.encode(attachment.getOriginalName(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            response.setContentLengthLong(file.length());

            try (FileInputStream in = new FileInputStream(file);
                 OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "파일 다운로드 중 오류 발생");
        }
    }
}
