<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Tạo công việc thu thập</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        nav { margin-bottom: 20px; background-color: #f0f0f0; padding: 10px; display: flex; justify-content: space-between; align-items: center; }
        .nav-left, .nav-right { display: flex; align-items: center; }
        .nav-left a, .nav-right a { margin: 0 10px; }
    </style>
</head>
<body>
    <nav>
        <%@ include file="navbar.jsp" %>
    </nav>
    <h1>Tạo công việc thu thập mới</h1>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>
    <form action="createJob" method="post" onsubmit="return validateUrls()">
        <label>URL (mỗi dòng một URL):<br>
        <textarea name="urls" rows="10" cols="50" required placeholder="https://devwork.vn/job1&#10;https://devwork.vn/job2"></textarea></label><br>
        <button type="submit">Tạo công việc</button>
    </form>
    <a href="dashboard">Quay lại bảng điều khiển</a>
    <script>
        function validateUrls() {
            const textarea = document.querySelector('textarea[name="urls"]');
            const urls = textarea.value.trim().split(/\r?\n/).filter(url => url.trim() !== '');
            
            for (let url of urls) {
                url = url.trim();
                if (!url.startsWith('https://devwork.vn/')) {
                    alert('Chỉ chấp nhận URL từ devwork.vn. URL không hợp lệ: ' + url);
                    return false;
                }
            }
            
            if (urls.length === 0) {
                alert('Vui lòng nhập ít nhất một URL.');
                return false;
            }
            
            return true;
        }
    </script>
</body>
</html>
