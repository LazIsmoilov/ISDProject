<%--
  Created by IntelliJ IDEA.
  User: laz
  Date: 30/4/2025
  Time: 4:03 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="uts.isd.model.User" %>
<%@ page import="uts.isd.model.dao.DBManager" %>
<%@ page import="uts.isd.model.dao.DBConnector" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %><%
  User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect("login.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Edit Profile - IoT Bay</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="form-container">
  <h1>Edit Profile</h1>
  <form action="EditServlet" method="post">
    <div class="form-group">
      <label for="name">Full Name</label>
      <input type="text" id="name" name="name" value="<%= user.getName() %>" required>
    </div>
    <div class="form-group">
      <label for="email">Email Address</label>
      <input type="email" id="email" name="email" value="<%= user.getEmail() %>" required>
    </div>
    <div class="form-group">
      <label for="password">Password</label>
      <input type="password" id="password" name="password" value="<%= user.getPassword() %>" required>
    </div>
    <div class="form-group">
      <label for="dob">Date of Birth</label>
      <input type="date" id="dob" name="dob" value="<%= user.getDob() %>" required>
    </div>
    <div class="form-group">
      <label for="gender">Gender</label>
      <select id="gender" name="gender" required>
        <option value="male" <%= "male".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Male</option>
        <option value="female" <%= "female".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Female</option>
        <option value="other" <%= "other".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Other</option>
      </select>
    </div>
    <div style="text-align: center;">
      <button type="submit" class="submit-btn">Update Profile</button>
      <form method="post" action="${pageContext.request.contextPath}/DeleteServlet">
        <button>Delete my Account</button>
      </form>
    </div>
  </form>
</div>
</body>
</html>
