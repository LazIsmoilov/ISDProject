<%--
  Created by IntelliJ IDEA.
  User: laz
  Date: 30/4/2025
  Time: 4:04 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Register - IoT Bay</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="register-form">
  <h2>Register</h2>
  <form action="RegisterServlet" method="post">
    <label for="name">Full Name:</label>
    <input id="name" type="text" name="name" required><br><br>

    <label for="email">Email:</label>
    <input id="email" type="email" name="email" required><br><br>

    <label for="password">Password:</label>
    <input id="password" type="password" name="password" required><br><br>

    <label for="dob">Date of Birth:</label>
    <input id="dob" type="date" name="dob" required><br><br>

    <label for="gender">Gender:</label>
    <select name="gender" id="gender" required>
      <option value="Male">Male</option>
      <option value="Female">Female</option>
      <option value="Other">Other</option>
    </select><br><br>

    <label for="type">User Type:</label>
    <select name="type" id="type">
      <option value="CUSTOMER">Customer</option>
      <option value="STAFF">Staff</option>
      <option value="ADMIN">Admin</option>
    </select><br><br>

    <label for="phone">Phone Number:</label>
    <input type="text" id="phone" name="phone" required><br><br>

    <label for="tos">Agree to our <span style="color: dodgerblue; cursor: pointer">Terms of Service</span></label>
    <input type="checkbox" id="tos" name="tos">
    <br>
    <input type="submit" value="Register">
  </form>
</div>
</body>
</html>



