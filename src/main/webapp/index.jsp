<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="uts.isd.model.dao.UserDBManager" %>
<%@ page import="uts.isd.model.dao.DBConnector" %>
<%@ page import="java.sql.SQLException" %>

<%
    // Lazy initialize UserDBManager to ensure 'db' is available
    UserDBManager db = (UserDBManager) session.getAttribute("db");
    if (db == null) {
        try {
            DBConnector connector = new DBConnector();
            db = new UserDBManager(connector.getConnection());
            session.setAttribute("db", db);
        } catch (Exception e) {
            throw new RuntimeException("Cannot initialize UserDBManager", e);
        }
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
            out.print(db.getUserCount());
        } catch (SQLException e) {
            out.print("Error fetching count.");
        }
    %>
</p>
<br>

<div class="main-content">
    <a href="login.jsp"><button>ENTER</button></a>
</div>

<script>
    function logout() {
        window.location.href = "logout.jsp";
    }
</script>

</body>
</html>
