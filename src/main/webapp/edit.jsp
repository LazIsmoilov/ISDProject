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

  int idToEdit = loggedInUser.getUserId();

  DAO dao = (DAO) session.getAttribute("db");
  if (dao == null) {
    session.setAttribute("error", "Database not initialized in session");
    response.sendRedirect("index.jsp");
    return;
  }

  UserDBManager userDbManager = dao.Users();
  if (userDbManager == null) {
    session.setAttribute("error", "UserDBManager not initialized");
    response.sendRedirect("index.jsp");
    return;
  }

  User userToEdit;
  try {
    userToEdit = userDbManager.getById(idToEdit);
    if (userToEdit == null) {
      session.setAttribute("error", "User not found for ID: " + idToEdit);
      response.sendRedirect("index.jsp");
      return;
    }
  } catch (Exception e) {
    session.setAttribute("error", "Database error: " + e.getMessage());
    response.sendRedirect("index.jsp");
    return;
  }

  // Debugging logs
  System.out.println("DAO: " + dao);
  System.out.println("UserDBManager: " + userDbManager);
  System.out.println("LoggedInUser ID: " + loggedInUser.getUserId());
  System.out.println("UserToEdit: " + userToEdit);
%>
<!DOCTYPE html>
<html>
<head>
  <title>Edit Profile</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>
<h2>Edit Your Profile (ID: <%= userToEdit.getUserId() %>, Name: <%= userToEdit.getFullName() %>)</h2>
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
  <input type="text" name="phone" value="<%= userToEdit.getPhone() %>"><br>
  <label>User Type:</label>
  <select name="role">
    <option value="customer" <%= userToEdit.getType() == UserType.CUSTOMER ? "selected" : "" %>>Customer</option>
    <option value="staff" <%= userToEdit.getType() == UserType.STAFF ? "selected" : "" %>>Staff</option>
  </select><br>
  <label>Is Active:</label>
  <input type="checkbox" name="isActive" <%= userToEdit.getIsActive() ? "checked" : "" %>><br>

  <button type="submit">Update</button>
</form>
<a href="index.jsp">Back to Home</a>
<% if (session.getAttribute("error") != null) { %>
<p style="color:red;"><%= session.getAttribute("error") %></p>
<% session.removeAttribute("error"); %>
<% } %>
</body>
</html>