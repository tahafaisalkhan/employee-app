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

public class EmployeeDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Employee> employees = new ArrayList<>();
        List<EmployeeDetail> employeeDetails = new ArrayList<>();

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully.");

            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
                 Statement statement = connection.createStatement()) {

                System.out.println("Connected to the database successfully.");

                String joinSQL = "SELECT e.id, e.first_name, e.last_name, e.email, e.hire_date, " +
                                 "d.address, d.street, d.province, d.city, d.country, d.phone_number " +
                                 "FROM employees e " +
                                 "LEFT JOIN employee_details d ON e.id = d.id";
                ResultSet resultSet = statement.executeQuery(joinSQL);
                System.out.println("Fetched data from joined tables.");

                while (resultSet.next()) 
                {
                    Employee employee = new Employee();
                    employee.setId(resultSet.getInt("id"));
                    employee.setFirstName(resultSet.getString("first_name"));
                    employee.setLastName(resultSet.getString("last_name"));
                    employee.setEmail(resultSet.getString("email"));
                    employee.setHireDate(resultSet.getDate("hire_date").toString());

                    EmployeeDetail detail = new EmployeeDetail();
                    detail.setId(resultSet.getInt("id"));
                    detail.setAddress(resultSet.getString("address"));
                    detail.setStreet(resultSet.getString("street"));
                    detail.setProvince(resultSet.getString("province"));
                    detail.setCity(resultSet.getString("city"));
                    detail.setCountry(resultSet.getString("country"));
                    detail.setPhoneNumber(resultSet.getString("phone_number"));

                    employees.add(employee);
                    employeeDetails.add(detail);
                }
                resultSet.close();
                System.out.println("All employee data processed.");
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        request.setAttribute("employees", employees);
        request.setAttribute("employeeDetails", employeeDetails);
        System.out.println("Set employees and employeeDetails attributes with data.");

        request.getRequestDispatcher("/employeeDetails.jsp").forward(request, response);
        System.out.println("Forwarded to employeeDetails.jsp.");
    }
}
