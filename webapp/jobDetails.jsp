<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="app.model.bean.JobDetail" %>
<%@ page import="app.model.bean.ScrapeJob" %>
<%@ page import="app.model.bean.User" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="com.google.gson.reflect.TypeToken" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.HashMap" %>
<html>
<head>
    <title>Chi tiết công việc</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        nav { margin-bottom: 20px; background-color: #f0f0f0; padding: 10px; display: flex; justify-content: space-between; align-items: center; }
        .nav-left, .nav-right { display: flex; align-items: center; }
        .nav-left a, .nav-right a { margin: 0 10px; }
        .job-container {
            display: flex;
            border: 1px solid #ccc;
            margin: 10px;
            padding: 10px;
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
    </style>
</head>
<body>
    <nav>
        <%@ include file="navbar.jsp" %>
    </nav>
    <h1>Chi tiết công việc thu thập</h1>
    <%
        ScrapeJob job = (ScrapeJob) request.getAttribute("job");
    %>
    <p><strong>ID công việc:</strong> <%= job.getId() %></p>
    <p><strong>Trạng thái:</strong> <span id="jobStatus"><%= job.getStatus() %></span></p>
    <p><strong>Ngày tạo:</strong> <%= job.getCreatedAt() %></p>
    <% if ("failed".equals(job.getStatus()) && job.getErrorMessage() != null) { %>
        <p><strong>Lỗi:</strong> <%= job.getErrorMessage() %></p>
    <% } %>

    <h2>Công việc đã thu thập</h2>
    <div id="progressArea">
        <label for="progressBar">Tiến độ:</label>
        <progress id="progressBar" value="<%= job.getScrapedCount() %>" max="<%= job.getTotalPages() %>"></progress>
        <span id="progressText"><%= job.getScrapedCount() %>/<%= job.getTotalPages() %></span>
    </div>
    <%
        List<JobDetail> details = (List<JobDetail>) request.getAttribute("details");
        Gson gson = new Gson();
        for (JobDetail detail : details) {
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
            <p><strong>Công ty:</strong> <a href="<%= detail.getCompanyUrl() %>"><%= detail.getCompanyName() %></a></p>
            <p><strong>Tỉnh:</strong> <%= detail.getProvince() %></p>
            <p><strong>Lương:</strong> <%= detail.getSalary() %></p>
            <p><strong>URL:</strong> <a href="<%= detail.getUrl() %>"><%= detail.getUrl() %></a></p>
            <% if (detail.getThumbnail() != null) { %>
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
                <strong>Kỹ năng:</strong>
                <% for (String skill : skills) { %>
                    <span class="skill-tag"><%= skill %></span>
                <% } %>
            </div>
        </div>
        <div class="job-info">
            <% if (!jobInfo.isEmpty()) { %>
            <h4>Thông tin công việc</h4>
            <% for (Map.Entry<String, String> entry : jobInfo.entrySet()) { %>
                <p><strong><%= entry.getKey() %>:</strong> <%= entry.getValue() %></p>
            <% } %>
            <% } %>
        </div>
    </div>
    <% } %>
    <a href="dashboard">Quay lại bảng điều khiển</a>
    <script>
        (function() {
            const jobId = '<%= job.getId() %>';
            const progressBar = document.getElementById('progressBar');
            const progressText = document.getElementById('progressText');
            const statusSpan = document.getElementById('jobStatus');

            // Prefer SSE (EventSource) for live updates; fallback to polling
            if (typeof(EventSource) !== 'undefined') {
                const source = new EventSource('progress-sse?id=' + jobId);
                source.onmessage = function(e) {
                    try {
                        const data = JSON.parse(e.data);
                        const total = data.totalPages || 0;
                        const scraped = data.scrapedCount || 0;
                        progressBar.max = total > 0 ? total : 1;
                        progressBar.value = scraped;
                        progressText.textContent = scraped + '/' + total;
                        if (statusSpan) statusSpan.textContent = data.status;
                        if (data.status === 'completed' || data.status === 'failed') {
                            source.close();
                        }
                    } catch (ex) {
                        console.error('Bad SSE payload', ex);
                    }
                };
                source.onerror = function(err) {
                    console.error('SSE error', err);
                    source.close();
                };
            } else {
                function fetchProgress() {
                    fetch('progress?id=' + jobId, { credentials: 'same-origin', cache: 'no-cache' })
                        .then(res => {
                            if (!res.ok) throw new Error('Network error');
                            return res.json();
                        })
                        .then(data => {
                            const total = data.totalPages || 0;
                            const scraped = data.scrapedCount || 0;
                            progressBar.max = total > 0 ? total : 1;
                            progressBar.value = scraped;
                            progressText.textContent = scraped + '/' + total;
                            if (statusSpan) statusSpan.textContent = data.status;
                            if (data.status === 'completed' || data.status === 'failed') {
                                clearInterval(pollInterval);
                            }
                        })
                        .catch(err => console.error('Failed to fetch progress', err));
                }

                const pollInterval = setInterval(fetchProgress, 2000);
                fetchProgress();
            }
        })();
    </script>
</body>
</html>