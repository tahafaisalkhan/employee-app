package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDataToFileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try 
        {
            String desktopPath = System.getProperty("user.home") + "/Desktop/employee_data.txt";
            writeEmployeeDataToFile(desktopPath);
            response.getWriter().write("Employee data has been written to " + desktopPath);
        } 
        catch (SQLException | ClassNotFoundException e) 
        {
            e.printStackTrace();
            response.getWriter().write("An error occurred: " + e.getMessage());
        }
    }

    private void writeEmployeeDataToFile(String filename) throws SQLException, ClassNotFoundException, IOException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) 
        
        {

            String sql = "SELECT e.id, e.first_name, e.last_name, e.email, e.hire_date, " +
                         "d.address, d.street, d.province, d.city, d.country, d.phone_number " +
                         "FROM employees e LEFT JOIN employee_details d ON e.id = d.id";

            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) 
            {

                while (resultSet.next()) 
                {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");
                    String hireDate = resultSet.getString("hire_date");
                    String address = resultSet.getString("address");
                    String street = resultSet.getString("street");
                    String province = resultSet.getString("province");
                    String city = resultSet.getString("city");
                    String country = resultSet.getString("country");
                    String phoneNumber = resultSet.getString("phone_number");

                    String line = String.join("|", String.valueOf(id), firstName, lastName, email, hireDate, 
                                               address, street, province, city, country, phoneNumber);
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }
}
