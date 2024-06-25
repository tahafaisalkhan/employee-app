<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.Employee" %>
<%@ page import="com.example.EmployeeDetail" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Search Employees</title>
    <link rel="stylesheet" type="text/css" href="css/searchEmployee.css">
    <link rel="icon" type="image/png" href="images/favicon.png">
    <script>
        function toggleSidebar() 
        {
            const sidebar = document.querySelector('.sidebar');
            const mainContent = document.querySelector('.main-content');
            if (sidebar.classList.contains('active')) 
            {
                sidebar.classList.remove('active');
                mainContent.classList.remove('active');
            } 
            else 
            {
                sidebar.classList.add('active');
                mainContent.classList.add('active');
            }
        }
    </script>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div class="main-content">
        <div class="header">
            <div class="header-left">
                <button class="btn-toggle-sidebar" onclick="toggleSidebar()">☰</button>
                <form action="LogoutServlet" method="post" class="logout-form">
                    <button type="submit" class="btn-logout">Logout</button>
                </form>
            </div>
            <h1 class="header-title">Search Employee</h1>
        </div>
        <div class="center">
            <form action="SearchEmployeeServlet" method="POST">
                <label for="search">Enter Employee ID or Email:</label>
                <input type="text" id="search" name="search" required>
                <button type="submit" class="btn">Search</button>
            </form>
        </div>
        <div class="content">
            <%
                Employee result = (Employee) request.getAttribute("result");
                EmployeeDetail resultDetail = (EmployeeDetail) request.getAttribute("resultDetail");
                if (result != null && resultDetail != null) 
                {
            %>
                <div class="search-results">
                    <h2>Search Results</h2>
                    <h3>Employee Information</h3>
                    <p>Employee ID: <%= result.getId() %></p>
                    <p>First Name: <%= result.getFirstName() %></p>
                    <p>Last Name: <%= result.getLastName() %></p>
                    <p>Email: <%= result.getEmail() %></p>
                    <p>Hire Date: <%= result.getHireDate() %></p>

                    <h3>Employee Details</h3>
                    <p>Address: <%= resultDetail.getAddress() %></p>
                    <p>Street: <%= resultDetail.getStreet() %></p>
                    <p>Province: <%= resultDetail.getProvince() %></p>
                    <p>City: <%= resultDetail.getCity() %></p>
                    <p>Country: <%= resultDetail.getCountry() %></p>
                    <p>Phone Number: <%= resultDetail.getPhoneNumber() %></p>
                </div>
            <%
                } 
                else if (request.getAttribute("searchPerformed") != null) 
                {
            %>
                <div class="search-results">
                    <h2>Search Results</h2>
                    <p>No employee found with the provided ID or email.</p>
                </div>
            <%
                }
            %>
        </div>
    </div>
</body>
</html>
