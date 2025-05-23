<%--
  Created by IntelliJ IDEA.
  User: laz
  Date: 14/5/2025
  Time: 10:31 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="uts.isd.model.User" %>
<%@ page import="java.util.List" %>
<html>
<head>
  <title>Admin Dashboard</title>
  <link rel="stylesheet" href="admin.css">
</head>
<body>
<h2>Admin Dashboard</h2>
<% User currentUser = (User) session.getAttribute("loggedInUser");
  if (currentUser == null || !currentUser.isAdmin()) { %>
<p style="color:red;">You do not have permission to access this page.</p>
<a href="index.jsp">Back to Home</a>
<% } else { %>
<h3>User Management</h3>
<a href="userManagement.jsp">Create New User</a> <!-- Link to userManagement.jsp -->
<form action="AdminServlet" method="get">
  <input type="hidden" name="action" value="search">
  <label>Name:</label>
  <input type="text" name="name"><br>
  <label>Phone Number:</label>
  <input type="text" name="phone"><br>
  <button type="submit">Search Users</button>
</form>

<h3>User List</h3>
<% List<User> users = (List<User>) session.getAttribute("users");
  if (users != null && !users.isEmpty()) { %>
<table border="1">
  <tr>
    <th>ID</th>
    <th>Name</th>
    <th>Email</th>
    <th>Phone</th>
    <th>Type</th>
    <th>Active</th>
    <th>Actions</th>
  </tr>
  <% for (User user : users) { %>
  <tr>
    <td><%= user.getId() %></td>
    <td><%= user.getName() %></td>
    <td><%= user.getEmail() %></td>
    <td><%= user.getPhoneNumber() != null ? user.getPhoneNumber() : "" %></td>
    <td><%= user.getType() %></td>
    <td><%= user.getIsActive() ? "Yes" : "No" %></td>
    <td>
      <a href="edit.jsp?userId=<%= user.getId() %>">Edit</a>
      <a href="AdminServlet?action=toggle&id=<%= user.getId() %>">Toggle Active</a>
      <a href="AdminServlet?action=delete&id=<%= user.getId() %>">Delete</a>
    </td>
  </tr>
  <% } %>
</table>
<% } else { %>
<p>No users found.</p>
<% } %>
<a href="index.jsp">Back to Home</a>
<% } %>
<% if (session.getAttribute("error") != null) { %>
<p style="color:red;"><%= session.getAttribute("error") %></p>
<% session.removeAttribute("error"); %>
<% } %>
</body>
</html>


