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
import java.util.List;

public class DBServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DBServlet doGet method called");

        List<Employee> employees = new ArrayList<>();

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
                 Statement statement = connection.createStatement()) 
            {

                String selectSQL = "SELECT * FROM employees";
                ResultSet resultSet = statement.executeQuery(selectSQL);

                while (resultSet.next()) 
                {
                    Employee employee = new Employee();
                    employee.setId(resultSet.getInt("id"));
                    employee.setFirstName(resultSet.getString("first_name"));
                    employee.setLastName(resultSet.getString("last_name"));
                    employee.setEmail(resultSet.getString("email"));
                    employee.setHireDate(resultSet.getDate("hire_date").toString());

                    employees.add(employee);
                }
                resultSet.close();
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        System.out.println("Employees data being sent to JSP: " + employees);

        request.setAttribute("employees", employees);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}





//// Create database if not exists
//String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS Employees";
//statement.executeUpdate(createDatabaseSQL);
//System.out.println("Database 'Employees' created successfully!");
//
//// Switch to the newly created database
//statement.execute("USE Employees");
//
//// Drop table if exists
//String dropTableSQL = "DROP TABLE IF EXISTS employees";
//statement.executeUpdate(dropTableSQL);
//System.out.println("Table 'employees' dropped successfully!");
//
//// Create table
//String createTableSQL = "CREATE TABLE employees ("
//      + "id INT AUTO_INCREMENT PRIMARY KEY, "
//      + "first_name VARCHAR(50) NOT NULL, "
//      + "last_name VARCHAR(50) NOT NULL, "
//      + "email VARCHAR(100) NOT NULL, "
//      + "hire_date DATE NOT NULL"
//      + ")";
//statement.executeUpdate(createTableSQL);
//System.out.println("Table 'employees' created successfully!");
//
//// Insert data
//String insertDataSQL = "INSERT INTO employees (first_name, last_name, email, hire_date) VALUES "
//      + "('John', 'Doe', 'john.doe@example.com', '2022-01-01'), "
//      + "('Jane', 'Smith', 'jane.smith@example.com', '2022-02-01'), "
//      + "('Alice', 'Johnson', 'alice.johnson@example.com', '2022-03-01'), "
//      + "('Bob', 'Williams', 'bob.williams@example.com', '2022-04-01'), "
//      + "('Carol', 'Jones', 'carol.jones@example.com', '2022-05-01'), "
//      + "('David', 'Brown', 'david.brown@example.com', '2022-06-01'), "
//      + "('Eve', 'Davis', 'eve.davis@example.com', '2022-07-01'), "
//      + "('Frank', 'Miller', 'frank.miller@example.com', '2022-08-01'), "
//      + "('Grace', 'Wilson', 'grace.wilson@example.com', '2022-09-01'), "
//      + "('Henry', 'Moore', 'henry.moore@example.com', '2022-10-01'), "
//      + "('Ivy', 'Taylor', 'ivy.taylor@example.com', '2022-11-01'), "
//      + "('Jack', 'Anderson', 'jack.anderson@example.com', '2022-12-01')";
//statement.executeUpdate(insertDataSQL);
//System.out.println("Data inserted successfully!");
