<%--
  Created by IntelliJ IDEA.
  User: laz
  Date: 30/4/2025
  Time: 4:03 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="uts.isd.model.User" %>
<%@ page import="uts.isd.model.User.UserType" %>
<%@ page import="uts.isd.model.dao.UserDBManager" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
  User loggedInUser = (User) session.getAttribute("loggedInUser");
  if (loggedInUser == null) {
    response.sendRedirect("login.jsp");
    return;
  }
  boolean isAdmin = loggedInUser.isAdmin();
  String userIdParam = request.getParameter("userId");
  int idToEdit = loggedInUser.getId();
  if (isAdmin && userIdParam != null && !userIdParam.trim().isEmpty()) {
    try {
      idToEdit = Integer.parseInt(userIdParam);
    } catch (NumberFormatException e) {
      session.setAttribute("error", "Invalid user ID: " + userIdParam);
      response.sendRedirect("index.jsp");
      return;
    }
  }
  UserDBManager userDbManager = (UserDBManager) application.getAttribute("userDBManager");
  if (userDbManager == null) {
    session.setAttribute("error", "Database not initialized");
    response.sendRedirect("index.jsp");
    return;
  }
  User userToEdit = userDbManager.getById(idToEdit);
  if (userToEdit == null) {
    session.setAttribute("error", "User not found for ID: " + idToEdit);
    response.sendRedirect("index.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html>
<head>
  <title>Edit User</title>
  <link rel="stylesheet" href="admin.css">
</head>
<body>
<h2>Edit User Profile (ID: <%= userToEdit.getId() %>, Name: <%= userToEdit.getName() %>)</h2>
<form action="EditServlet" method="post">
  <input type="hidden" name="userId" value="<%= userToEdit.getId() %>">
  <label>Name:</label>
  <input type="text" name="name" value="<%= userToEdit.getName() %>" required><br>
  <label>Email:</label>
  <input type="email" name="email" value="<%= userToEdit.getEmail() %>" required><br>
  <label>Password:</label>
  <input type="password" name="password" placeholder="Enter new password"><br>
  <label>Date of Birth:</label>
  <input type="date" name="dob" value="<%= userToEdit.getDob() %>"><br>
  <label>Gender:</label>
  <select name="gender">
    <option value="Male" <%= "Male".equalsIgnoreCase(userToEdit.getGender()) ? "selected" : "" %>>Male</option>
    <option value="Female" <%= "Female".equalsIgnoreCase(userToEdit.getGender()) ? "selected" : "" %>>Female</option>
    <option value="Other" <%= "Other".equalsIgnoreCase(userToEdit.getGender()) ? "selected" : "" %>>Other</option>
  </select><br>
  <% if (isAdmin) { %>
  <label>Phone Number:</label>
  <input type="text" name="phoneNumber" value="<%= userToEdit.getPhoneNumber() != null ? userToEdit.getPhoneNumber() : "" %>"><br>
  <label>User Type:</label>
  <select name="userType">
    <option value="CUSTOMER" <%= userToEdit.getType() == UserType.CUSTOMER ? "selected" : "" %>>Customer</option>
    <option value="STAFF" <%= userToEdit.getType() == UserType.STAFF ? "selected" : "" %>>Staff</option>
    <option value="ADMIN" <%= userToEdit.getType() == UserType.ADMIN ? "selected" : "" %>>Admin</option>
  </select><br>
  <label>Is Active:</label>
  <input type="checkbox" name="isActive" <%= userToEdit.getIsActive() ? "checked" : "" %>><br>
  <% } else { %>
  <input type="hidden" name="phoneNumber" value="<%= userToEdit.getPhoneNumber() != null ? userToEdit.getPhoneNumber() : "" %>">
  <input type="hidden" name="userType" value="<%= userToEdit.getType().name() %>">
  <input type="hidden" name="isActive" value="<%= userToEdit.getIsActive() ? "true" : "false" %>">
  <% } %>
  <button type="submit">Update</button>
</form>
<a href="index.jsp">Back to Home</a>
<% if (session.getAttribute("error") != null) { %>
<p style="color:red;"><%= session.getAttribute("error") %></p>
<% session.removeAttribute("error"); %>
<% } %>
</body>
</html>