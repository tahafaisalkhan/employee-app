package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.*;

import java.io.IOException;

public class UpdateEmployeeClientServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String hireDate = request.getParameter("hireDate");
        String address = request.getParameter("address");
        String street = request.getParameter("street");
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String phoneNumber = request.getParameter("phoneNumber");

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        String json = String.format(
                "{\"id\": \"%s\", \"firstName\": \"%s\", \"lastName\": \"%s\", \"email\": \"%s\", \"hireDate\": \"%s\", \"address\": \"%s\", \"street\": \"%s\", \"province\": \"%s\", \"city\": \"%s\", \"country\": \"%s\", \"phoneNumber\": \"%s\"}",
                id, firstName, lastName, email, hireDate, address, street, province, city, country, phoneNumber);

        RequestBody body = RequestBody.create(mediaType, json);
        Request req = new Request.Builder()
                .url("http://localhost:8930/Proj2/api/updateEmployeeJson")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response res = client.newCall(req).execute()) 
        {
            if (res.isSuccessful()) 
            {
                response.getWriter().write("Employee updated successfully.");
            } 
            else 
            {
                response.getWriter().write("Failed to update employee. Error: " + res.message());
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            response.getWriter().write("An error occurred: " + e.getMessage());
        }
    }
}
