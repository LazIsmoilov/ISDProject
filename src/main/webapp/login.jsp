<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>Login</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
  <%@ include file="header.jsp" %>
</head>
<body>
<pref-header></pref-header>

<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-6">
      <h2 class="mb-4">User Login</h2>


      <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
      </c:if>


      <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="form-group">
          <label>Email:</label>
          <input type="email" name="email" class="form-control" required placeholder="Enter your email" >
        </div>

        <div class="form-group">
          <label>Password:</label>
          <input type="password" name="password" class="form-control" required placeholder="Enter your password">
        </div>
        <button type="submit" class="btn btn-primary">Login</button>
        <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-link">Register</a>
      </form>
    </div>
  </div>
</div>
</body>
</html>