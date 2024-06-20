<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Page</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
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
            <h1 class="header-title">Employee List</h1>
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
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Map<String, String>> employees = (List<Map<String, String>>) request.getAttribute("employees");
                        if (employees != null) {
                            for (Map<String, String> employee : employees) {
                    %>
                                <tr>
                                    <td><%= employee.get("id") %></td>
                                    <td><%= employee.get("first_name") %></td>
                                    <td><%= employee.get("last_name") %></td>
                                    <td><%= employee.get("email") %></td>
                                    <td><%= employee.get("hire_date") %></td>
                                    <td>
                                        <form action="RemoveEmployeeServlet" method="post" onsubmit="return confirm('Are you sure you want to remove this employee?')">
                                            <input type="hidden" name="email" value="<%= employee.get("email") %>">
                                            <button type="submit" class="btn-remove">Remove</button>
                                        </form>
                                    </td>
                                </tr>
                    <%
                            }
                        } 
                        else {
                    %>
                            <tr>
                                <td colspan="6">No data available</td>
                            </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
            <div class="center">
                <form action="addEmployee.jsp" method="get">
                    <button type="submit" class="btn-add">Add Employee</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
