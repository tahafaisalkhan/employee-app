<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Upload Employees File</title>
    <link rel="stylesheet" type="text/css" href="css/uploadFiles.css">
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

        $(document).ready(function() 
        		{
            $('#uploadForm').on('submit', function(event) 
            		{
                event.preventDefault();
                var formData = new FormData(this);

                $.ajax({
                    url: 'UploadFileServlet',
                    type: 'POST',
                    data: formData,
                    contentType: false,
                    processData: false,
                    success: function(response) {
                        $('#uploadResult').html('<p>' + response + '</p>');
                    },
                    error: function(xhr, status, error) {
                        $('#uploadResult').html('<p>An error occurred: ' + error + '</p>');
                    }
                });
            });
        });
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
            <h1 class="header-title">Upload Employees File</h1>
        </div>
        <div class="center">
            <form id="uploadForm" enctype="multipart/form-data">
                <label for="file">Select file:</label>
                <input type="file" id="file" name="file" accept=".txt, .xls, .xlsx" required><br>
                <button type="submit" class="btn">Upload</button>
            </form>
            <div id="uploadResult"></div>
        </div>
    </div>
</body>
</html>
