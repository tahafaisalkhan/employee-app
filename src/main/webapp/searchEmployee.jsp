<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.Employee" %>
<%@ page import="com.example.EmployeeDetail" %>
<%@ page import="java.util.List" %>
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
                List<EmployeeDetail> resultDetails = (List<EmployeeDetail>) request.getAttribute("resultDetails");
                if (result != null && resultDetails != null && !resultDetails.isEmpty()) 
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

                    <h3>Employee Addresses</h3>
                    <table>
                        <thead>
                            <tr>
                                <th>Address</th>
                                <th>Street</th>
                                <th>Province</th>
                                <th>City</th>
                                <th>Country</th>
                                <th>Phone Number</th>
                                <th>Address Type</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (EmployeeDetail detail : resultDetails) {
                            %>
                                <tr>
                                    <td><%= detail.getAddress() %></td>
                                    <td><%= detail.getStreet() %></td>
                                    <td><%= detail.getProvince() %></td>
                                    <td><%= detail.getCity() %></td>
                                    <td><%= detail.getCountry() %></td>
                                    <td><%= detail.getPhoneNumber() %></td>
                                    <td><%= detail.getAddressType() %></td>
                                </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
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
