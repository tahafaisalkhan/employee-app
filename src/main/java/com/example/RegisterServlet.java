package com.example;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String adminUsername = request.getParameter("adminUsername");
        String adminPassword = request.getParameter("adminPassword");

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) {

                // Check if the admin credentials are valid
                String checkAdminSQL = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
                try (PreparedStatement checkAdminStmt = connection.prepareStatement(checkAdminSQL)) {
                    checkAdminStmt.setString(1, adminUsername);
                    checkAdminStmt.setString(2, adminPassword);
                    try (ResultSet adminResultSet = checkAdminStmt.executeQuery()) {
                        if (adminResultSet.next() && adminResultSet.getInt(1) == 0) {
                            request.setAttribute("errorMessage", "Invalid admin credentials. Please try again.");
                            RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
                            dispatcher.forward(request, response);
                            return;
                        }
                    }
                }

                // Check if the username already exists
                String checkUserSQL = "SELECT COUNT(*) FROM users WHERE username = ?";
                try (PreparedStatement checkUserStmt = connection.prepareStatement(checkUserSQL)) {
                    checkUserStmt.setString(1, username);
                    try (ResultSet resultSet = checkUserStmt.executeQuery()) {
                        if (resultSet.next() && resultSet.getInt(1) > 0) {
                            request.setAttribute("errorMessage", "Username already exists. Please choose a different username.");
                            RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
                            dispatcher.forward(request, response);
                            return;
                        }
                    }
                }

                // Insert the new user
                String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("login.jsp");
    }
}
