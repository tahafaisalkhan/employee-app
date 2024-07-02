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
            <h1 class="header-title">Fund Transfer</h1>
        </div>
        <div class="content">
            <form action="FundTransferServlet" method="POST">
                <label for="amount">Amount to Transfer:</label>
                <input type="number" id="amount" name="amount" step="0.01" required><br>

                <label for="employeeIds">Employee IDs (comma-separated):</label>
                <input type="text" id="employeeIds" name="employeeIds" required><br>

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
    </script>
</body>
</html>
