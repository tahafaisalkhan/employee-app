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
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddEmployeeJsonServlet extends HttpServlet
{
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
            !jsonObject.has("hireDate") ||
            !jsonObject.has("address") ||
            !jsonObject.has("street") ||
            !jsonObject.has("province") ||
            !jsonObject.has("city") ||
            !jsonObject.has("country") ||
            !jsonObject.has("phoneNumber")) 
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\":\"Missing required fields\"}");
            return;
        }

        String firstName = jsonObject.get("firstName").getAsString();
        String lastName = jsonObject.get("lastName").getAsString();
        String email = jsonObject.get("email").getAsString();
        String hireDate = jsonObject.get("hireDate").getAsString();
        String address = jsonObject.get("address").getAsString();
        String street = jsonObject.get("street").getAsString();
        String province = jsonObject.get("province").getAsString();
        String city = jsonObject.get("city").getAsString();
        String country = jsonObject.get("country").getAsString();
        String phoneNumber = jsonObject.get("phoneNumber").getAsString();

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        Connection connection = null;

        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
            connection.setAutoCommit(false);

            String sqlEmployee = "INSERT INTO employees (first_name, last_name, email, hire_date) VALUES (?, ?, ?, ?)";
            int employeeId;
            try (PreparedStatement statement = connection.prepareStatement(sqlEmployee, PreparedStatement.RETURN_GENERATED_KEYS)) 
            {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, email);
                statement.setString(4, hireDate);
                statement.executeUpdate();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) 
                {
                    if (generatedKeys.next()) 
                    {
                        employeeId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve employee ID.");
                    }
                }
            }

            String sqlDetails = "INSERT INTO employee_details (id, address, street, province, city, country, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sqlDetails)) 
            {
                statement.setInt(1, employeeId);
                statement.setString(2, address);
                statement.setString(3, street);
                statement.setString(4, province);
                statement.setString(5, city);
                statement.setString(6, country);
                statement.setString(7, phoneNumber);
                statement.executeUpdate();
            }

            connection.commit();
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("message", "Employee added successfully");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse.toString());
        } 
        catch (Exception e) 
        {
            if (connection != null) 
            {
                try 
                {
                    connection.rollback();
                } 
                catch (SQLException rollbackEx) 
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
                catch (SQLException closeEx) 
                {
                    closeEx.printStackTrace();
                }
            }
        }
    }
}
