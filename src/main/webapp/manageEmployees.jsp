<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Employees</title>
    <link rel="stylesheet" type="text/css" href="css/manageEmployees.css">
    <link rel="icon" type="image/png" href="images/favicon.png">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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
            <h1 class="header-title">Manage Employees</h1>
        </div>
        <div class="container">
            <div class="panel">
                <h2>Add Employee</h2>
                <form id="addEmployeeForm">
                    <label for="firstName">First Name:</label>
                    <input type="text" id="firstName" name="firstName" required><br>

                    <label for="lastName">Last Name:</label>
                    <input type="text" id="lastName" name="lastName" required><br>

                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required><br>

                    <label for="hireDate">Hire Date (YYYY-MM-DD):</label>
                    <input type="date" id="hireDate" name="hireDate" required><br>

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

                    <button type="submit" class="btn">Add Employee</button>
                </form>
                <div id="addEmployeeResult"></div>
            </div>

            <div class="panel">
                <h2>Delete Employee</h2>
                <form id="deleteEmployeeForm">
                    <label for="id">Employee ID:</label>
                    <input type="text" id="id" name="id" required><br>
                    <button type="submit" class="btn">Delete Employee</button>
                </form>
                <div id="deleteEmployeeResult"></div>
            </div>

            <div class="panel">
                <h2>Update Employee</h2>
                <form id="updateEmployeeForm">
                    <label for="id">Employee ID:</label>
                    <input type="text" id="id" name="id" required><br>

                    <label for="firstName">First Name:</label>
                    <input type="text" id="firstName" name="firstName"><br>

                    <label for="lastName">Last Name:</label>
                    <input type="text" id="lastName" name="lastName"><br>

                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email"><br>

                    <label for="hireDate">Hire Date (YYYY-MM-DD):</label>
                    <input type="date" id="hireDate" name="hireDate"><br>

                    <label for="address">Address:</label>
                    <input type="text" id="address" name="address"><br>

                    <label for="street">Street:</label>
                    <input type="text" id="street" name="street"><br>

                    <label for="province">Province:</label>
                    <input type="text" id="province" name="province"><br>

                    <label for="city">City:</label>
                    <input type="text" id="city" name="city"><br>

                    <label for="country">Country:</label>
                    <input type="text" id="country" name="country"><br>

                    <label for="phoneNumber">Phone Number:</label>
                    <input type="text" id="phoneNumber" name="phoneNumber"><br>

                    <button type="submit" class="btn">Update Employee</button>
                </form>
                <div id="updateEmployeeResult"></div>
            </div>
        </div>
    </div>

    <script>
        $(document).ready(function() {
            $('#addEmployeeForm').on('submit', function(event) {
                event.preventDefault();
                $.ajax({
                    url: 'AddEmployeeClientServlet',
                    type: 'POST',
                    data: $(this).serialize(),
                    success: function(response) {
                        $('#addEmployeeResult').html('<p>' + response + '</p>');
                        $('#addEmployeeForm')[0].reset();
                    },
                    error: function(xhr, status, error) {
                        $('#addEmployeeResult').html('<p>An error occurred: ' + error + '</p>');
                    }
                });
            });

            $('#deleteEmployeeForm').on('submit', function(event) {
                event.preventDefault();
                $.ajax({
                    url: 'DeleteEmployeeClientServlet',
                    type: 'POST',
                    data: $(this).serialize(),
                    success: function(response) {
                        $('#deleteEmployeeResult').html('<p>' + response + '</p>');
                        $('#deleteEmployeeForm')[0].reset();
                    },
                    error: function(xhr, status, error) {
                        $('#deleteEmployeeResult').html('<p>An error occurred: ' + error + '</p>');
                    }
                });
            });

            $('#updateEmployeeForm').on('submit', function(event) {
                event.preventDefault();
                $.ajax({
                    url: 'UpdateEmployeeClientServlet',
                    type: 'POST',
                    data: $(this).serialize(),
                    success: function(response) {
                        $('#updateEmployeeResult').html('<p>' + response + '</p>');
                        $('#updateEmployeeForm')[0].reset(); 
                    },
                    error: function(xhr, status, error) {
                        $('#updateEmployeeResult').html('<p>An error occurred: ' + error + '</p>');
                    }
                });
            });
        });
    </script>
</body>
</html>
