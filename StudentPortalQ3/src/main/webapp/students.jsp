<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List,java.util.Map" %>

<%!
    // Escape values before displaying them in HTML
    private String esc(Object value) {
        if (value == null) return "";

        return value.toString()
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
%>

<%
    // Allow only logged-in users to view student profiles
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Map<String, Object>> students =
        (List<Map<String, Object>>) request.getAttribute("students");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Profile Portal</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/style.css">
</head>

<body>

<div class="container">

    <h2>Student Profile Portal</h2>

    <p>View student details and uploaded documents.</p>

    <a class="btn"
       href="<%= request.getContextPath() %>/logout">
       Logout
    </a>

    <% if (students == null || students.isEmpty()) { %>

        <p>No student records found.</p>

    <% } else { %>

        <% for (Map<String, Object> student : students) { %>

            <div class="student-card">

                <h3><%= esc(student.get("name")) %></h3>

                <p>
                    <b>Student ID:</b>
                    <%= esc(student.get("id")) %>
                </p>

                <p>
                    <b>Roll Number:</b>
                    <%= esc(student.get("roll")) %>
                </p>

                <p>
                    <b>Email:</b>
                    <%= esc(student.get("email")) %>
                </p>

                <p>
                    <b>Course:</b>
                    <%= esc(student.get("course")) %>
                </p>

                <h4>Uploaded Files</h4>

                <%
                    List<Map<String, Object>> files =
                        (List<Map<String, Object>>) student.get("files");
                %>

                <% if (files == null || files.isEmpty()) { %>

                    <p>No files uploaded for this student.</p>

                <% } else { %>

                    <% for (Map<String, Object> file : files) { %>

                        <div class="file-item">

                            <p>
                                <b>File Name:</b>
                                <%= esc(file.get("name")) %>
                            </p>

                            <p>
                                <b>File Type:</b>
                                <%= esc(file.get("type")) %>
                            </p>

                            <p>
                                <b>Upload Date:</b>
                                <%= esc(file.get("date")) %>
                            </p>

                            <% if ("image/jpeg".equals(file.get("type"))
                                || "image/png".equals(file.get("type"))) { %>

                                <img
                                    class="profile-image"
                                    src="<%= request.getContextPath() %>/file?id=<%= file.get("id") %>"
                                    alt="Student uploaded image">

                            <% } %>

                            <a target="_blank"
                               rel="noopener noreferrer"
                               href="<%= request.getContextPath() %>/file?id=<%= file.get("id") %>">
                                Open File
                            </a>

                        </div>

                    <% } %>

                <% } %>

            </div>

        <% } %>

    <% } %>

</div>

</body>
</html>
