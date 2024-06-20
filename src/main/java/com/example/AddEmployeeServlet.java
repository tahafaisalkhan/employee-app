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

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) 
            {
                String checkEmailSQL = "SELECT COUNT(*) FROM employees WHERE email = ?";
                try (PreparedStatement checkEmailStmt = connection.prepareStatement(checkEmailSQL)) 
                {
                    checkEmailStmt.setString(1, email);
                    ResultSet resultSet = checkEmailStmt.executeQuery();
                    if (resultSet.next() && resultSet.getInt(1) > 0) 
                    {
                        request.setAttribute("errorMessage", "Email already exists. Please use a different email.");
                        request.getRequestDispatcher("addEmployee.jsp").forward(request, response);
                        return;
                    }
                }

                String sql = "INSERT INTO employees (first_name, last_name, email, hire_date) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) 
                {
                    statement.setString(1, firstName);
                    statement.setString(2, lastName);
                    statement.setString(3, email);
                    statement.setString(4, hireDate);
                    statement.executeUpdate();
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        response.sendRedirect("DBServlet");
    }
}
