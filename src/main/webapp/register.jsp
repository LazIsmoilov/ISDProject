<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<html>
<head>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
  <title>Login</title>
</head>
<body class="container mt-5">
<div class="row justify-content-center">
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <h2 class="mb-4">User Register</h2>
        <c:if test="${not empty error}">
          <div class="alert alert-danger">${error}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/register" method="post">
          <div class="form-group">
            <label>Full Name</label>
            <input type="text" name="fullName" class="form-control" required placeholder="Enter your fullname">
          </div>

          <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" class="form-control" required placeholder="Enter your email">
            <small class="form-text text-muted">Enter an unregistered email(xxx@gmail.com)</small>
          </div>

          <div class="form-group">
            <label>Password</label>
            <input type="password" name="password" class="form-control" required minlength="8" placeholder="Enter your password">
            <small class="form-text text-muted">More than 8 characters </small>
          </div>

          <div class="form-group">
            <label>Phone</label>
            <input type="tel" name="phone" class="form-control" required pattern="[0-9]{10}" placeholder="Enter your phone number">
            <small class="form-text text-muted">Enter a 10-digit number(04**-***-***)</small>
          </div>

          <div class="form-group">
            <label>role</label>
            <select name="role" class="form-control" required >
              <option value=""></option>
              <option value="customer">customer</option>
              <option value="staff">staff</option>
              <option value="admin">admin</option>
            </select>
            <small class="form-text text-muted">Please choose your role</small>
          </div>

          <button type="submit" class="btn btn-primary btn-block">Register</button>
          <a class="btn btn-success btn-block" href="${pageContext.request.contextPath}/login.jsp" >back</a>

        </form>
      </div>
    </div>
  </div>
</div>
</body>
</html>


