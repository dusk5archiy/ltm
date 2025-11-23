<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Scrape Job</title>
</head>
<body>
    <h1>Create New Scrape Job</h1>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>
    <form action="createJob" method="post">
        <label>URLs (one per line):<br>
        <textarea name="urls" rows="10" cols="50" required placeholder="https://devwork.vn/job1&#10;https://devwork.vn/job2"></textarea></label><br>
        <button type="submit">Create Job</button>
    </form>
    <a href="dashboard">Back to Dashboard</a>
</body>
</html>