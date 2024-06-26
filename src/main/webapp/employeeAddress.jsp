<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.EmployeeDetail" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Addresses</title>
    <link rel="stylesheet" type="text/css" href="css/employeeAddress.css">
    <link rel="icon" type="image/png" href="images/favicon.png">
    <script>
        function toggleSidebar() {
            const sidebar = document.querySelector('.sidebar');
            const mainContent = document.querySelector('.main-content');
            if (sidebar.classList.contains('active')) {
                sidebar.classList.remove('active');
                mainContent.classList.remove('active');
            } else {
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
            <h1 class="header-title">Employee Addresses</h1>
        </div>
        <div class="content">
            <h2>Addresses</h2>
            <table class="styled-table">
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
                        List<EmployeeDetail> employeeDetails = (List<EmployeeDetail>) request.getAttribute("employeeDetails");
                        if (employeeDetails != null) {
                            for (EmployeeDetail detail : employeeDetails) {
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
                        } else {
                    %>
                            <tr>
                                <td colspan="7">No data available</td>
                            </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
            <div class="center">
                <a href="employeeDetails" class="btn">Back</a>
            </div>
        </div>
    </div>
</body>
</html>
