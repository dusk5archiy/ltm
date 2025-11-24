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
    <form action="createJob" method="post">
        <label>URL (mỗi dòng một URL):<br>
        <textarea name="urls" rows="10" cols="50" required placeholder="https://devwork.vn/job1&#10;https://devwork.vn/job2"></textarea></label><br>
        <button type="submit">Tạo công việc</button>
    </form>
    <a href="dashboard">Quay lại bảng điều khiển</a>
</body>
</html>
