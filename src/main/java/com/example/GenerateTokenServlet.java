package com.example;

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
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import com.google.gson.JsonObject;

public class GenerateTokenServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("Received username: " + username);
        System.out.println("Received password: " + password);

        if (username == null || password == null) 
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username or password missing.");
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
                String sql = "SELECT id FROM admin WHERE username = ? AND password = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) 
                {
                    statement.setString(1, username);
                    statement.setString(2, hashPassword(password));
                    try (ResultSet resultSet = statement.executeQuery()) 
                    {
                        if (resultSet.next()) 
                        {
                            String token = UUID.randomUUID().toString();
                            int adminId = resultSet.getInt("id");

                            String insertTokenSql = "INSERT INTO tokens (admin_id, token, expires_at) VALUES (?, ?, ?)";
                            try (PreparedStatement insertTokenStmt = connection.prepareStatement(insertTokenSql)) 
                            {
                                insertTokenStmt.setInt(1, adminId);
                                insertTokenStmt.setString(2, token);
                                insertTokenStmt.setTimestamp(3, Timestamp.from(Instant.now().plusSeconds(300)));
                                insertTokenStmt.executeUpdate();
                            }

                            JsonObject jsonResponse = new JsonObject();
                            jsonResponse.addProperty("token", token);
                            response.setContentType("application/json");
                            response.getWriter().write(jsonResponse.toString());
                        } 
                        else 
                        {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password.");
                        }
                    }
                }
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
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
