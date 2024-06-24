<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Details</title>
    <link rel="stylesheet" type="text/css" href="css/employeeDetail.css">
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
            <h1 class="header-title">Employee Details</h1>
        </div>
        <div class="content">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Email</th>
                        <th>Hire Date</th>
                        <th>Address</th>
                        <th>Street</th>
                        <th>Province</th>
                        <th>City</th>
                        <th>Country</th>
                        <th>Phone Number</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Map<String, String>> employeeDetails = (List<Map<String, String>>) request.getAttribute("employeeDetails");
                        if (employeeDetails != null) {
                            for (Map<String, String> employee : employeeDetails) {
                    %>
                                <tr>
                                    <td><%= employee.get("id") %></td>
                                    <td><%= employee.get("first_name") %></td>
                                    <td><%= employee.get("last_name") %></td>
                                    <td><%= employee.get("email") %></td>
                                    <td><%= employee.get("hire_date") %></td>
                                    <td><%= employee.get("address") %></td>
                                    <td><%= employee.get("street") %></td>
                                    <td><%= employee.get("province") %></td>
                                    <td><%= employee.get("city") %></td>
                                    <td><%= employee.get("country") %></td>
                                    <td><%= employee.get("phone_number") %></td>
                                </tr>
                    <%
                            }
                        } else {
                    %>
                            <tr>
                                <td colspan="11">No data available</td>
                            </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
