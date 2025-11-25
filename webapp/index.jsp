<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="app.model.bean.JobDetail" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="com.google.gson.reflect.TypeToken" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
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
        .job-card { border: none; padding: 0; margin: 0; box-shadow: none; display: flex; align-items: center; }
        .job-thumbnail { width: 100px; height: 100px; object-fit: contain; margin-right: 15px; border-radius: 5px; background-color: #f0f0f0; }
        .job-title { font-size: 18px; font-weight: bold; color: #333; }
        .company { color: #666; }
        .location { color: #888; }
        .salary { color: #28a745; font-weight: bold; }
        .url { margin-top: 10px; }
        .url a { color: #007bff; text-decoration: none; }
        .url a:hover { text-decoration: underline; }
        .skill-tag {
            display: inline-block;
            background: #f0f0f0;
            padding: 2px 8px;
            margin: 2px;
            border-radius: 4px;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <nav>
        <%@ include file="navbar.jsp" %>
    </nav>
    <h1>Danh sách việc làm</h1>
    <input type="text" id="searchBox" placeholder="Tìm kiếm công việc..." style="width: 100%; padding: 10px; margin-bottom: 20px; font-size: 16px; border: 1px solid #ccc; border-radius: 5px;">
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
                    <%
                        Gson gson = new Gson();
                        List<String> skills = new ArrayList<>();
                        try {
                            skills = gson.fromJson(detail.getSkills(), new TypeToken<List<String>>(){}.getType());
                        } catch (Exception e) {
                            // Leave empty if parsing fails
                        }
                        Map<String, String> descriptions = new HashMap<>();
                        try {
                            descriptions = gson.fromJson(detail.getDescriptions(), new TypeToken<Map<String, String>>(){}.getType());
                        } catch (Exception e) {
                            // Leave empty if parsing fails
                        }
                        StringBuilder hiddenText = new StringBuilder();
                        for (String skill : skills) {
                            hiddenText.append(skill).append(" ");
                        }
                        for (String desc : descriptions.values()) {
                            hiddenText.append(desc).append(" ");
                        }
                    %>
                    <% if (detail.getThumbnail() != null && !detail.getThumbnail().isEmpty()) { %>
                        <img src="<%= detail.getThumbnail() %>" alt="Thumbnail" class="job-thumbnail">
                    <% } %>
                    <div>
                        <div class="job-title"><%= detail.getJobTitle() != null ? detail.getJobTitle() : "Việc làm chưa có tiêu đề" %></div>
                        <div class="company"><%= detail.getCompanyName() != null ? detail.getCompanyName() : "Công ty chưa xác định" %></div>
                        <div class="location"><%= detail.getProvince() != null ? detail.getProvince() : "Chưa xác định địa điểm" %></div>
                        <div class="salary"><%= detail.getSalary() != null ? detail.getSalary() : "Chưa xác định lương" %></div>
                        <% if (!skills.isEmpty()) { %>
                        <div>
                            <strong>Kỹ năng:</strong>
                            <% for (String skill : skills) { %>
                                <span class="skill-tag"><%= skill %></span>
                            <% } %>
                        </div>
                        <% } %>
                        <div class="url"><a href="jobView?id=<%= detail.getId() %>">Xem việc làm</a></div>
                        <div style="display:none;" class="hidden-search-content"><%= hiddenText.toString() %></div>
                    </div>
                </div>
            </td>
            <% } %>
        </tr>
        <% } %>
    </table>
    <script>
        const searchBox = document.getElementById('searchBox');
        searchBox.addEventListener('input', function() {
            const query = this.value.toLowerCase();
            const tds = document.querySelectorAll('td');
            tds.forEach(td => {
                const card = td.querySelector('.job-card');
                if (card) {
                    const title = card.querySelector('.job-title').textContent.toLowerCase();
                    const company = card.querySelector('.company').textContent.toLowerCase();
                    const location = card.querySelector('.location').textContent.toLowerCase();
                    const hidden = card.querySelector('.hidden-search-content').textContent.toLowerCase();
                    if (title.includes(query) || company.includes(query) || location.includes(query) || hidden.includes(query)) {
                        td.style.display = '';
                    } else {
                        td.style.display = 'none';
                    }
                }
            });
        });
    </script>
</body>
</html>
