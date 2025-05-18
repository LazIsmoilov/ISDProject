<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Edit Profile</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
  <style>
    .profile-container {
      max-width: 600px;
      margin: 2rem auto;
      padding: 2rem;
      background: #f8f9fa;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .form-group.required label:after {
      content: "*";
      color: #dc3545;
      margin-left: 4px;
    }
    .btn-block {
      width: 100%;
    }
  </style>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">

  <!-- Nav -->
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container-fluid">
      <a class="navbar-brand" href="${pageContext.request.contextPath}/profile-main.jsp">My Info</a>
      <div class="d-flex">
        <span class="text-white me-3">Welcome, ${sessionScope.user.fullName}</span>
      </div>
    </div>
  </nav>

  <div class="profile-container">
    <h2 class="mb-4">Edit Profile</h2>

    <!-- Success/Error -->
    <c:if test="${not empty success}">
      <div class="alert alert-success alert-dismissible fade show">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
      <div class="alert alert-danger alert-dismissible fade show">${error}</div>
    </c:if>

    <!-- Save Changes Form -->
    <form action="${pageContext.request.contextPath}/update-profile" method="post" id="profileForm">
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
          onsubmit="return confirm('Are you sure you want to delete your account? This cannot be undone.');"
          class="mt-2">
      <button type="submit" class="btn btn-danger btn-block">Delete Account</button>
    </form>

    <!-- Back Button -->
    <div class="mt-2">
      <a class="btn btn-success btn-block" href="${pageContext.request.contextPath}/profile-main.jsp">Back</a>
    </div>
  </div>
</div>

</script>
</body>
</html>