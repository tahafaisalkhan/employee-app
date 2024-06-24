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

public class RemoveEmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        System.out.println("Received request to remove employee with email: " + email);

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        Connection connection = null;

        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
            connection.setAutoCommit(false);

            String getIdSQL = "SELECT id FROM employees WHERE email = ?";
            int employeeId = -1;
            try (PreparedStatement getIdStmt = connection.prepareStatement(getIdSQL)) 
            {
                getIdStmt.setString(1, email);
                ResultSet resultSet = getIdStmt.executeQuery();
                if (resultSet.next()) 
                {
                    employeeId = resultSet.getInt("id");
                }
                else 
                {
                    System.out.println("No employee found with email " + email);
                    response.sendRedirect("DBServlet");
                    return;
                }
            }

            String deleteDetailsSQL = "DELETE FROM employee_details WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteDetailsSQL)) 
            {
                statement.setInt(1, employeeId);
                statement.executeUpdate();
            }

            String deleteEmployeeSQL = "DELETE FROM employees WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteEmployeeSQL)) 
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

            connection.commit();
        } 
        catch (Exception e) 
        {
            if (connection != null) 
            {
                try 
                {
                    connection.rollback();
                } 
                catch (Exception rollbackEx) 
                {
                    rollbackEx.printStackTrace();
                }
            }
            System.out.println("Error while removing employee with email " + email);
            e.printStackTrace();
        } 
        finally 
        {
            if (connection != null) 
            {
                try 
                {
                    connection.setAutoCommit(true);
                    connection.close();
                } 
                catch (Exception closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }

        response.sendRedirect("DBServlet");
    }
}
