<%@ page import="app.model.bean.User" %>
<div class="nav-left">
    <a href="/">Home</a>
    <%
        User user = (User) session.getAttribute("user");
        if (user != null) {
    %>
    | <a href="dashboard">Dashboard</a> | <a href="myJobs">My Jobs</a>
    <%
        }
    %>
</div>
<div class="nav-right">
    <%
        if (user != null) {
    %>
    Welcome, <%= user.getUsername() %> | <a href="logout">Sign Out</a>
    <%
        } else {
    %>
    <a href="login">Login</a>
    <%
        }
    %>
</div>