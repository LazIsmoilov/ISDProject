<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="uts.isd.model.dao.UserDBManager" %>
<%@ page import="uts.isd.model.dao.DAO" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="jakarta.servlet.ServletException" %>

<% if (session.getAttribute("error") != null) { %>
<p style="color:red;"><%= session.getAttribute("error") %></p>
<% session.removeAttribute("error"); %>
<% } %>
<%
    // Initialize UserDBManager from DAO
    UserDBManager db = null;
    try {
        // Check if session attribute "db" exists and is a DAO instance
        Object dbObj = session.getAttribute("db");
        if (dbObj instanceof DAO) {
            db = ((DAO) dbObj).Users();
        } else {
            // Initialize new DAO if not set or wrong type
            DAO dao = new DAO();
            db = dao.Users();
            session.setAttribute("db", dao); // Store DAO in session
        }
    } catch (SQLException e) {
        // Log the error and set a default message for display
        out.println("<!-- SQLException during DB initialization: " + e.getMessage() + " -->");
        db = null;
    } catch (Exception e) {
        // Handle any other unexpected errors
        out.println("<!-- Unexpected error during DB initialization: " + e.getMessage() + " -->");
        db = null;
    }
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
<p>Registered Users:
    <%
        try {
            if (db != null) {
                out.print(db.getUserCount());
            } else {
                out.print("Database unavailable");
            }
        } catch (SQLException e) {
            out.print("Error fetching count: " + e.getMessage());
        }
    %>
</p>
<br>

<div class="main-content">
    <a href="login.jsp"><button>ENTER</button></a>
</div>

</body>
</html>