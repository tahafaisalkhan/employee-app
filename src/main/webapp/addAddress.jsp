<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Address</title>
    <link rel="stylesheet" type="text/css" href="css/addAddress.css">
    <link rel="icon" type="image/png" href="images/favicon.png">
    <script>
        function toggleSidebar() 
        {
            const sidebar = document.querySelector('.sidebar');
            const mainContent = document.querySelector('.main-content');
            if (sidebar.classList.contains('active')) 
            {
                sidebar.classList.remove('active');
                mainContent.classList.remove('active');
            } 
            else 
            {
                sidebar.classList.add('active');
                mainContent.classList.add('active');
            }
        }

        function validateForm() 
        {
            const addressType = document.getElementById('addressType').value;
            const province = document.getElementById('province').value;
            const city = document.getElementById('city').value;
            const country = document.getElementById('country').value;
            const numberPattern = /^[0-9]+$/;
            const textPattern = /^[a-zA-Z ]+$/;

            if (addressType !== 'Home' && addressType !== 'Office') 
            {
                alert('Address Type must be Home or Office');
                return false;
            }

            if (!textPattern.test(province))
{
                alert('Province must contain only alphabetic characters');
                return false;
            }

            if (!textPattern.test(city)) 
            {
                alert('City must contain only alphabetic characters');
                return false;
            }

            if (!textPattern.test(country)) 
            {
                alert('Country must contain only alphabetic characters');
                return false;
            }

            return true;
        }
    </script>
</head>
<body>
    <jsp:include page="sidebar.jsp" />
    <div class="main-content">
        <div class="header">
            <div class="header-left">
                <button class="btn-toggle-sidebar" onclick="toggleSidebar()">☰</button>
                <form action="LogoutServlet" method="post" class="logout-form">
                    <button type="submit" class="btn-logout">Logout</button>
                </form>
            </div>
            <h1 class="header-title">Add Address for Employee</h1>
        </div>
        <div class="content">
            <% 
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (errorMessage != null) {
            %>
                <div class="error-message"><%= errorMessage %></div>
            <% 
                }
            %>
            <form action="AddAddressServlet" method="POST" onsubmit="return validateForm()">
                <label for="employeeId">Employee ID:</label>
                <input type="text" id="employeeId" name="employeeId" required><br>

                <label for="address">Address:</label>
                <input type="text" id="address" name="address" required><br>

                <label for="street">Street:</label>
                <input type="text" id="street" name="street" required><br>

                <label for="province">Province:</label>
                <input type="text" id="province" name="province" required><br>

                <label for="city">City:</label>
                <input type="text" id="city" name="city" required><br>

                <label for="country">Country:</label>
                <input type="text" id="country" name="country" required><br>

                <label for="phoneNumber">Phone Number:</label>
                <input type="text" id="phoneNumber" name="phoneNumber" required><br>

                <label for="addressType">Address Type (Home/Office):</label>
                <input type="text" id="addressType" name="addressType" required><br>

                <button type="submit" class="btn">Add Address</button>
            </form>
        </div>
    </div>
</body>
</html>
