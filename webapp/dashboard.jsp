<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="app.model.bean.ScrapeJob" %>
<%@ page import="app.model.bean.User" %>
<html>
<head>
    <title>Dashboard</title>
</head>
<body>
    <h1>Dashboard</h1>
    <p>Welcome, <%= ((app.model.bean.User) session.getAttribute("user")).getUsername() %>!</p>
    <a href="createJob">Create New Scrape Job</a>
    <h2>Your Scrape Jobs</h2>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Status</th>
            <th>Created At</th>
            <th>Progress</th>
            <th>Actions</th>
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
            <td><a href="jobDetails?id=<%= job.getId() %>">View Details</a></td>
        </tr>
        <% } %>
    </table>
    <a href="logout">Logout</a>
    <%
        StringBuilder _jobIdsBuilder = new StringBuilder();
        for (ScrapeJob job : jobs) {
            if (_jobIdsBuilder.length() > 0) _jobIdsBuilder.append(",");
            _jobIdsBuilder.append(job.getId());
        }
    %>
    <script>
        (function() {
            function fetchProgress(jobId) {
                fetch('progress?id=' + jobId)
                    .then(res => res.json())
                    .then(data => {
                        const progress = document.getElementById('progress-' + jobId);
                        const text = document.getElementById('progress-text-' + jobId);
                        if (progress) {
                            progress.max = data.totalPages || 1;
                            progress.value = data.scrapedCount || 0;
                        }
                        if (text) {
                            text.textContent = (data.scrapedCount || 0) + '/' + (data.totalPages || 0);
                        }
                    }).catch(err => console.error('Progress fetch failed for', jobId, err));
            }

            function pollAll() {
                const elems = document.querySelectorAll('[id^="progress-"]');
                elems.forEach(el => {
                    const id = el.id.split('-')[1];
                    fetchProgress(id);
                });
            }

            setInterval(pollAll, 5000);
            pollAll();
        })();
    </script>
</body>
</html>