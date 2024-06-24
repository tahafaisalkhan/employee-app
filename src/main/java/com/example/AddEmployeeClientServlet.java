package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.*;

import java.io.IOException;

public class AddEmployeeClientServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                "{\"firstName\": \"%s\", \"lastName\": \"%s\", \"email\": \"%s\", \"hireDate\": \"%s\", \"address\": \"%s\", \"street\": \"%s\", \"province\": \"%s\", \"city\": \"%s\", \"country\": \"%s\", \"phoneNumber\": \"%s\"}",
                firstName, lastName, email, hireDate, address, street, province, city, country, phoneNumber);

        RequestBody body = RequestBody.create(mediaType, json);
        Request req = new Request.Builder()
                .url("http://localhost:8930/Proj2/api/addEmployeeJson")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        try (Response res = client.newCall(req).execute()) 
        {
            if (res.isSuccessful()) 
            {
                response.getWriter().write("Employee added successfully.");
            } 
            else 
            {
                response.getWriter().write("Failed to add employee. Error: " + res.message());
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
            response.getWriter().write("An error occurred: " + e.getMessage());
        }
    }
}
