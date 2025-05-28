<%@ page import="uts.isd.model.User" %>
<%@ page import="uts.isd.model.User.UserType" %>
<%@ page import="uts.isd.model.dao.UserDBManager" %>
<%@ page import="uts.isd.model.dao.DAO" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
  User loggedInUser = (User) session.getAttribute("loggedInUser");
  if (loggedInUser == null) {
    response.sendRedirect("login.jsp");
    return;
  }

  // Check if the user is an admin
  if (!loggedInUser.isAdmin()) {
    session.setAttribute("error", "You do not have permission to access this page.");
    response.sendRedirect("index.jsp");
    return;
  }

  // Get userId from URL parameter
  String userIdParam = request.getParameter("userId");
  int idToEdit;
  try {
    idToEdit = Integer.parseInt(userIdParam);
  } catch (NumberFormatException e) {
    session.setAttribute("error", "Invalid user ID provided.");
    response.sendRedirect("admin.jsp");
    return;
  }

  DAO dao = (DAO) session.getAttribute("db");
  if (dao == null) {
    session.setAttribute("error", "Database not initialized in session");
    response.sendRedirect("admin.jsp");
    return;
  }

  UserDBManager userDbManager = dao.Users();
  if (userDbManager == null) {
    session.setAttribute("error", "UserDBManager not initialized");
    response.sendRedirect("admin.jsp");
    return;
  }

  User userToEdit;
  try {
    userToEdit = userDbManager.getById(idToEdit);
    if (userToEdit == null) {
      session.setAttribute("error", "User not found for ID: " + idToEdit);
      response.sendRedirect("admin.jsp");
      return;
    }
  } catch (Exception e) {
    session.setAttribute("error", "Database error: " + e.getMessage());
    response.sendRedirect("admin.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html>
<head>
  <title>Edit User Profile</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>
<h2>Edit User Profile (ID: <%= userToEdit.getUserId() %>, Name: <%= userToEdit.getFullName() %>)</h2>
<form action="EditServlet" method="post">
  <input type="hidden" name="userId" value="<%= userToEdit.getUserId() %>">
  <label>Name:</label>
  <input type="text" name="name" value="<%= userToEdit.getFullName() %>" required><br>
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
  <label>Phone Number:</label>
  <input type="text" name="phone" value="<%= userToEdit.getPhone() != null ? userToEdit.getPhone() : "" %>"><br>
  <label>User Type:</label>
  <select name="role">
    <option value="customer" <%= userToEdit.getType() == UserType.CUSTOMER ? "selected" : "" %>>Customer</option>
    <option value="staff" <%= userToEdit.getType() == UserType.STAFF ? "selected" : "" %>>Staff</option>
    <option value="admin" <%= userToEdit.getType() == UserType.ADMIN ? "selected" : "" %>>Admin</option>
  </select><br>
  <label>Is Active:</label>
  <input type="checkbox" name="isActive" <%= userToEdit.getIsActive() ? "checked" : "" %>><br>

  <button type="submit">Update</button>
</form>
<a href="admin.jsp">Back to Admin Dashboard</a>
<% if (session.getAttribute("error") != null) { %>
<p style="color:red;"><%= session.getAttribute("error") %></p>
<% session.removeAttribute("error"); %>
<% } %>
<% if (session.getAttribute("message") != null) { %>
<p style="color:green;"><%= session.getAttribute("message") %></p>
<% session.removeAttribute("message"); %>
<% } %>
</body>
</html>