package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;

public class AddEmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String hireDate = request.getParameter("hireDate");
        String address = request.getParameter("address");
        String street = request.getParameter("street");
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String phoneNumber = request.getParameter("phoneNumber");

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

        if (!emailPattern.matcher(email).matches()) 
        {
            request.setAttribute("errorMessage", "Invalid email format.");
            request.getRequestDispatcher("addEmployee.jsp").forward(request, response);
            return;
        }

        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) 
            
            {
                String checkEmailSQL = "SELECT COUNT(*) FROM employees WHERE email = ?";
                try (PreparedStatement checkEmailStmt = connection.prepareStatement(checkEmailSQL)) {
                    checkEmailStmt.setString(1, email);
                    ResultSet resultSet = checkEmailStmt.executeQuery();
                    if (resultSet.next() && resultSet.getInt(1) > 0) 
                    {
                        request.setAttribute("errorMessage", "Email already exists. Please use a different email.");
                        request.getRequestDispatcher("addEmployee.jsp").forward(request, response);
                        return;
                    }
                }

                String insertEmployeeSQL = "INSERT INTO employees (first_name, last_name, email, hire_date) VALUES (?, ?, ?, ?)";
                try (PreparedStatement employeeStmt = connection.prepareStatement(insertEmployeeSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    employeeStmt.setString(1, firstName);
                    employeeStmt.setString(2, lastName);
                    employeeStmt.setString(3, email);
                    employeeStmt.setString(4, hireDate);
                    employeeStmt.executeUpdate();

                    int employeeId = 0;
                    try (ResultSet generatedKeys = employeeStmt.getGeneratedKeys()) 
                    {
                        if (generatedKeys.next()) 
                        {
                            employeeId = generatedKeys.getInt(1);
                        }
                    }

                    String insertDetailsSQL = "INSERT INTO employee_details (id, address, street, province, city, country, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement detailsStmt = connection.prepareStatement(insertDetailsSQL)) 
                    {
                        detailsStmt.setInt(1, employeeId);
                        detailsStmt.setString(2, address);
                        detailsStmt.setString(3, street);
                        detailsStmt.setString(4, province);
                        detailsStmt.setString(5, city);
                        detailsStmt.setString(6, country);
                        detailsStmt.setString(7, phoneNumber);
                        detailsStmt.executeUpdate();
                    }
                }
            }
            response.sendRedirect("DBServlet");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error adding employee: " + e.getMessage());
            request.getRequestDispatcher("addEmployee.jsp").forward(request, response);
        }
    }
}
