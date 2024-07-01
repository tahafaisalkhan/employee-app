<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="sidebar">
    <h2>Menu</h2>
    <a href="home.jsp">Home</a>
    <% 
        String role = (String) session.getAttribute("role");
        if (role == null) 
        {
            role = "";
        }
    %>
    
    <% if (role.equals("Admin") || role.equals("Manager")) 
    { %>
        <a href="searchEmployee.jsp">Search Employee</a>
        <a href="manageEmployees.jsp">Manage Employees</a>
        <a href="fundTransfer.jsp">Fund Transfer</a>
    <% } %>
    
    <% if (role.equals("Admin") || role.equals("Observer") || role.equals("Manager") || role.equals("Analyst")) 
    { %>
        <a href="employeeDetails">Employee Details</a>
    <% } %>
    
    <% if (role.equals("Admin")) 
    { %>
        <a href="admin">Admin List</a>
    <% } %>
    
    <% if (role.equals("Admin") || role.equals("Analyst")) 
    { %>
        <a href="generateFile.jsp">Generate Files</a>
        <a href="uploadFiles.jsp">Upload Files</a>
    <% } %>
</div>
