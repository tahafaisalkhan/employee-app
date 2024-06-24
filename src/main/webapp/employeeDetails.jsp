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
                        List<Employee> employees = (List<Employee>) request.getAttribute("employees");
                        List<EmployeeDetail> employeeDetails = (List<EmployeeDetail>) request.getAttribute("employeeDetails");
                        if (employees != null && employeeDetails != null) 
                        {
                            for (int i = 0; i < employees.size(); i++) 
                            {
                                Employee employee = employees.get(i);
                                EmployeeDetail detail = employeeDetails.get(i);
                    %>
                                <tr>
                                    <td><%= employee.getId() %></td>
                                    <td><%= employee.getFirstName() %></td>
                                    <td><%= employee.getLastName() %></td>
                                    <td><%= employee.getEmail() %></td>
                                    <td><%= employee.getHireDate() %></td>
                                    <td><%= detail.getAddress() %></td>
                                    <td><%= detail.getStreet() %></td>
                                    <td><%= detail.getProvince() %></td>
                                    <td><%= detail.getCity() %></td>
                                    <td><%= detail.getCountry() %></td>
                                    <td><%= detail.getPhoneNumber() %></td>
                                </tr>
                    <%
                            }
                        } 
                        else 
                        {
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
