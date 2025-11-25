<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="app.model.bean.ScrapeJob" %>
<%@ page import="app.model.bean.User" %>
<%@ page import="app.model.bo.ScrapeJobBo" %>
<%@ page import="app.model.bo.UserBo" %>
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

    <h2>Hàng đợi xử lý</h2>
    <div id="processQueue">
        <!-- Live queue status will be updated here -->
        <p>Đang tải...</p>
    </div>

    <h2>Tất cả các quy trình thu thập</h2>
    <table border="1" style="width: 100%; border-collapse: collapse;">
        <thead>
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Trạng thái</th>
                <th>Ngày tạo</th>
                <th>Tiến độ</th>
            </tr>
        </thead>
        <tbody>
        <%
            ScrapeJobBo scrapeJobBo = new ScrapeJobBo();
            UserBo userBo = new UserBo();
            List<ScrapeJob> allJobs = scrapeJobBo.findAll();
            for (ScrapeJob job : allJobs) {
                User jobUser = userBo.findById(job.getUserId()).orElse(null);
        %>
        <tr>
            <td><%= job.getId() %></td>
            <td><%= jobUser != null ? jobUser.getUsername() : "Unknown" %></td>
            <td><%= job.getStatus() %> <% if ("failed".equals(job.getStatus()) && job.getErrorMessage() != null) { %> - <%= job.getErrorMessage() %><% } %></td>
            <td><%= job.getCreatedAt() %></td>
            <td>
                <progress id="progress-<%= job.getId() %>" value="<%= job.getScrapedCount() %>" max="<%= job.getTotalPages() %>"></progress>
                <span id="progress-text-<%= job.getId() %>"><%= job.getScrapedCount() %>/<%= job.getTotalPages() %></span>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <script>
        (function() {
            function fetchProgress(jobId) {
                if (!jobId || jobId === '') return;
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
                const elems = document.querySelectorAll('table tbody progress[id^="progress-"]');
                elems.forEach(el => {
                    const id = el.id.split('-')[1];
                    fetchProgress(id);
                });
            }

            function updateStats() {
                fetch('statistics?format=json', { credentials: 'same-origin', cache: 'no-cache' })
                    .then(res => res.json())
                    .then(data => {
                        const stats = document.querySelectorAll('.stat p');
                        stats[0].textContent = data.totalUsers;
                        stats[1].textContent = data.totalScrapeJobs;
                        stats[2].textContent = data.totalJobDetails;
                    }).catch(err => console.error('Stats fetch failed', err));
            }

            function updateJobs() {
                fetch('statistics?format=jobs', { credentials: 'same-origin', cache: 'no-cache' })
                    .then(res => res.json())
                    .then(data => {
                        const tbody = document.querySelector('table tbody');
                        const newTbody = document.createElement('tbody');
                        data.forEach(job => {
                            const tr = document.createElement('tr');
                            tr.innerHTML = '<td>' + job.id + '</td><td>' + job.username + '</td><td>' + job.status + (job.errorMessage ? ' - ' + job.errorMessage : '') + '</td><td>' + job.createdAt + '</td><td><progress id="progress-' + job.id + '" value="' + job.scrapedCount + '" max="' + job.totalPages + '"></progress><span id="progress-text-' + job.id + '">' + job.scrapedCount + '/' + job.totalPages + '</span></td>';
                            newTbody.appendChild(tr);
                        });
                        if (tbody) {
                            tbody.parentNode.replaceChild(newTbody, tbody);
                        }
                    }).catch(err => console.error('Jobs fetch failed', err));
            }

            function updateQueue() {
                const runningJobs = Array.from(document.querySelectorAll('tbody tr')).filter(tr => {
                    const statusCell = tr.cells[2];
                    return statusCell && statusCell.textContent.includes('running');
                });
                const queueDiv = document.getElementById('processQueue');
                if (runningJobs.length > 0) {
                    queueDiv.innerHTML = '<ul>' + runningJobs.map(tr => '<li>Job ID: ' + tr.cells[0].textContent + ' - ' + tr.cells[2].textContent + '</li>').join('') + '</ul>';
                } else {
                    queueDiv.innerHTML = '<p>Không có quy trình đang chạy.</p>';
                }
            }

            setInterval(updateStats, 1000);
            setInterval(updateJobs, 1000);
            setInterval(pollAll, 1000);
            setInterval(updateQueue, 1000);

            updateStats();
            updateJobs();
            pollAll();
            updateQueue();
        })();
    </script>
</body>
</html>