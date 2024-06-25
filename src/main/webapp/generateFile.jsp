<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Generate Employee Data Files</title>
    <link rel="stylesheet" type="text/css" href="css/generateFile.css">
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

        function generateTextFile() 
        {
            var xhr = new XMLHttpRequest();
            xhr.open("GET", "EmployeeDataToFileServlet", true);
            xhr.onreadystatechange = function () 
            {
                if (xhr.readyState == 4 && xhr.status == 200) 
                {
                    document.getElementById("message").innerText = xhr.responseText;
                }
            };
            xhr.send();
        }

        function generateXLSFile() 
        {
            var xhr = new XMLHttpRequest();
            xhr.open("GET", "EmployeeDataToXLSFileServlet", true);
            xhr.onreadystatechange = function () 
            {
                if (xhr.readyState == 4 && xhr.status == 200) 
                {
                    document.getElementById("message").innerText = xhr.responseText;
                }
            };
            xhr.send();
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
            <h1 class="header-title">Generate Employee Data Files</h1>
        </div>
        <div class="center">
            <button onclick="generateTextFile()" class="btn-add">Generate Text File</button>
            <button onclick="generateXLSFile()" class="btn-add">Generate XLS File</button>
            <div id="message" style="margin-top: 20px;"></div>
        </div>
    </div>
</body>
</html>
