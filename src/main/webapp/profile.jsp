<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Edit Profile</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
  <%@ include file="header.jsp" %>
</head>
<body>
<pref-header></pref-header>
<div class="container">

  <div class="profile-container">
    <h2 class="mb-4">Edit Profile</h2>


    <%
      User user = (User) session.getAttribute("user");

      if (user == null) {
        response.sendRedirect("login.jsp");
        return;
      }
    %>

    <!-- Success/Error -->
    <c:if test="${not empty success}">
      <div class="alert alert-success alert-dismissible fade show">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
      <div class="alert alert-danger alert-dismissible fade show">${error}</div>
    </c:if>

    <!-- Save Changes Form -->
    <form action="${pageContext.request.contextPath}/profile" method="post" userId="profileForm">
      <input type="hidden" name="userId" value="${user.userId}">

      <div class="form-group required">
        <label for="fullName">Full Name</label>
        <input type="text" class="form-control" id="fullName" name="fullName"
               value="${user.fullName}" required>
      </div>

      <div class="form-group required">
        <label for="email">Email Address</label>
        <input type="email" class="form-control" id="email" name="email"
               value="${user.email}" required readonly>
        <small class="form-text text-muted">Cannot be changed once registered</small>
      </div>

      <div class="form-group required">
        <label for="phone">Phone Number</label>
        <input type="tel" class="form-control" id="phone" name="phone"
               value="${user.phone}" required pattern="[0-9]{10}">
        <small class="form-text text-muted">Enter a 10-digit number(04**-***-***)</small>
      </div>

      <div class="form-group">
        <label for="newPassword">New Password</label>
        <input type="password" class="form-control" id="newPassword"
               name="newPassword" minlength="8">
      </div>


      <div class="form-group mt-4 d-flex justify-content-between">
        <button type="submit" class="btn btn-primary btn-block">Save Changes</button>
      </div>
    </form>

    <!-- Delete Form -->
    <form action="${pageContext.request.contextPath}/delete" method="post"
          onsubmit="return confirm('Are you sure you want to delete your account? This action is irreversible.');"
          class="mt-2">
      <button type="submit" class="btn btn-danger btn-block">Delete Account</button>
    </form>

    <!-- Back Button -->
    <div class="mt-2">
      <a class="btn btn-success btn-block" href="${pageContext.request.contextPath}/profile-dashboard.jsp">Back</a>
    </div>
  </div>
</div>

</body>
</html>