package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DeleteEmployeeJsonServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        String jsonString = sb.toString();
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        Connection connection = null;

        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
            connection.setAutoCommit(false);
            
            String deleteDetailsSQL = "DELETE FROM employee_details WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteDetailsSQL)) 
            {
                statement.setInt(1, Integer.parseInt(id));
                statement.executeUpdate();
            }

            String deleteEmployeeSQL = "DELETE FROM employees WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteEmployeeSQL)) 
            {
                statement.setInt(1, Integer.parseInt(id));
                int rowsAffected = statement.executeUpdate();
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                if (rowsAffected > 0) 
                {
                    response.getWriter().write("{\"message\":\"Employee deleted successfully.\"}");
                } 
                else 
                {
                    response.getWriter().write("{\"message\":\"Employee not found.\"}");
                }
            }

            connection.commit();
        } catch (Exception e)
        {
            if (connection != null) 
            {
                try 
                {
                    connection.rollback();
                } catch (Exception rollbackEx) 
                {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\":\"An error occurred: " + e.getMessage() + "\"}");
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
                catch (Exception closeEx) 
                {
                    closeEx.printStackTrace();
                }
            }
        }
    }
}
