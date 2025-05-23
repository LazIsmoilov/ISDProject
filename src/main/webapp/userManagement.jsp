<%--
  Created by IntelliJ IDEA.
  User: laz
  Date: 20/5/2025
  Time: 3:18 am
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="uts.isd.model.User" %>
<html>
<head>
  <title>User Management</title>
  <link rel="stylesheet" href="admin.css">
</head>
<body>
<h2>Create User</h2>
<% User currentUser = (User) session.getAttribute("loggedInUser");
  if (currentUser == null || !currentUser.isAdmin()) { %>
<p style="color:red;">You do not have permission to access this page.</p>
<a href="index.jsp">Back to Home</a>
<% } else { %>
<form action="AdminServlet" method="post">
  <input type="hidden" name="action" value="create">
  <label>Name:</label>
  <input type="text" name="name" required><br>
  <label>Email:</label>
  <input type="email" name="email" required><br>
  <label>Password:</label>
  <input type="password" name="password" required><br>
  <label>Date of Birth:</label>
  <input type="date" name="dob"><br>
  <label>Gender:</label>
  <select name="gender">
    <option value="Male">Male</option>
    <option value="Female">Female</option>
    <option value="Other">Other</option>
  </select><br>
  <label>Phone Number:</label>
  <input type="text" name="phoneNumber"><br>
  <label>User Type:</label>
  <select name="userType" required>
    <option value="CUSTOMER">Customer</option>
    <option value="STAFF">Staff</option>
    <!-- Uncomment if ADMIN creation is allowed -->
    <!-- <option value="ADMIN">Admin</option> -->
  </select><br>
  <label>Is Active:</label>
  <input type="checkbox" name="isActive" checked><br>
  <button type="submit">Create User</button>
</form>
<a href="index.jsp">Back to Home</a>
<% } %>
<% if (session.getAttribute("error") != null) { %>
<p style="color:red;"><%= session.getAttribute("error") %></p>
<% session.removeAttribute("error"); %>
<% } %>
<% if (session.getAttribute("success") != null) { %>
<p style="color:green;"><%= session.getAttribute("success") %></p>
<% session.removeAttribute("success"); %>
<% } %>
</body>
</html>