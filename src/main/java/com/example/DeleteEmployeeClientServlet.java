package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.*;

import java.io.IOException;

public class DeleteEmployeeClientServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        String json = String.format("{\"id\": \"%s\"}", id);
        RequestBody body = RequestBody.create(mediaType, json);
        Request req = new Request.Builder()
                .url("http://localhost:8930/Proj2/api/deleteEmployeeJson")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response res = client.newCall(req).execute()) 
        {
            response.setContentType("text/html");
            if (res.isSuccessful()) 
            {
                response.getWriter().write("<p>Employee deleted successfully.</p>");
            } 
            else 
            {
                response.getWriter().write("<p>Failed to delete employee. Error: " + res.message() + "</p>");
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            response.getWriter().write("<p>An error occurred: " + e.getMessage() + "</p>");
        }
    }
}
