<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="app.model.bean.JobDetail" %>
<html>
<head>
    <title>My Jobs</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }
        nav { margin-bottom: 20px; background-color: #fff; padding: 10px; border-radius: 5px; display: flex; justify-content: space-between; align-items: center; }
        .nav-left, .nav-right { display: flex; align-items: center; }
        .nav-left a, .nav-right a { margin: 0 10px; }
        table { width: 100%; border-collapse: collapse; background-color: #fff; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        td { border: 1px solid #ddd; padding: 15px; vertical-align: top; width: 50%; }
        .job-card { border: none; padding: 0; margin: 0; box-shadow: none; position: relative; }
        .job-title { font-size: 18px; font-weight: bold; color: #333; }
        .company { color: #666; }
        .location { color: #888; }
        .salary { color: #28a745; font-weight: bold; }
        .url { margin-top: 10px; }
        .url a { color: #007bff; text-decoration: none; }
        .url a:hover { text-decoration: underline; }
        .actions { margin-top: 10px; }
        .actions button { margin-right: 5px; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer; }
        .edit-btn { background-color: #ffc107; color: #000; text-decoration: none; display: inline-block; padding: 5px 10px; border-radius: 3px; }
        .delete-btn { background-color: #dc3545; color: #fff; }
    </style>
</head>
<body>
    <nav>
        <%@ include file="navbar.jsp" %>
    </nav>
    <h1>My Scraped Jobs</h1>
    <table>
        <%
            List<JobDetail> jobDetails = (List<JobDetail>) request.getAttribute("jobDetails");
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
                    <div class="job-title"><%= detail.getJobTitle() != null ? detail.getJobTitle() : "Untitled Job" %></div>
                    <div class="company"><%= detail.getCompanyName() != null ? detail.getCompanyName() : "Unknown Company" %></div>
                    <div class="location"><%= detail.getProvince() != null ? detail.getProvince() : "Location not specified" %></div>
                    <div class="salary"><%= detail.getSalary() != null ? detail.getSalary() : "Salary not specified" %></div>
                    <div class="url"><a href="jobView?id=<%= detail.getId() %>">View Job</a></div>
                    <div class="actions">
                        <a href="editJob?id=<%= detail.getId() %>" class="edit-btn">Edit</a>
                        <form method="post" action="deleteJob" style="display: inline;">
                            <input type="hidden" name="id" value="<%= detail.getId() %>">
                            <button type="submit" class="delete-btn" onclick="return confirm('Are you sure you want to delete this job?')">Delete</button>
                        </form>
                    </div>
                </div>
            </td>
            <% } %>
        </tr>
        <% } %>
    </table>
    <script>
        function deleteJob(id) {
            // Temporarily empty
            alert('Delete job ' + id);
        }
    </script>
</body>
</html>