<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>Register</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>

<div class="container mt-5">
  <h2>Register</h2>

  <c:if test="${not empty sessionScope.registerError}">
    <div class="alert alert-danger">${sessionScope.registerError}</div>
  </c:if>
  <c:if test="${not empty sessionScope.registerSuccess}">
    <div class="alert alert-success">${sessionScope.registerSuccess}</div>
  </c:if>

  <form action="register" method="post">
    <div class="form-group">
      <label for="fullName">Full Name</label>
      <input type="text" class="form-control" name="fullName" required>
    </div>

    <div class="form-group">
      <label for="email">Email</label>
      <input type="email" class="form-control" name="email" required>
    </div>

    <div class="form-group">
      <label for="password">Password</label>
      <input type="password" class="form-control" name="password" required>
    </div>

    <div class="form-group">
      <label for="role">Role</label>
      <select class="form-control" name="role" required>
        <option value="customer">Customer</option>
        <option value="staff">Staff</option>
      </select>
    </div>

    <button type="submit" class="btn btn-primary">Register</button>
  </form>

  <p class="mt-3">Already have an account? <a href="login.jsp">Login here</a></p>
</div>

</body>
</html>
