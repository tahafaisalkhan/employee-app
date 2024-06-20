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
import java.sql.SQLException;

public class MultiThreadedEmployeeFetcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int numberOfThreads = 3; 
        for (int i = 1; i <= numberOfThreads; i++) 
        {
            Thread thread = new EmployeeFetcherThread(i);
            thread.start();
        }

        response.getWriter().write("Started fetching employee data with multiple threads. Check the console for output.");
    }

    private static class EmployeeFetcherThread extends Thread 
    {
        private int threadNumber;

        public EmployeeFetcherThread(int threadNumber) 
        {
            this.threadNumber = threadNumber;
        }

        @Override
        public void run() 
        {
            try (Connection connection = getConnection()) 
            {
                String sql = "SELECT * FROM employees";
                try (PreparedStatement statement = connection.prepareStatement(sql)) 
                {
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) 
                    {
                        int id = resultSet.getInt("id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        String email = resultSet.getString("email");
                        String hireDate = resultSet.getString("hire_date");

                        System.out.printf("Thread %d: ID: %d, First Name: %s, Last Name: %s, Email: %s, Hire Date: %s%n",
                                threadNumber, id, firstName, lastName, email, hireDate);
                    }
                }
            } 
            catch (SQLException | ClassNotFoundException e) 
            {
                e.printStackTrace();
            }
        }

        private Connection getConnection() throws SQLException, ClassNotFoundException 
        {
            String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
            String jdbcUser = "root";
            String jdbcPassword = "tahafaisalkhan";
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
        }
    }
}
