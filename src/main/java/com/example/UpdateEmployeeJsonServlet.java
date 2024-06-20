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

public class UpdateEmployeeJsonServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                sb.append(line);
            }
        }

        String jsonString = sb.toString();
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();
        String firstName = jsonObject.get("firstName").getAsString();
        String lastName = jsonObject.get("lastName").getAsString();
        String email = jsonObject.get("email").getAsString();
        String hireDate = jsonObject.get("hireDate").getAsString();

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) 
            {
                String sql = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, hire_date = ? WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) 
                {
                    statement.setString(1, firstName);
                    statement.setString(2, lastName);
                    statement.setString(3, email);
                    statement.setString(4, hireDate);
                    statement.setInt(5, Integer.parseInt(id));
                    int rowsAffected = statement.executeUpdate();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    if (rowsAffected > 0) 
                    {
                        response.getWriter().write("{\"message\":\"Employee updated successfully.\"}");
                    } 
                    else 
                    {
                        response.getWriter().write("{\"message\":\"Employee not found.\"}");
                    }
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\":\"An error occurred: " + e.getMessage() + "\"}");
        }
    }
}
