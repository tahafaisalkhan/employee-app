package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDataToXLSFileServlet extends HttpServlet 
{
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
             FileOutputStream fileOut = new FileOutputStream(filename)) 
        {

            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Employee Data");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("First Name");
            headerRow.createCell(2).setCellValue("Last Name");
            headerRow.createCell(3).setCellValue("Email");
            headerRow.createCell(4).setCellValue("Hire Date");
            headerRow.createCell(5).setCellValue("Address");
            headerRow.createCell(6).setCellValue("Street");
            headerRow.createCell(7).setCellValue("Province");
            headerRow.createCell(8).setCellValue("City");
            headerRow.createCell(9).setCellValue("Country");
            headerRow.createCell(10).setCellValue("Phone Number");

            String sql = "SELECT e.id, e.first_name, e.last_name, e.email, e.hire_date, " +
                         "d.address, d.street, d.province, d.city, d.country, d.phone_number " +
                         "FROM employees e LEFT JOIN employee_details d ON e.id = d.id";

            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) 
            {

                int rowIndex = 1;
                while (resultSet.next()) 
                {
                    Row row = sheet.createRow(rowIndex++);

                    row.createCell(0).setCellValue(resultSet.getInt("id"));
                    row.createCell(1).setCellValue(resultSet.getString("first_name"));
                    row.createCell(2).setCellValue(resultSet.getString("last_name"));
                    row.createCell(3).setCellValue(resultSet.getString("email"));
                    row.createCell(4).setCellValue(resultSet.getString("hire_date"));
                    row.createCell(5).setCellValue(resultSet.getString("address"));
                    row.createCell(6).setCellValue(resultSet.getString("street"));
                    row.createCell(7).setCellValue(resultSet.getString("province"));
                    row.createCell(8).setCellValue(resultSet.getString("city"));
                    row.createCell(9).setCellValue(resultSet.getString("country"));
                    row.createCell(10).setCellValue(resultSet.getString("phone_number"));
                }
            }

            workbook.write(fileOut);
        }
    }
}

