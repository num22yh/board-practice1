<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시글 등록</title>
    <script>
        function validateForm() {
            const categoryId = document.forms["postForm"]["categoryId"].value;
            if (categoryId === "") {
                alert("카테고리를 선택해 주세요.");
                return false;
            }

            const author = document.forms["postForm"]["author"].value;
            if (author === "") {
                alert("작성자를 입력해주세요.");
                return false;
            }

            const password = document.forms["postForm"]["password"].value;
            const confirmPassword = document.forms["postForm"]["confirmPassword"].value;
            if (password.length < 4 || password.length > 16 || !/[a-zA-Z]/.test(password) || !/[0-9]/.test(password) || !/[!@#$%^&*]/.test(password)) {
                alert("비밀번호는 영문, 숫자, 특수문자를 포함하여 4자 이상, 16자 미만이어야 합니다.");
                return false;
            }

            if (password !== confirmPassword) {
                alert("비밀번호 확인이 일치하지 않습니다.");
                return false;
            }

            const title = document.forms["postForm"]["title"].value;
            if (title.length < 4 || title.length > 100) {
                alert("제목은 4자 이상, 100자 미만이어야 합니다.");
                return false;
            }

            const content = document.forms["postForm"]["content"].value;
            if (content.length < 4 || content.length > 2000) {
                alert("내용은 4자 이상, 2000자 미만이어야 합니다.");
                return false;
            }

            return true;
        }
    </script>
</head>
<body style="width: 80%; margin: 0 auto;">
<h1>게시글 등록</h1>
<form name="postForm" action="registerPost" method="post" enctype="multipart/form-data"
      onsubmit="return validateForm()">
    <table border="1" cellpadding="10" cellspacing="0" style="width: 100%;">
        <tr>
            <th>카테고리</th>
            <td>
                <select name="categoryId">
                    <option value="">카테고리 선택</option>
                    <c:forEach var="category" items="${categories}">
                        <option value="${category.id}">${category.categoryName}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <th>작성자</th>
            <td><input type="text" name="author"></td>
        </tr>
        <tr>
            <th>비밀번호</th>
            <td><input type="password" name="password"></td>
        </tr>
        <tr>
            <th>비밀번호 확인</th>
            <td><input type="password" name="confirmPassword"></td>
        </tr>
        <tr>
            <th>제목</th>
            <td style="display: flex; align-items: center; justify-content: center;"><input type="text" name="title" style="width: 100%;"></td>
        </tr>
        <tr>
            <th>내용</th>
            <td style="display: flex; align-items: center; justify-content: center;"><textarea name="content" rows="10" style="width: 100%;"></textarea>
            </td>
        </tr>
        <tr>
            <th>파일 첨부</th>
            <td>
                <input type="file" name="file" multiple>
                <input type="file" name="file" multiple>
                <input type="file" name="file" multiple>
            </td>

        </tr>

    </table>
    <div style="display: flex; justify-content: space-between; margin-top: 20px;">
        <button type="button" onclick="window.location.href='postList'">취소</button>
        <button type="submit">저장</button>
    </div>
</form>
</body>
</html>
