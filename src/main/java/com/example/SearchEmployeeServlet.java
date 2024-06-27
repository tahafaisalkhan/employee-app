package com.example;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SearchEmployeeServlet extends HttpServlet 
{
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        HttpSession session = request.getSession();

        List<Employee> employees = (List<Employee>) session.getAttribute("employees");
        List<EmployeeDetail> employeeDetails = (List<EmployeeDetail>) session.getAttribute("employeeDetails");

        if (employees == null || employeeDetails == null) 
        {
            employees = new ArrayList<>();
            employeeDetails = new ArrayList<>();
            String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
            String jdbcUser = "root";
            String jdbcPassword = "tahafaisalkhan";

            try 
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
                try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) 
                {
                    String sql = "SELECT id, first_name, last_name, email, hire_date FROM employees";
                    try (PreparedStatement statement = connection.prepareStatement(sql);
                         ResultSet resultSet = statement.executeQuery()) 
                    {
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
                    }

                    String sqlDetails = "SELECT id, address, street, province, city, country, phone_number, address_type FROM employee_details";
                    try (PreparedStatement statement = connection.prepareStatement(sqlDetails);
                         ResultSet resultSet = statement.executeQuery())
                    {
                        while (resultSet.next()) 
                        {
                            EmployeeDetail employeeDetail = new EmployeeDetail();
                            employeeDetail.setId(resultSet.getInt("id"));
                            employeeDetail.setAddress(resultSet.getString("address"));
                            employeeDetail.setStreet(resultSet.getString("street"));
                            employeeDetail.setProvince(resultSet.getString("province"));
                            employeeDetail.setCity(resultSet.getString("city"));
                            employeeDetail.setCountry(resultSet.getString("country"));
                            employeeDetail.setPhoneNumber(resultSet.getString("phone_number"));
                            employeeDetail.setAddressType(resultSet.getString("address_type"));
                            employeeDetails.add(employeeDetail);
                        }
                    }
                }
                session.setAttribute("employees", employees);
                session.setAttribute("employeeDetails", employeeDetails);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }

        Employee result = null;
        List<EmployeeDetail> resultDetails = new ArrayList<>();
        for (Employee employee : employees) 
        {
            if (String.valueOf(employee.getId()).equals(search) || employee.getEmail().equalsIgnoreCase(search))
            {
                result = employee;
                break;
            }
        }

        if (result != null) 
        {
            for (EmployeeDetail detail : employeeDetails) 
            {
                if (detail.getId() == result.getId()) 
                {
                    resultDetails.add(detail);
                }
            }
        }

        request.setAttribute("result", result);
        request.setAttribute("resultDetails", resultDetails);
        request.setAttribute("searchPerformed", true);
        RequestDispatcher dispatcher = request.getRequestDispatcher("searchEmployee.jsp");
        dispatcher.forward(request, response);
    }
}
