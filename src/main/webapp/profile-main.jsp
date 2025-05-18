<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<html>
<head>
  <title>User Dashboard</title>
  <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
  <style>
    .dashboard-card {
      transition: transform 0.2s;
      min-height: 150px;
    }
    .dashboard-card:hover {
      transform: translateY(-5px);
    }
  </style>
</head>
<body class="bg-light">
<pref-header></pref-header>
<div class="container mt-5">


    <!-- Login Logs Card -->
    <div class="col">
      <div class="card dashboard-card shadow">
        <div class="card-body text-center">
          <h5 class="card-title">Login History</h5>
          <p class="card-text">View your recent login activities</p>
          <a href="${pageContext.request.contextPath}/logs" class="btn btn-primary">View Logs</a>
        </div>
      </div>
    </div>

    <!-- Profile Management Card -->
    <div class="col">
      <div class="card dashboard-card shadow">
        <div class="card-body text-center">
          <h5 class="card-title">Profile Settings</h5>
          <p class="card-text">Update your personal information</p>
          <a href="${pageContext.request.contextPath}/profile" class="btn btn-success">Profile Info</a>
        </div>
      </div>
    </div>
  </div>
<div class="text-center mt-4">
  <a href="${pageContext.request.contextPath}/main.jsp" class="btn btn-success">Back</a>
</div>

<%@ include file="header.jsp" %>
</body>
</html>
