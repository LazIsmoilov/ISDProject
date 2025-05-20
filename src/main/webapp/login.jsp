<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>Login</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>

<div class="container mt-5">
  <h2>Login</h2>

  <c:if test="${not empty sessionScope.loginError}">
    <div class="alert alert-danger">${sessionScope.loginError}</div>
  </c:if>

  <form action="login" method="post">
    <div class="form-group">
      <label for="email">Email</label>
      <input type="email" class="form-control" name="email" required>
    </div>

    <div class="form-group">
      <label for="password">Password</label>
      <input type="password" class="form-control" name="password" required>
    </div>

    <button type="submit" class="btn btn-primary">Login</button>
  </form>

  <p class="mt-3">Don’t have an account? <a href="register.jsp">Register here</a></p>
</div>

</body>
</html>
