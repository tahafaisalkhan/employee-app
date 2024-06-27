package com.example;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SendEmployeeDetailsJsonServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeId = Integer.parseInt(request.getParameter("id"));
        List<EmployeeDetail> employeeDetails = getEmployeeDetailsById(employeeId);

        if (employeeDetails != null && !employeeDetails.isEmpty()) 
        {
            String json = new Gson().toJson(employeeDetails);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } 
        else 
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found");
        }
    }

    private List<EmployeeDetail> getEmployeeDetailsById(int id) 
    {
        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";
        List<EmployeeDetail> employeeDetails = new ArrayList<>();

        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) 
            {
                String sql = "SELECT id, address, street, province, city, country, phone_number, address_type FROM employee_details WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) 
                {
                    statement.setInt(1, id);
                    try (ResultSet resultSet = statement.executeQuery()) 
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
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return employeeDetails;
    }
}
