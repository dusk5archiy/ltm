<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="app.model.bean.ScrapeJob" %>
<%@ page import="app.model.bean.User" %>
<html>
<head>
    <title>Bảng điều khiển</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        nav { margin-bottom: 20px; background-color: #f0f0f0; padding: 10px; display: flex; justify-content: space-between; align-items: center; }
        .nav-left, .nav-right { display: flex; align-items: center; }
        .nav-left a, .nav-right a { margin: 0 10px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <nav>
        <%@ include file="navbar.jsp" %>
    </nav>
    <h1>Bảng điều khiển</h1>
    <p>Chào mừng, <%= ((app.model.bean.User) session.getAttribute("user")).getUsername() %>!</p>
    <a href="createJob">Tạo công việc thu thập mới</a>
    <h2>Công việc thu thập của bạn</h2>
    <input type="text" id="searchBox" placeholder="Tìm kiếm công việc thu thập..." style="width: 100%; padding: 10px; margin-bottom: 20px; font-size: 16px; border: 1px solid #ccc; border-radius: 5px;">
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Trạng thái</th>
            <th>Ngày tạo</th>
            <th>Tiến độ</th>
            <th>Hành động</th>
        </tr>
        <%
            List<ScrapeJob> jobs = (List<ScrapeJob>) request.getAttribute("jobs");
            for (ScrapeJob job : jobs) {
        %>
        <tr>
            <td><%= job.getId() %></td>
            <td><%= job.getStatus() %> <% if ("failed".equals(job.getStatus()) && job.getErrorMessage() != null) { %> - <%= job.getErrorMessage() %><% } %></td>
            <td><%= job.getCreatedAt() %></td>
            <td>
                <progress id="progress-<%= job.getId() %>" value="<%= job.getScrapedCount() %>" max="<%= job.getTotalPages() %>"></progress>
                <span id="progress-text-<%= job.getId() %>"><%= job.getScrapedCount() %>/<%= job.getTotalPages() %></span>
            </td>
            <td><a href="jobDetails?id=<%= job.getId() %>">Xem chi tiết</a></td>
        </tr>
        <% } %>
    </table>
    <%
        StringBuilder _jobIdsBuilder = new StringBuilder();
        for (ScrapeJob job : jobs) {
            if (_jobIdsBuilder.length() > 0) _jobIdsBuilder.append(",");
            _jobIdsBuilder.append(job.getId());
        }
    %>
    <script>
        const searchBox = document.getElementById('searchBox');
        searchBox.addEventListener('input', function() {
            const query = this.value.toLowerCase();
            const rows = document.querySelectorAll('tbody tr');
            rows.forEach(row => {
                const status = row.cells[1].textContent.toLowerCase();
                const id = row.cells[0].textContent.toLowerCase();
                if (status.includes(query) || id.includes(query)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        });
    </script>
    <script>
        (function() {
            function fetchProgress(jobId) {
                fetch('progress?id=' + jobId, { credentials: 'same-origin', cache: 'no-cache' })
                    .then(res => res.json())
                    .then(data => {
                        const progress = document.getElementById('progress-' + jobId);
                        const text = document.getElementById('progress-text-' + jobId);
                        if (progress) {
                            const newProgress = document.createElement('progress');
                            newProgress.id = 'progress-' + jobId;
                            newProgress.max = data.totalPages || 1;
                            newProgress.value = data.scrapedCount || 0;
                            progress.parentNode.replaceChild(newProgress, progress);
                        }
                        if (text) {
                            const newText = document.createElement('span');
                            newText.id = 'progress-text-' + jobId;
                            newText.textContent = (data.scrapedCount || 0) + '/' + (data.totalPages || 0);
                            text.parentNode.replaceChild(newText, text);
                        }
                    }).catch(err => console.error('Progress fetch failed for', jobId, err));
            }

            function pollAll() {
                const elems = document.querySelectorAll('progress[id^="progress-"]');
                elems.forEach(el => {
                    const id = el.id.split('-')[1];
                    fetchProgress(id);
                });
            }

            setInterval(pollAll, 1000);
            pollAll();
        })();
    </script>
</body>
</html>
