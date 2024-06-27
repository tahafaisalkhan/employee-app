package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@MultipartConfig
public class UploadFileServlet extends HttpServlet 
{
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();
        String fileType = filePart.getContentType();
        List<String> allowedTypes = List.of("text/plain", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        if (!allowedTypes.contains(fileType)) 
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Unsupported file type.");
            return;
        }

        try (InputStream fileContent = filePart.getInputStream()) 
        {
            if (fileName.endsWith(".txt")) 
            {
                processTxtFile(fileContent);
            } 
            else if (fileName.endsWith(".xls")) 
            {
                processExcelFile(fileContent, false);
            } 
            else if (fileName.endsWith(".xlsx")) 
            {
                processExcelFile(fileContent, true);
            }
            response.getWriter().write("File uploaded and processed successfully.");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while processing the file: " + e.getMessage());
        }
    }

    private void processTxtFile(InputStream fileContent) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                String[] columns = line.split("\\|");
                if (columns.length >= 11) 
                {
                    String firstName = columns[1];
                    String lastName = columns[2];
                    String email = columns[3];
                    String hireDate = columns[4];
                    String address = columns[5];
                    String street = columns[6];
                    String province = columns[7];
                    String city = columns[8];
                    String country = columns[9];
                    String phoneNumber = columns[10];
                    String addressType = columns[11];
                    addEmployeeToDatabase(firstName, lastName, email, hireDate, address, street, province, city, country, phoneNumber, addressType);
                }
            }
        }
    }

    private void processExcelFile(InputStream fileContent, boolean isXlsx) throws Exception {
        Workbook workbook = null;
        try 
        {
            if (isXlsx) 
            {
                workbook = new XSSFWorkbook(fileContent);
            } 
            else 
            {
                workbook = new HSSFWorkbook(fileContent);
            }
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) 
            {
                if (row.getRowNum() == 0) continue;
                String firstName = getCellValue(row, 0);
                String lastName = getCellValue(row, 1);
                String email = getCellValue(row, 2);
                String hireDate = getCellValue(row, 3);
                String address = getCellValue(row, 4);
                String street = getCellValue(row, 5);
                String province = getCellValue(row, 6);
                String city = getCellValue(row, 7);
                String country = getCellValue(row, 8);
                String phoneNumber = getCellValue(row, 9);
                String addressType = getCellValue(row, 10);
                if (firstName != null && lastName != null && email != null && hireDate != null) 
                {
                    addEmployeeToDatabase(firstName, lastName, email, hireDate, address, street, province, city, country, phoneNumber, addressType);
                }
            }
        } 
        finally 
        {
            if (workbook != null) 
            {
                workbook.close();
            }
        }
    }

    private String getCellValue(Row row, int cellNum) 
    {
        Cell cell = row.getCell(cellNum);
        if (cell == null) return null;
        switch (cell.getCellType()) 
        {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } 
                else 
                {
                    return Double.toString(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            default:
                return null;
        }
    }

    private void addEmployeeToDatabase(String firstName, String lastName, String email, String hireDate, String address, String street, String province, String city, String country, String phoneNumber, String addressType) throws Exception {
        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection connection = null;

        try 
        {
            connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
            connection.setAutoCommit(false);

            String insertEmployeeSQL = "INSERT INTO employees (first_name, last_name, email, hire_date) VALUES (?, ?, ?, ?)";
            int employeeId;
            try (PreparedStatement statement = connection.prepareStatement(insertEmployeeSQL, PreparedStatement.RETURN_GENERATED_KEYS)) 
            {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, email);
                statement.setString(4, hireDate);
                statement.executeUpdate();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) 
                {
                    if (generatedKeys.next()) 
                    {
                        employeeId = generatedKeys.getInt(1);
                    } 
                    else 
                    {
                        throw new SQLException("Failed to retrieve employee ID.");
                    }
                }
            }

            String insertDetailsSQL = "INSERT INTO employee_details (id, address, street, province, city, country, phone_number, address_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertDetailsSQL)) 
            {
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

            connection.commit();
        } 
        catch (Exception e) 
        {
            if (connection != null) 
            {
                connection.rollback();
            }
            throw e;
        } 
        finally 
        {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }
}
