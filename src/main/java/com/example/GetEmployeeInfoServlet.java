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
import java.sql.Timestamp;
import java.time.Instant;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GetEmployeeInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) 
            {
                String expiredTokenSql = "SELECT admin_id FROM tokens WHERE token = ? AND expires_at <= ?";
                try (PreparedStatement expiredTokenStmt = connection.prepareStatement(expiredTokenSql)) {
                    expiredTokenStmt.setString(1, token);
                    expiredTokenStmt.setTimestamp(2, Timestamp.from(Instant.now()));
                    try (ResultSet expiredTokenResult = expiredTokenStmt.executeQuery()) {
                        if (expiredTokenResult.next()) {
                            JsonObject jsonResponse = new JsonObject();
                            jsonResponse.addProperty("error", "Token has expired.");
                            response.setContentType("application/json");
                            response.getWriter().write(jsonResponse.toString());
                            return;
                        }
                    }
                }

                String tokenSql = "SELECT admin_id FROM tokens WHERE token = ? AND expires_at > ?";
                try (PreparedStatement tokenStmt = connection.prepareStatement(tokenSql)) 
                {
                    tokenStmt.setString(1, token);
                    tokenStmt.setTimestamp(2, Timestamp.from(Instant.now()));
                    try (ResultSet tokenResult = tokenStmt.executeQuery()) 
                    {
                        if (tokenResult.next()) 
                        {
                            int adminId = tokenResult.getInt("admin_id");

                            JsonArray employeeData = new JsonArray();

                            String employeeSql = "SELECT * FROM employees";
                            try (PreparedStatement employeeStmt = connection.prepareStatement(employeeSql);
                                 ResultSet employeeResult = employeeStmt.executeQuery()) 
                            {
                                while (employeeResult.next()) 
                                {
                                    JsonObject employee = new JsonObject();
                                    employee.addProperty("id", employeeResult.getInt("id"));
                                    employee.addProperty("first_name", employeeResult.getString("first_name"));
                                    employee.addProperty("last_name", employeeResult.getString("last_name"));
                                    employee.addProperty("email", employeeResult.getString("email"));
                                    employee.addProperty("hire_date", employeeResult.getDate("hire_date").toString());
                                    employeeData.add(employee);
                                }
                            }

                            String addressSql = "SELECT * FROM employee_details";
                            try (PreparedStatement addressStmt = connection.prepareStatement(addressSql);
                                 ResultSet addressResult = addressStmt.executeQuery()) 
                            {
                                while (addressResult.next()) {
                                    JsonObject detail = new JsonObject();
                                    detail.addProperty("id", addressResult.getInt("id"));
                                    detail.addProperty("address", addressResult.getString("address"));
                                    detail.addProperty("street", addressResult.getString("street"));
                                    detail.addProperty("province", addressResult.getString("province"));
                                    detail.addProperty("city", addressResult.getString("city"));
                                    detail.addProperty("country", addressResult.getString("country"));
                                    detail.addProperty("phone_number", addressResult.getString("phone_number"));
                                    detail.addProperty("address_type", addressResult.getString("address_type"));
                                    employeeData.add(detail);
                                }
                            }

                            response.setContentType("application/json");
                            response.getWriter().write(employeeData.toString());
                        } 
                        else {
                            JsonObject jsonResponse = new JsonObject();
                            jsonResponse.addProperty("error", "Token is incorrect.");
                            response.setContentType("application/json");
                            response.getWriter().write(jsonResponse.toString());
                        }
                    }
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("error", "An error occurred while processing your request.");
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());
        }
    }
}
