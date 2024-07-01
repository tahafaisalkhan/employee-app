package com.example;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String jdbcUrl = "jdbc:mysql://localhost:3306/Employees";
        String jdbcUser = "root";
        String jdbcPassword = "tahafaisalkhan";

        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) 
            {
                String checkUserSQL = "SELECT COUNT(*) FROM admin WHERE username = ?";
                try (PreparedStatement checkUserStmt = connection.prepareStatement(checkUserSQL)) 
                {
                    checkUserStmt.setString(1, username);
                    try (ResultSet resultSet = checkUserStmt.executeQuery()) 
                    {
                        if (resultSet.next() && resultSet.getInt(1) > 0)
                        {
                            request.setAttribute("errorMessage", "Username already exists. Please choose a different username.");
                            RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
                            dispatcher.forward(request, response);
                            return;
                        }
                    }
                }

                String sql = "INSERT INTO admin (username, password) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) 
                {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, hashPassword(password));
                    preparedStatement.executeUpdate();
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        response.sendRedirect("login.jsp");
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException 
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) 
        {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
