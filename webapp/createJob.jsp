<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Scrape Job</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        nav { margin-bottom: 20px; background-color: #f0f0f0; padding: 10px; display: flex; justify-content: space-between; align-items: center; }
        .nav-left, .nav-right { display: flex; align-items: center; }
        .nav-left a, .nav-right a { margin: 0 10px; }
    </style>
</head>
<body>
    <nav>
        <%@ include file="navbar.jsp" %>
    </nav>
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