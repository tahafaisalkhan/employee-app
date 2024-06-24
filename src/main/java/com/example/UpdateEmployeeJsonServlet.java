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
import java.sql.SQLException;

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

            String sqlEmployee = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, hire_date = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sqlEmployee)) 
            {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, email);
                statement.setString(4, hireDate);
                statement.setInt(5, Integer.parseInt(id));
                statement.executeUpdate();
            }

            String sqlDetails = "UPDATE employee_details SET address = ?, street = ?, province = ?, city = ?, country = ?, phone_number = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sqlDetails)) 
            {
                statement.setString(1, address);
                statement.setString(2, street);
                statement.setString(3, province);
                statement.setString(4, city);
                statement.setString(5, country);
                statement.setString(6, phoneNumber);
                statement.setInt(7, Integer.parseInt(id));
                statement.executeUpdate();
            }

            connection.commit();
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("message", "Employee updated successfully");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse.toString());
        } 
        catch (Exception e) {
            if (connection != null) 
            {
                try {
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
