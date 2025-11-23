<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="app.model.bean.User" %>
<div class="nav-left">
    <a href="/">Trang chủ</a>
    <%
        User user = (User) session.getAttribute("user");
        if (user != null) {
    %>
    | <a href="dashboard">Bảng điều khiển</a> | <a href="myJobs">Việc của tôi</a>
    <%
        if ("admin".equals(user.getRole())) {
    %>
    | <a href="statistics">Thống kê</a>
    <%
        }
    %>
    <%
        }
    %>
</div>
<div class="nav-right">
    <%
        if (user != null) {
    %>
    Chào mừng, <%= user.getUsername() %> | <a href="logout">Đăng xuất</a>
    <%
        } else {
    %>
    <a href="login">Đăng nhập</a>
    <%
        }
    %>
</div>