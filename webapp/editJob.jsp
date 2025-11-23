<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="app.model.bean.JobDetail" %>
<html>
<head>
    <title>Chỉnh sửa việc làm</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }
        nav { margin-bottom: 20px; background-color: #fff; padding: 10px; border-radius: 5px; display: flex; justify-content: space-between; align-items: center; }
        .nav-left, .nav-right { display: flex; align-items: center; }
        .nav-left a, .nav-right a { margin: 0 10px; }
        .form-container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); max-width: 800px; margin: 0 auto; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], textarea { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
        .key-value { margin-bottom: 5px; }
        .vertical-pairs .key-value { display: block; }
        .horizontal-pairs .key-value { display: flex; }
        .horizontal-pairs .key-value input { margin-right: 5px; flex: 1; }
        .key-value input[type="text"], .key-value textarea { width: 100%; margin-bottom: 5px; padding: 8px; border: 1px solid #ddd; border-radius: 4px; font-family: Arial, sans-serif; }
        .key-value textarea { resize: vertical; }
        .add-btn { background-color: #28a745; color: white; border: none; padding: 5px 10px; border-radius: 3px; cursor: pointer; }
        .remove-btn { background-color: #dc3545; color: white; border: none; padding: 5px 10px; border-radius: 3px; cursor: pointer; }
        .submit-btn { background-color: #007bff; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; font-size: 16px; }
        .submit-btn:hover { background-color: #0056b3; }
    </style>
</head>
<body>
    <nav>
        <%@ include file="navbar.jsp" %>
    </nav>
    <div class="form-container">
        <h1>Chỉnh sửa việc làm</h1>
        <%
            JobDetail jobDetail = (JobDetail) request.getAttribute("jobDetail");
            List<String> skills = (List<String>) request.getAttribute("skills");
            Map<String, String> descriptions = (Map<String, String>) request.getAttribute("descriptions");
            Map<String, String> jobInfo = (Map<String, String>) request.getAttribute("jobInfo");
        %>
        <form method="post" action="editJob">
            <input type="hidden" name="id" value="<%= jobDetail.getId() %>">

            <div class="form-group">
                <label>Tiêu đề công việc:</label>
                <input type="text" name="jobTitle" value="<%= jobDetail.getJobTitle() != null ? jobDetail.getJobTitle() : "" %>">
            </div>

            <div class="form-group">
                <label>Tên công ty:</label>
                <input type="text" name="companyName" value="<%= jobDetail.getCompanyName() != null ? jobDetail.getCompanyName() : "" %>">
            </div>

            <div class="form-group">
                <label>Tỉnh:</label>
                <input type="text" name="province" value="<%= jobDetail.getProvince() != null ? jobDetail.getProvince() : "" %>">
            </div>

            <div class="form-group">
                <label>Lương:</label>
                <input type="text" name="salary" value="<%= jobDetail.getSalary() != null ? jobDetail.getSalary() : "" %>">
            </div>

            <div class="form-group">
                <label>URL:</label>
                <input type="text" name="url" value="<%= jobDetail.getUrl() != null ? jobDetail.getUrl() : "" %>">
            </div>

            <div class="form-group">
                <label>URL công ty:</label>
                <input type="text" name="companyUrl" value="<%= jobDetail.getCompanyUrl() != null ? jobDetail.getCompanyUrl() : "" %>">
            </div>

            <div class="form-group">
                <label>Hình ảnh thu nhỏ:</label>
                <input type="text" name="thumbnail" value="<%= jobDetail.getThumbnail() != null ? jobDetail.getThumbnail() : "" %>">
            </div>

            <div class="form-group">
                <label>Kỹ năng (mỗi dòng một kỹ năng):</label>
                <textarea name="skills" rows="5"><%
                    if (skills != null) {
                        for (String skill : skills) {
                            out.print(skill + "\n");
                        }
                    }
                %></textarea>
            </div>

            <div class="form-group">
                <label>Mô tả:</label>
                <div id="descriptions" class="vertical-pairs">
                    <%
                        if (descriptions != null) {
                            int i = 0;
                            for (Map.Entry<String, String> entry : descriptions.entrySet()) {
                    %>
                    <div class="key-value">
                        <input type="text" name="descKeys" value="<%= entry.getKey() %>" placeholder="Khóa">
                        <textarea name="descValues" rows="3" placeholder="Giá trị"><%= entry.getValue() %></textarea>
                        <button type="button" class="remove-btn" onclick="removeField(this)">Xóa</button>
                    </div>
                    <%
                                i++;
                            }
                        }
                    %>
                </div>
                <button type="button" class="add-btn" onclick="addField('descriptions', 'descKeys', 'descValues')">Thêm mô tả</button>
            </div>

            <div class="form-group">
                <label>Thông tin công việc:</label>
                <div id="jobInfo" class="horizontal-pairs">
                    <%
                        if (jobInfo != null) {
                            for (Map.Entry<String, String> entry : jobInfo.entrySet()) {
                    %>
                    <div class="key-value">
                        <input type="text" name="infoKeys" value="<%= entry.getKey() %>" placeholder="Khóa">
                        <input type="text" name="infoValues" value="<%= entry.getValue() %>" placeholder="Giá trị">
                        <button type="button" class="remove-btn" onclick="removeField(this)">Xóa</button>
                    </div>
                    <%
                            }
                        }
                    %>
                </div>
                <button type="button" class="add-btn" onclick="addField('jobInfo', 'infoKeys', 'infoValues')">Thêm thông tin công việc</button>
            </div>

            <button type="submit" class="submit-btn">Lưu thay đổi</button>
        </form>
    </div>

    <script>
        function addField(containerId, keyName, valueName) {
            const container = document.getElementById(containerId);
            const div = document.createElement('div');
            div.className = 'key-value';
            if (containerId === 'descriptions') {
                div.innerHTML = '<input type="text" name="' + keyName + '" placeholder="Khóa"><textarea name="' + valueName + '" rows="3" placeholder="Giá trị"></textarea><button type="button" class="remove-btn" onclick="removeField(this)">Xóa</button>';
            } else {
                div.innerHTML = '<input type="text" name="' + keyName + '" placeholder="Khóa"><input type="text" name="' + valueName + '" placeholder="Giá trị"><button type="button" class="remove-btn" onclick="removeField(this)">Xóa</button>';
            }
            container.appendChild(div);
        }

        function removeField(button) {
            button.parentElement.remove();
        }
    </script>
</body>
</html>