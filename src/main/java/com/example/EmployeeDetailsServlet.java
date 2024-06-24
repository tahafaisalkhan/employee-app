package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Map<String, String>> employeeDetails = new ArrayList<>();

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully.");
            
            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
                 Statement statement = connection.createStatement()) {

                System.out.println("Connected to the database successfully.");

                String joinSQL = "SELECT e.id, e.first_name, e.last_name, e.email, e.hire_date, " +
                                 "d.address, d.street, d.province, d.city, d.country, d.phone_number " +
                                 "FROM employees e " +
                                 "JOIN employee_details d ON e.id = d.id";
                ResultSet resultSet = statement.executeQuery(joinSQL);
                System.out.println("Fetched data from joined tables.");

                while (resultSet.next()) 
                {
                    Map<String, String> row = new HashMap<>();
                    row.put("id", String.valueOf(resultSet.getInt("id")));
                    row.put("first_name", resultSet.getString("first_name"));
                    row.put("last_name", resultSet.getString("last_name"));
                    row.put("email", resultSet.getString("email"));
                    row.put("hire_date", resultSet.getDate("hire_date").toString());
                    row.put("address", resultSet.getString("address"));
                    row.put("street", resultSet.getString("street"));
                    row.put("province", resultSet.getString("province"));
                    row.put("city", resultSet.getString("city"));
                    row.put("country", resultSet.getString("country"));
                    row.put("phone_number", resultSet.getString("phone_number"));
                    
                    System.out.println("Processed employee: " + resultSet.getString("first_name"));
                    
                    employeeDetails.add(row);
                }
                resultSet.close();
                System.out.println("All employee data processed.");
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        request.setAttribute("employeeDetails", employeeDetails);
        System.out.println("Set employeeDetails attribute with data: " + employeeDetails);

        request.getRequestDispatcher("/employeeDetails.jsp").forward(request, response);
        System.out.println("Forwarded to employeeDetails.jsp.");
    }
}
