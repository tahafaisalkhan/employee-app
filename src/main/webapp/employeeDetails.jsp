<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.example.Employee" %>
<%@ page import="com.example.EmployeeDetail" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Details</title>
    <link rel="stylesheet" type="text/css" href="css/employeeDetail.css">
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
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Employee> employees = (List<Employee>) request.getAttribute("employees");
                        if (employees != null) {
                            for (Employee employee : employees) {
                    %>
                                <tr>
                                    <td><a href="EmployeeAddressServlet?id=<%= employee.getId() %>"><%= employee.getId() %></a></td>
                                    <td><%= employee.getFirstName() %></td>
                                    <td><%= employee.getLastName() %></td>
                                    <td><%= employee.getEmail() %></td>
                                    <td><%= employee.getHireDate() %></td>
                                </tr>
                    <%
                            }
                        } 
                        else 
                        {
                    %>
                            <tr>
                                <td colspan="5">No data available</td>
                            </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
            <div class="center">
                <form action="addAddress.jsp" method="get" style="margin-top: 10px;">
                    <button type="submit" class="btn-add">Add Address</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
