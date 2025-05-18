<%--
  Created by IntelliJ IDEA.
  User: laz
  Date: 30/4/2025
  Time: 4:03 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Login - IoT Bay</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
<%
  String error = (String) session.getAttribute("loginError");
  if (error != null) {
%>
<p style="color:red;"><%= error %></p>
<%
    session.removeAttribute("loginError");
  }
%>
<%@ include file="header.jsp" %>
<div class="login-form">
  <h2>Login</h2>
  <form action="LoginServlet" method="post">
    <label for="email">Email:</label>
    <input id="email" type="email" name="email" required><br><br>
    <label for="password">Password:</label>
    <input id="password" type="password" name="password" required><br><br>
    <input type="submit" value="Login">
  </form>
</div>
</body>
</html>




