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
import java.util.List;
import java.util.stream.Collectors;

@MultipartConfig
public class UploadFileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();
        String fileType = filePart.getContentType();
        List<String> allowedTypes = List.of("text/plain", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        if (!allowedTypes.contains(fileType)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Unsupported file type.");
            return;
        }

        try (InputStream fileContent = filePart.getInputStream()) {
            if (fileName.endsWith(".txt")) {
                processTxtFile(fileContent);
            } else if (fileName.endsWith(".xls")) {
                processExcelFile(fileContent, false);
            } else if (fileName.endsWith(".xlsx")) {
                processExcelFile(fileContent, true);
            }
            response.getWriter().write("File uploaded and processed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while processing the file: " + e.getMessage());
        }
    }

    private void processTxtFile(InputStream fileContent) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split("\\|");
                if (columns.length >= 4) {
                    String firstName = columns[1];
                    String lastName = columns[2];
                    String email = columns[3];
                    String hireDate = columns[4];
                    addEmployeeToDatabase(firstName, lastName, email, hireDate);
                }
            }
        }
    }

    private void processExcelFile(InputStream fileContent, boolean isXlsx) throws Exception {
        Workbook workbook = null;
        try {
            if (isXlsx) {
                workbook = new XSSFWorkbook(fileContent);
            } else {
                workbook = new HSSFWorkbook(fileContent);
            }
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                String firstName = getCellValue(row, 0);
                String lastName = getCellValue(row, 1);
                String email = getCellValue(row, 2);
                String hireDate = getCellValue(row, 3);
                if (firstName != null && lastName != null && email != null && hireDate != null) {
                    addEmployeeToDatabase(firstName, lastName, email, hireDate);
                }
            }
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    private String getCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString(); // Convert date to string
                } else {
                    return Double.toString(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            default:
                return null;
        }
    }

    private void addEmployeeToDatabase(String firstName, String lastName, String email, String hireDate) throws Exception {
        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) {
            String sql = "INSERT INTO employees (first_name, last_name, email, hire_date) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, email);
                statement.setString(4, hireDate);
                statement.executeUpdate();
            }
        }
    }
}
