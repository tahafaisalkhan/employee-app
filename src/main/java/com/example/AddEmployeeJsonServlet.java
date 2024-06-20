package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AddEmployeeJsonServlet extends HttpServlet {
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
        Gson gson = new Gson();
        JsonObject jsonObject;

        try 
        {
            jsonObject = gson.fromJson(jsonString, JsonObject.class);
        } 
        catch (JsonSyntaxException e) 
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\":\"Invalid JSON format\"}");
            return;
        }

        if (jsonObject == null || 
        		!jsonObject.has("firstName") || 
        		!jsonObject.has("lastName") || 
        		!jsonObject.has("email") || 
        		!jsonObject.has("hireDate")) 
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error");
            return;
        }

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
            response.getWriter().write("Error");
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("message", "Employee added successfully");
        response.getWriter().write(jsonResponse.toString());
    }
}
