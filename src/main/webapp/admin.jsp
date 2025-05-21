<%--
  Created by IntelliJ IDEA.
  User: laz
  Date: 14/5/2025
  Time: 10:31 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List, uts.isd.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
  <title>Admin Panel - User Management</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>

<h2>Manage Users</h2>

<!-- Display any error messages -->
<%
  String error = (String) session.getAttribute("error");
  if (error != null) {
%>
<p style="color: red;"><%= error %></p>
<%
    session.removeAttribute("error"); // Clear after displaying
  }
%>

<!-- Search Form -->
<form action="AdminServlet" method="get">
  <input type="hidden" name="action" value="search">
  <label for="searchName">Search by Name:</label>
  <input id="searchName" type="text" name="name">
  <label for="searchPhone">Search by Phone:</label>
  <input id="searchPhone" type="text" name="phone">
  <input type="submit" value="Search">
</form>

<!-- User List Table -->
<%
  List<User> users = (List<User>) session.getAttribute("users");
  if (users != null && !users.isEmpty()) {
%>
<table>
  <tr>
    <th>ID</th><th>Name</th><th>Email</th><th>Phone</th><th>Status</th><th>Actions</th>
  </tr>
  <%
    for (User user : users) {
  %>
  <tr>
    <td><%= user.getId() %></td>
    <td><%= user.getName() %></td>
    <td><%= user.getEmail() %></td>
    <td><%= user.getPhoneNumber() %></td>
    <td><%= user.getIsActive() ? "Active" : "Inactive" %></td>
    <td>
      <a href="AdminServlet?action=toggle&id=<%= user.getId() %>">
        <%= user.getIsActive() ? "Deactivate" : "Activate" %>
      </a>
      <a href="edit.jsp?id=<%= user.getId() %>">Edit</a>
      <a href="AdminServlet?action=delete&id=<%= user.getId() %>" onclick="return confirm('Are you sure?')">Delete</a>
    </td>
  </tr>
  <% } %>
</table>
<% } else { %>
<p>No users found.</p>
<% } %>

</body>
</html>


