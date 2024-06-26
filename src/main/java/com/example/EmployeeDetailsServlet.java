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

                String employeeSQL = "SELECT * FROM employees";
                ResultSet employeeResultSet = statement.executeQuery(employeeSQL);

                while (employeeResultSet.next()) {
                    Employee employee = new Employee();
                    employee.setId(employeeResultSet.getInt("id"));
                    employee.setFirstName(employeeResultSet.getString("first_name"));
                    employee.setLastName(employeeResultSet.getString("last_name"));
                    employee.setEmail(employeeResultSet.getString("email"));
                    employee.setHireDate(employeeResultSet.getDate("hire_date").toString());
                    employees.add(employee);
                }
                employeeResultSet.close();

                String addressSQL = "SELECT * FROM employee_details";
                ResultSet addressResultSet = statement.executeQuery(addressSQL);

                while (addressResultSet.next()) {
                    EmployeeDetail detail = new EmployeeDetail();
//                    detail.setAddressId(addressResultSet.getInt("address_id"));
                    detail.setId(addressResultSet.getInt("id"));
                    detail.setAddress(addressResultSet.getString("address"));
                    detail.setStreet(addressResultSet.getString("street"));
                    detail.setProvince(addressResultSet.getString("province"));
                    detail.setCity(addressResultSet.getString("city"));
                    detail.setCountry(addressResultSet.getString("country"));
                    detail.setPhoneNumber(addressResultSet.getString("phone_number"));
                    detail.setAddressType(addressResultSet.getString("address_type"));
                    employeeDetails.add(detail);
                }
                addressResultSet.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("employees", employees);
        request.setAttribute("employeeDetails", employeeDetails);
        System.out.println("Set employees and employeeDetails attributes with data.");

        request.getRequestDispatcher("/employeeDetails.jsp").forward(request, response);
        System.out.println("Forwarded to employeeDetails.jsp.");
    }
}
