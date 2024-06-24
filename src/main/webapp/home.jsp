<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home Page</title>
    <link rel="stylesheet" type="text/css" href="css/home.css">
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
            <h1 class="header-title">Home Page</h1>
        </div>
        <div class="content">
            <h3>Welcome to the Employee Managing System!</h3>
<!--             <p>This system is built using various cutting-edge technologies to efficiently manage employees for your business. The backend is powered by MySQL for robust data storage and management, while the application logic is implemented in Java. We utilize APIs for seamless integration and data exchange, and Servlets for handling client requests and responses. These technologies work together to provide functionalities such as adding, deleting, and updating employees, as well as file input and output operations for employee lists, among other features.</p>
 -->		    
		    <div class="slideshow-container">
		        <div class="mySlides">
		            <img src="images/1.png" style="width:40%">
		        </div>
		        <div class="mySlides">
		            <img src="images/2.png" style="width:40%">
		        </div>
		        <div class="mySlides">
		            <img src="images/3.png" style="width:40%">
		        </div>
		       	<div class="mySlides">
		            <img src="images/4.png" style="width:40%">
		        </div>
		        <div class="mySlides">
		            <img src="images/5.png" style="width:40%">
		        </div>
		       	<div class="mySlides">
		            <img src="images/6.png" style="width:40%">
		        </div>
		       	<div class="mySlides">
		            <img src="images/7.png" style="width:40%">
		        </div>
		    </div>
	
		    <script>
		        var slideIndex = 0;
		        showSlides();
		
		        function showSlides() {
		            var i;
		            var slides = document.getElementsByClassName("mySlides");
		            for (i = 0; i < slides.length; i++) {
		                slides[i].style.display = "none";  
		            }
		            slideIndex++;
		            if (slideIndex > slides.length) {slideIndex = 1}    
		            slides[slideIndex-1].style.display = "block";  
		            setTimeout(showSlides, 2000); // Change image every 2 seconds
		        }
		    </script>
        </div>
    </div>
</body>
</html>
