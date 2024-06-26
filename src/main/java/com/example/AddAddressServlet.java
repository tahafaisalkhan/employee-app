package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AddAddressServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        String address = request.getParameter("address");
        String street = request.getParameter("street");
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String phoneNumber = request.getParameter("phoneNumber");
        String addressType = request.getParameter("addressType");

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) {
                String sql = "INSERT INTO employee_details (id, address, street, province, city, country, phone_number, address_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, employeeId);
                    statement.setString(2, address);
                    statement.setString(3, street);
                    statement.setString(4, province);
                    statement.setString(5, city);
                    statement.setString(6, country);
                    statement.setString(7, phoneNumber);
                    statement.setString(8, addressType);
                    statement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("employeeDetails");
    }
}
