package com.example;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class LoginServlet extends HttpServlet 
{
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
                String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) 
                {
                    statement.setString(1, username);
                    statement.setString(2, hashPassword(password));
                    try (ResultSet resultSet = statement.executeQuery()) 
                    {
                        if (resultSet.next()) {
                            HttpSession session = request.getSession();
                            session.setAttribute("username", username);
                            session.setAttribute("role", resultSet.getString("role"));

                            String token = UUID.randomUUID().toString();
                            session.setAttribute("token", token);

                            response.sendRedirect("home.jsp");
                        } 
                        else 
                        {
                            request.setAttribute("errorMessage", "Invalid username or password.");
                            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
                            dispatcher.forward(request, response);
                        }
                    }
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
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
