<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>게시판 - 보기</title>
    <style>
        body {
            width: 80%;
            margin: auto;
        }

        .header-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
        }

        .header-row div {
            margin-right: 15px;
        }

        .title-row {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 20px;
        }

        .title {
            font-weight: bold;
            font-size: 20px;
        }

        .content {
            padding: 15px;
            border: 1px solid #000;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>

<h2>게시판 - 보기</h2>
<div class="header-row">
    <div>작성자: ${post.author}</div>
    <div>등록일시: ${post.createdAt}</div>
    <div>수정일시: ${post.updatedAt}</div>
</div>

<div class="title-row">
    <div class="title">[${post.categoryName}] ${post.title}</div>
    <div style="margin-right: 20px">조회수: ${post.view_count}</div>
</div>


<div class="content">
    <p>${post.content}</p>
</div>


<div class="attachments">
    <c:if test="${not empty post.attachments}">
        <ul>
            <c:forEach var="attachment" items="${post.attachments}">
                <li><a href="downloadAttachment?path=${attachment.logicalPath}">${attachment.originalName}</a></li>
            </c:forEach>
        </ul>
    </c:if>
</div>


<div class="comments"
     style="background-color: #f0f0f0; padding: 15px; border-radius: 5px; margin-bottom: 20px; border: 1px solid #ddd;">
    <c:if test="${not empty comments}">
        <ul style="list-style-type: none; padding: 0;">
            <c:forEach var="comment" items="${comments}">
                <li class="comment" style="padding: 10px 0; border-bottom: 1px solid #ccc;">
                    <div class="comment-header" style="font-size: 0.9em; color: #555;">
                        <strong>${comment.author}</strong> (${comment.createdAt})
                    </div>
                    <div class="comment-content" style="margin-top: 5px; font-size: 1em; color: #333;">
                            ${comment.content}
                    </div>
                </li>
            </c:forEach>
        </ul>
    </c:if>


    <div class="comment-form"
         style="background-color: #fff; padding: 15px; border: 1px solid #ddd; border-radius: 5px;">
        <form action="addComment" method="post" style="display: flex; flex-direction: column;">
            <input type="hidden" name="postId" value="${post.id}">


            <input type="text" name="author" placeholder="작성자 입력" required
                   style="width: 100px; padding: 8px; margin-bottom: 10px; border: 1px solid #ccc; border-radius: 4px;">


            <div style="display: flex; align-items: flex-start;">
                <textarea name="content" placeholder="댓글을 입력해 주세요." required
                          style="width: 80%; height: 60px; margin-right: 10px; resize: none; border: 1px solid #ccc; border-radius: 4px;"></textarea>
                <button type="submit" style="padding: 8px 15px;">등록</button>
            </div>
        </form>
    </div>
</div>


<div class="buttons">
    <button onclick="window.location.href='postList'">목록</button>
    <button onclick="location.href='editPost?id=${post.id}'">수정</button>
    <button onclick="openPasswordLayer()">삭제</button>
</div>

<div id="passwordLayer"
     style="position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0, 0, 0, 0.5); display: none; justify-content: center; align-items: center;">
    <div style="background: white; padding: 20px; border-radius: 8px; width: 300px;">
        <h3>비밀번호 확인</h3>
        <form id="passwordForm" action="deletePost" method="post">
            <input type="hidden" name="postId" value="${post.id}">
            <input type="password" name="password" placeholder="비밀번호 입력" required
                   style="width: 100%; padding: 8px; margin-bottom: 10px; border: 1px solid #ccc; border-radius: 4px;">
            <div style="display: flex; justify-content: space-between;">
                <button type="button" onclick="closePasswordLayer()"
                        style="padding: 8px 15px;  border-radius: 4px;">취소
                </button>
                <button type="submit"
                        style="padding: 8px 15px;   border-radius: 4px;">
                    확인
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    function openPasswordLayer() {
        document.getElementById("passwordLayer").style.display = "flex";
    }

    function closePasswordLayer() {
        document.getElementById("passwordLayer").style.display = "none";
    }
</script>

</body>
</html>
