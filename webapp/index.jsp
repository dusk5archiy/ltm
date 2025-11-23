<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="app.model.bean.JobDetail" %>
<%@ page import="app.model.bean.User" %>
<%@ page import="app.model.bo.JobDetailBo" %>
<html>
<head>
    <title>Trang chủ - Danh sách việc làm</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }
        nav { margin-bottom: 20px; background-color: #fff; padding: 10px; border-radius: 5px; display: flex; justify-content: space-between; align-items: center; }
        .nav-left, .nav-right { display: flex; align-items: center; }
        .nav-left a, .nav-right a { margin: 0 10px; }
        table { width: 100%; border-collapse: collapse; background-color: #fff; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        td { border: 1px solid #ddd; padding: 15px; vertical-align: top; width: 50%; }
        .job-card { border: none; padding: 0; margin: 0; box-shadow: none; }
        .job-title { font-size: 18px; font-weight: bold; color: #333; }
        .company { color: #666; }
        .location { color: #888; }
        .salary { color: #28a745; font-weight: bold; }
        .url { margin-top: 10px; }
        .url a { color: #007bff; text-decoration: none; }
        .url a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <nav>
        <%@ include file="navbar.jsp" %>
    </nav>
    <h1>Danh sách việc làm</h1>
    <table>
        <%
            JobDetailBo jobDetailBo = new JobDetailBo();
            List<JobDetail> jobDetails = jobDetailBo.findAll();
            int i = 0;
            while (i < jobDetails.size()) {
        %>
        <tr>
            <%
                for (int j = 0; j < 2 && i < jobDetails.size(); j++, i++) {
                    JobDetail detail = jobDetails.get(i);
            %>
            <td>
                <div class="job-card">
                    <div class="job-title"><%= detail.getJobTitle() != null ? detail.getJobTitle() : "Việc làm chưa có tiêu đề" %></div>
                    <div class="company"><%= detail.getCompanyName() != null ? detail.getCompanyName() : "Công ty chưa xác định" %></div>
                    <div class="location"><%= detail.getProvince() != null ? detail.getProvince() : "Chưa xác định địa điểm" %></div>
                    <div class="salary"><%= detail.getSalary() != null ? detail.getSalary() : "Chưa xác định lương" %></div>
                    <div class="url"><a href="jobView?id=<%= detail.getId() %>">Xem việc làm</a></div>
                </div>
            </td>
            <% } %>
        </tr>
        <% } %>
    </table>
</body>
</html>
