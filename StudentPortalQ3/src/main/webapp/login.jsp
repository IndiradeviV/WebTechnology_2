<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Portal Login</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f1f5f9;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }
        .login-box {
            background: white;
            padding: 30px;
            width: 320px;
            border-radius: 10px;
            box-shadow: 0 4px 15px #ccc;
        }
        h2 { text-align: center; color: #1e40af; }
        label { display: block; margin-top: 15px; }
        input {
            box-sizing: border-box;
            width: 100%;
            padding: 10px;
            margin-top: 6px;
        }
        button {
            width: 100%;
            padding: 11px;
            margin-top: 20px;
            border: none;
            background: #2563eb;
            color: white;
            cursor: pointer;
            border-radius: 5px;
        }
        button:hover { background: #1d4ed8; }
        .error { color: red; text-align: center; }
    </style>
</head>
<body>
    <div class="login-box">
        <h2>Student Portal</h2>

        <% if ("1".equals(request.getParameter("error"))) { %>
            <p class="error">Invalid username or password.</p>
        <% } %>

        <form action="<%= request.getContextPath() %>/login"
              method="post">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>

            <button type="submit">Login</button>
        </form>
    </div>
</body>
</html>
