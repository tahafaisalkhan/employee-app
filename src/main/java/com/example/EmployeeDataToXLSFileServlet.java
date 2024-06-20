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

public class EmployeeDataToXLSFileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try 
        {
            String desktopPath = System.getProperty("user.home") + "/Desktop/employee_data.xls";
            writeEmployeeDataToFile(desktopPath);
            response.getWriter().write("Employee data has been written to " + desktopPath);
        } 
        catch (SQLException | ClassNotFoundException e) 
        {
            e.printStackTrace();
            response.getWriter().write("An error occurred: " + e.getMessage());
        }
    }

    private void writeEmployeeDataToFile(String filename) throws SQLException, ClassNotFoundException, IOException 
    {
        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) 
        {
            
            String sql = "SELECT * FROM employees";
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

                    String line = String.join("\t", String.valueOf(id), firstName, lastName, email, hireDate);
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }
}
