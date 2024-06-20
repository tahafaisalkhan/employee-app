package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class RemoveEmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        System.out.println("Received request to remove employee with email: " + email);

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) 
            {
                String sql = "DELETE FROM employees WHERE email = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) 
                {
                    statement.setString(1, email);
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) 
                    {
                        System.out.println("Employee with email " + email + " removed successfully.");
                    } 
                    else 
                    {
                        System.out.println("No employee found with email " + email);
                    }
                }
            }
        } 
        catch (Exception e) 
        {
            System.out.println("Error while removing employee with email " + email);
            e.printStackTrace();
        }

        response.sendRedirect("DBServlet");
    }
}
