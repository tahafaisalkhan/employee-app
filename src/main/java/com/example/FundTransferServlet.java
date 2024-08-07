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

public class FundTransferServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String amountStr = request.getParameter("amount");
        String employeeIdsStr = request.getParameter("employeeIds");
        double amount;

        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid amount. Please enter a valid number.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("fundTransfer.jsp");
            dispatcher.forward(request, response);
            return;
        }

        String[] employeeIdsArray = employeeIdsStr.split(",");
        List<Integer> employeeIds = new ArrayList<>();
        for (String idStr : employeeIdsArray) {
            try {
                employeeIds.add(Integer.parseInt(idStr.trim()));
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid employee ID: " + idStr);
                RequestDispatcher dispatcher = request.getRequestDispatcher("fundTransfer.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) 
            {
                List<Integer> invalidEmployeeIds = new ArrayList<>();
                for (Integer employeeId : employeeIds) 
                {
                    String checkEmployeeSql = "SELECT id FROM employees WHERE id = ?";
                    try (PreparedStatement checkEmployeeStmt = connection.prepareStatement(checkEmployeeSql)) 
                    {
                        checkEmployeeStmt.setInt(1, employeeId);
                        try (ResultSet resultSet = checkEmployeeStmt.executeQuery()) 
                        {
                            if (!resultSet.next()) 
                            {
                                invalidEmployeeIds.add(employeeId);
                            }
                        }
                    }
                }

                if (!invalidEmployeeIds.isEmpty()) 
                {
                    StringBuilder errorMessage = new StringBuilder("Invalid employee IDs: ");
                    for (Integer invalidId : invalidEmployeeIds) 
                    {
                        errorMessage.append(invalidId).append(" ");
                    }
                    request.setAttribute("errorMessage", errorMessage.toString().trim());
                    RequestDispatcher dispatcher = request.getRequestDispatcher("fundTransfer.jsp");
                    dispatcher.forward(request, response);
                    return;
                }

                String selectFundsSql = "SELECT funds FROM funds WHERE employee_id = ?";
                String updateFundsSql = "UPDATE funds SET funds = ? WHERE employee_id = ?";
                String insertFundsSql = "INSERT INTO funds (employee_id, funds) VALUES (?, ?)";

                for (Integer employeeId : employeeIds) 
                {
                    double currentFunds = 0;
                    boolean fundsExist = false;

                    try (PreparedStatement selectFundsStmt = connection.prepareStatement(selectFundsSql)) 
                    {
                        selectFundsStmt.setInt(1, employeeId);
                        try (ResultSet resultSet = selectFundsStmt.executeQuery()) 
                        {
                            if (resultSet.next()) 
                            {
                                currentFunds = resultSet.getDouble("funds");
                                fundsExist = true;
                            }
                        }
                    }

                    double newFunds = currentFunds + amount;

                    if (fundsExist) 
                    {
                        try (PreparedStatement updateFundsStmt = connection.prepareStatement(updateFundsSql)) 
                        {
                            updateFundsStmt.setDouble(1, newFunds);
                            updateFundsStmt.setInt(2, employeeId);
                            updateFundsStmt.executeUpdate();
                        }
                    } 
                    else 
                    {
                        try (PreparedStatement insertFundsStmt = connection.prepareStatement(insertFundsSql)) 
                        {
                            insertFundsStmt.setInt(1, employeeId);
                            insertFundsStmt.setDouble(2, newFunds);
                            insertFundsStmt.executeUpdate();
                        }
                    }
                }

                response.sendRedirect("fundTransfer.jsp");
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while processing the fund transfer.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("fundTransfer.jsp");
            dispatcher.forward(request, response);
        }
    }
}
