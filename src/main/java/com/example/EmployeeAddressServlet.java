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

public class EmployeeAddressServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeId = Integer.parseInt(request.getParameter("id"));
        List<EmployeeDetail> employeeDetails = new ArrayList<>();

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
                 Statement statement = connection.createStatement()) {

                String sql = "SELECT * FROM employee_details WHERE id = " + employeeId;
                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    EmployeeDetail detail = new EmployeeDetail();
                    detail.setId(resultSet.getInt("id"));
                    detail.setAddress(resultSet.getString("address"));
                    detail.setStreet(resultSet.getString("street"));
                    detail.setProvince(resultSet.getString("province"));
                    detail.setCity(resultSet.getString("city"));
                    detail.setCountry(resultSet.getString("country"));
                    detail.setPhoneNumber(resultSet.getString("phone_number"));
                    detail.setAddressType(resultSet.getString("address_type"));

                    employeeDetails.add(detail);
                }
                resultSet.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("employeeDetails", employeeDetails);
        request.getRequestDispatcher("/employeeAddress.jsp").forward(request, response);
    }
}
