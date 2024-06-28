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

public class AddAddressServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String employeeIdStr = request.getParameter("employeeId");
        String phoneNumberStr = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String street = request.getParameter("street");
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String addressType = request.getParameter("addressType");

        if (!addressType.equalsIgnoreCase("Home") && !addressType.equalsIgnoreCase("Office")) 
        {
            request.setAttribute("errorMessage", "Address Type must be 'Home' or 'Office'.");
            request.getRequestDispatcher("addAddress.jsp").forward(request, response);
            return;
        }
        if (!province.matches("[a-zA-Z ]+") || !city.matches("[a-zA-Z ]+") || !country.matches("[a-zA-Z ]+")) 
        {
            request.setAttribute("errorMessage", "Province, City, and Country must contain only alphabetic characters.");
            request.getRequestDispatcher("addAddress.jsp").forward(request, response);
            return;
        }

        int employeeId = -1;
        try 
        {
            employeeId = Integer.parseInt(employeeIdStr);
        } 
        catch (NumberFormatException e) 
        {
            request.setAttribute("errorMessage", "Employee ID must be a number.");
            request.getRequestDispatcher("addAddress.jsp").forward(request, response);
            return;
        }

        long phoneNumber = -1;
        try 
        {
            phoneNumber = Long.parseLong(phoneNumberStr);
        } 
        catch (NumberFormatException e) 
        {
            request.setAttribute("errorMessage", "Phone Number must be a number.");
            request.getRequestDispatcher("addAddress.jsp").forward(request, response);
            return;
        }

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) 
            {
                String checkEmployeeSql = "SELECT COUNT(*) FROM employees WHERE id = ?";
                try (PreparedStatement checkStatement = connection.prepareStatement(checkEmployeeSql)) 
                {
                    checkStatement.setInt(1, employeeId);
                    try (ResultSet resultSet = checkStatement.executeQuery()) 
                    {
                        if (resultSet.next() && resultSet.getInt(1) == 0) 
                        {
                            request.setAttribute("errorMessage", "Employee ID does not exist.");
                            request.getRequestDispatcher("addAddress.jsp").forward(request, response);
                            return;
                        }
                    }
                }

                String sql = "INSERT INTO employee_details (id, address, street, province, city, country, phone_number, address_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) 
                {
                    statement.setInt(1, employeeId);
                    statement.setString(2, address);
                    statement.setString(3, street);
                    statement.setString(4, province);
                    statement.setString(5, city);
                    statement.setString(6, country);
                    statement.setLong(7, phoneNumber);
                    statement.setString(8, addressType);
                    statement.executeUpdate();
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while adding the address.");
            request.getRequestDispatcher("addAddress.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("employeeDetails");
    }
}
