<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Thống kê</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }
        nav { margin-bottom: 20px; background-color: #fff; padding: 10px; border-radius: 5px; display: flex; justify-content: space-between; align-items: center; }
        .nav-left, .nav-right { display: flex; align-items: center; }
        .nav-left a, .nav-right a { margin: 0 10px; }
        .stats { display: flex; justify-content: space-around; margin-top: 20px; }
        .stat { border: 1px solid #ccc; padding: 20px; text-align: center; width: 200px; background-color: #fff; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .stat h2 { margin: 0; color: #333; }
        .stat p { font-size: 24px; color: #007bff; }
    </style>
</head>
<body>
    <nav>
        <%@ include file="navbar.jsp" %>
    </nav>
    <h1>Thống kê quản trị</h1>
    <div class="stats">
        <div class="stat">
            <h2>Tổng số người dùng</h2>
            <p>${totalUsers}</p>
        </div>
        <div class="stat">
            <h2>Tổng số công việc thu thập</h2>
            <p>${totalScrapeJobs}</p>
        </div>
        <div class="stat">
            <h2>Tổng số chi tiết công việc</h2>
            <p>${totalJobDetails}</p>
        </div>
    </div>
</body>
</html>