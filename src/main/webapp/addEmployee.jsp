<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Employee</title>
    <link rel="stylesheet" type="text/css" href="css/addPage.css">
    <link rel="icon" type="image/png" href="images/favicon.png">
</head>
<body>
    <h1>Add Employee</h1>
    <div class="container">
        <form action="AddEmployeeServlet" method="post">
            <label for="firstName">First Name:</label>
            <input type="text" id="firstName" name="firstName" required><br>
            
            <label for="lastName">Last Name:</label>
            <input type="text" id="lastName" name="lastName" required><br>
            
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required><br>
            
            <label for="hireDate">Hire Date (YYYY-MM-DD):</label>
            <input type="date" id="hireDate" name="hireDate" required><br>
            
            <button type="submit">Add Employee</button>
        </form>
        <div class="center">
            <form action="DBServlet" method="get">
                <button type="submit" class="button">Back to Employee List</button>
            </form>
        </div>
        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
        %>
            <p style="color: red;"><%= errorMessage %></p>
        <%
            }
        %>
    </div>
</body>
</html>
