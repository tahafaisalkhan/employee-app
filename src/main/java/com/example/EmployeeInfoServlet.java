package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class EmployeeInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        System.out.println("Received request for employee with ID: " + id);

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        Map<String, String> employeeData = new HashMap<>();

        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) 
            {
                String sql = "SELECT * FROM employees WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) 
                {
                    statement.setInt(1, Integer.parseInt(id));
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) 
                    {
                        employeeData.put("id", String.valueOf(resultSet.getInt("id")));
                        employeeData.put("first_name", resultSet.getString("first_name"));
                        employeeData.put("last_name", resultSet.getString("last_name"));
                        employeeData.put("email", resultSet.getString("email"));
                        employeeData.put("hire_date", resultSet.getDate("hire_date").toString());
                    }
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = new Gson().toJson(employeeData);
        response.getWriter().write(json);
    }
}
