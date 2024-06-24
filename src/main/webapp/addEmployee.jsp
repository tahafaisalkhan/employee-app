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
    <div class="header-container">
        <h1>Add Employee</h1>
    </div>
    <div class="container">
        <div class="form-container">
            <form action="AddEmployeeServlet" method="post">
                <label for="firstName">First Name:</label>
                <input type="text" id="firstName" name="firstName" required><br>
                
                <label for="lastName">Last Name:</label>
                <input type="text" id="lastName" name="lastName" required><br>
                
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required><br>
                
                <label for="hireDate">Hire Date (YYYY-MM-DD):</label>
                <input type="date" id="hireDate" name="hireDate" required><br>
                
                <label for="address">Address:</label>
                <input type="text" id="address" name="address" required><br>
                
                <label for="street">Street:</label>
                <input type="text" id="street" name="street" required><br>
                
                <label for="province">Province:</label>
                <input type="text" id="province" name="province" required><br>
                
                <label for="city">City:</label>
                <input type="text" id="city" name="city" required><br>
                
                <label for="country">Country:</label>
                <input type="text" id="country" name="country" required><br>
                
                <label for="phoneNumber">Phone Number:</label>
                <input type="text" id="phoneNumber" name="phoneNumber" required><br>
                
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
                <p><%= errorMessage %></p>
            <%
                }
            %>
        </div>
    </div>
</body>
</html>
