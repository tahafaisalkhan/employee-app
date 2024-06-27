<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="css/login.css">
    <link rel="icon" type="image/png" href="images/favicon.png">
</head>
<body>
    <div class="container">
        <h1>Login</h1>
        <form action="LoginServlet" method="post">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required><br>

            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required><br>

            <button type="submit" class="btn-add">Login</button>
        </form>
        <div class="center">
            <form action="register.jsp" method="get">
                <button type="submit" class="btn-add">Register</button>
            </form>
        </div>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
        <% if (errorMessage != null) 
        { %>
            <div class="error-message"><%= errorMessage %></div>
        <% } %>
    </div>
</body>
</html>
