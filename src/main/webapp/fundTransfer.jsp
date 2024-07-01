<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Fund Transfer</title>
    <link rel="stylesheet" type="text/css" href="css/fundTransfer.css">
    <link rel="icon" type="image/png" href="images/favicon.png">
   
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
            <h2 class="header-title">Fund Transfer</h2>
        </div>
        <div class="content">
            <form action="FundTransferServlet" method="POST">
                <label for="amount">Amount to Transfer:</label>
                <input type="number" id="amount" name="amount" step="0.01" required><br>

                <label for="employeeIds">Employee IDs (comma-separated):</label>
                <input type="text" id="employeeIds" name="employeeIds" required><br>

                <label for="token">Token:</label>
                <input type="text" id="token" name="token" required><br>

                <button type="submit" class="btn-transfer">Transfer Funds</button>
            </form>
            <% 
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
            %>
                <p class="error-message"><%= errorMessage %></p>
            <% 
            }
            %>
        </div>

        <div class="token-panel">
            <button class="btn-generate-token" onclick="generateToken()">Show My Token</button>
            <p id="tokenDisplay" class="token-display"></p>
        </div>
    </div>
    <script>
        function toggleSidebar() {
            const sidebar = document.querySelector('.sidebar');
            const mainContent = document.querySelector('.main-content');
            if (sidebar.classList.contains('active')) {
                sidebar.classList.remove('active');
                mainContent.classList.remove('active');
            } else {
                sidebar.classList.add('active');
                mainContent.classList.add('active');
            }
        }

        function generateToken() {
            const token = '<%= session.getAttribute("token") %>';
            document.getElementById('tokenDisplay').innerText = token;
        }
    </script>
</body>
</html>
