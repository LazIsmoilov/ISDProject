<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="uts.isd.model.User" %>
<%@ page import="uts.isd.model.dao.*" %>
<%@ page import="java.sql.SQLException" %>

<%
    // Retrieving the logged-in user from session (if any)
    DAO db = (DAO)session.getAttribute("db");

%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Home - IoT Bay</title>
    <link rel="stylesheet" href="style.css">
    <%@ include file="header.jsp" %>
</head>
<body>
<pref-header></pref-header>

<div class="content">
    <div class="main-header">
        <h1>IoT Bay</h1>
        <h2>Premium Devices | Secure Shopping | Expert Support</h2>
    </div>

    <div class="stats">
        <div class="stat-item">
            <div class="stat-number">25% + 20%</div>
            <div class="stat-label">Discount</div>
        </div>
        <div class="stat-item">
            <div class="stat-number">100+</div>
            <div class="stat-label">Products</div>
        </div>
        <div class="stat-item">
            <div class="stat-number">24/7</div>
            <div class="stat-label">Support</div>
        </div>
    </div>
</div>

<br>
<p>Registered Users: <%=db.Users().getUserCount()%></p>
<br>

<div class="main-content">
    <a href="register.jsp"><button>Register</button></a>
    <a href="login.jsp"><button>Login</button></a>
</div>

<script>
    function logout() {
        window.location.href = "logout.jsp";
    }
</script>
</body>
</html>
