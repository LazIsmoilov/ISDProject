<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<html>
<head>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
  <title>Profile</title>
</head>
<body class="container mt-5">

<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
  <div class="container-fluid">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/profile-main.jsp">My Info</a>
    <div class="d-flex">
      <span class="text-white me-3">Welcome, ${sessionScope.user.fullName}</span>
      <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light">Logout</a>
    </div>
  </div>
</nav>

<div class="row justify-content-center">
  <div class="container mt-4">
    <div class="card mb-4">
      <div class="card-body">

        <!-- ✅ 修改部分：添加成功提示 + 清除 -->
        <c:if test="${not empty success}">
          <div class="alert alert-success alert-dismissible fade show" role="alert">
              ${success}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
          <c:remove var="success" scope="request"/> <!-- ✅ 防止刷新后重复显示 -->
        </c:if>
        <!-- ✅ 修改结束 -->

        <h5 class="card-title">Profile Information</h5>
        <p>Name: ${user.fullName}</p>
        <p>Email: ${user.email}</p>
        <p>Phone: ${user.phone}</p>
        <a href="${pageContext.request.contextPath}/profile" class="btn btn-primary">Edit Profile</a>
        <a href="${pageContext.request.contextPath}/profile-main.jsp" class="btn btn-success ms-2">Back</a>

      </div>
    </div>
  </div>
</div>

<!-- ✅ 引入 Bootstrap 以支持 alert dismiss -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>