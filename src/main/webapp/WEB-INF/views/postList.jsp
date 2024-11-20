<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>게시글 목록</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            text-align: center;
        }
        th, td {
            border-bottom: 1px solid #000;
            padding: 8px;
        }
    </style>
</head>
<body style="display: flex; justify-content: center;">
<div class="board" style="width: 90%;">
    <h2>자유 게시판 - 목록</h2>
    <form method="get" action="postList" style="display: flex; align-items: center; gap: 10px; border: 1px solid black; padding: 5px;">
        <p>등록일</p>
        <input type="date" name="startDate" value="${startDate}">
        <p>~</p>
        <input type="date" name="endDate" value="${endDate}">
        <select name="categoryId">
            <option value="0">전체 카테고리</option>
            <c:forEach var="category" items="${categories}">
                <option value="${category.id}" ${param.categoryId == category.id ? 'selected' : ''}>${category.categoryName}</option>
            </c:forEach>
        </select>
        <input type="text" name="keyword" placeholder="검색어를 입력해 주세요. (제목+작성자+내용)" value="${param.keyword}" style="width: 300px;">
        <button type="submit">검색</button>
    </form>

    <p>총 ${posts.size()}건</p>
    <table border="1">
        <thead>
        <tr>
            <th>카테고리</th>
            <th>첨부파일</th>
            <th>제목</th>
            <th>작성자</th>
            <th>조회수</th>
            <th>등록 일시</th>
            <th>수정 일시</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="post" items="${posts}">
            <tr>
                <td>${post.categoryName}</td>
                <td>
                    <c:if test="${not empty post.attachments}">
                        📎
                    </c:if>
                </td>
                <td><a href="viewPost?id=${post.id}">${post.title}</a></td>
                <td>${post.author}</td>
                <td>${post.view_count}</td>
                <td>${post.createdAt}</td>
                <td>
                    <c:if test="${post.createdAt eq post.updatedAt}">-</c:if>
                    <c:if test="${post.createdAt ne post.updatedAt}">${post.updatedAt}</c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <!-- 페이지네이션 -->
    <div class="pagination" style="display: flex; justify-content: center; margin-top: 10px;">
        <c:forEach begin="1" end="${pageCount}" var="page">
            <a href="postList?page=${page}&keyword=${param.keyword}&categoryId=${param.categoryId}&startDate=${param.startDate}&endDate=${param.endDate}"
               class="${page == currentPage ? 'current' : ''}"
               style="margin: 0 5px;">
                    ${page}
            </a>
        </c:forEach>
    </div>

    <div style="display: flex; justify-content: flex-end; margin-top: 10px;">
        <button type="button" onclick="window.location.href='registerPost'">등록</button>
    </div>
</div>
</body>
</html>
