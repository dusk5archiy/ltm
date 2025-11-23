<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="app.model.bean.JobDetail" %>
<%@ page import="app.model.bean.User" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="com.google.gson.reflect.TypeToken" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<html>
<head>
    <title>Job Details</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }
        nav { margin-bottom: 20px; background-color: #fff; padding: 10px; border-radius: 5px; display: flex; justify-content: space-between; align-items: center; }
        .nav-left, .nav-right { display: flex; align-items: center; }
        .nav-left a, .nav-right a { margin: 0 10px; }
        .job-container {
            display: flex;
            border: 1px solid #ccc;
            margin: 10px;
            padding: 10px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .job-main {
            flex: 3;
        }
        .job-info {
            flex: 1;
            margin-left: 20px;
            border-left: 1px solid #eee;
            padding-left: 10px;
        }
        .skill-tag {
            display: inline-block;
            background: #f0f0f0;
            padding: 2px 8px;
            margin: 2px;
            border-radius: 4px;
            font-size: 12px;
        }
        .description-section h4 {
            margin-top: 20px;
            margin-bottom: 5px;
        }
        .description-section p {
            margin: 0;
        }
        .back-link {
            margin-top: 20px;
            display: inline-block;
            padding: 10px 15px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        .back-link:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <nav>
        <%@ include file="navbar.jsp" %>
    </nav>
    <%
        JobDetail detail = (JobDetail) request.getAttribute("jobDetail");
        if (detail != null) {
            Gson gson = new Gson();

            // Parse skills
            List<String> skills = new ArrayList<>();
            try {
                skills = gson.fromJson(detail.getSkills(), new TypeToken<List<String>>(){}.getType());
            } catch (Exception e) {
                // Leave empty if parsing fails
            }

            // Parse descriptions
            Map<String, String> descriptions = new HashMap<>();
            try {
                descriptions = gson.fromJson(detail.getDescriptions(), new TypeToken<Map<String, String>>(){}.getType());
            } catch (Exception e) {
                // Leave empty if parsing fails
            }

            // Parse jobInfo
            Map<String, String> jobInfo = new HashMap<>();
            try {
                jobInfo = gson.fromJson(detail.getJobInfo(), new TypeToken<Map<String, String>>(){}.getType());
            } catch (Exception e) {
                // Leave empty if parsing fails
            }
    %>
    <div class="job-container">
        <div class="job-main">
            <h3><%= detail.getJobTitle() %></h3>
            <p><strong>Company:</strong> <a href="<%= detail.getCompanyUrl() %>" target="_blank"><%= detail.getCompanyName() %></a></p>
            <p><strong>Province:</strong> <%= detail.getProvince() %></p>
            <p><strong>Salary:</strong> <%= detail.getSalary() %></p>
            <p><strong>URL:</strong> <a href="<%= detail.getUrl() %>" target="_blank"><%= detail.getUrl() %></a></p>
            <% if (detail.getThumbnail() != null && !detail.getThumbnail().isEmpty()) { %>
                <img src="<%= detail.getThumbnail() %>" alt="Thumbnail" style="max-width:200px;">
            <% } %>
            <% if (!descriptions.isEmpty()) { %>
            <div class="description-section">
                <% for (Map.Entry<String, String> entry : descriptions.entrySet()) { %>
                    <h4><%= entry.getKey() %></h4>
                    <p><%= entry.getValue() %></p>
                <% } %>
            </div>
            <% } %>
            <div>
                <strong>Skills:</strong>
                <% for (String skill : skills) { %>
                    <span class="skill-tag"><%= skill %></span>
                <% } %>
            </div>
        </div>
        <div class="job-info">
            <% if (!jobInfo.isEmpty()) { %>
            <h4>Job Info</h4>
            <% for (Map.Entry<String, String> entry : jobInfo.entrySet()) { %>
                <p><strong><%= entry.getKey() %>:</strong> <%= entry.getValue() %></p>
            <% } %>
            <% } %>
            <p><strong>Collected At:</strong> <%= detail.getCollectedAt() %></p>
        </div>
    </div>
    <a href="/" class="back-link">Back to Home</a>
    <% } else { %>
    <p>Job not found.</p>
    <% } %>
</body>
</html>