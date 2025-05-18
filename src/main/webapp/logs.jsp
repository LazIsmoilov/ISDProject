<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<html>
<head>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
  <title>Access Logs</title>
</head>
<body class="container mt-4">

<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
  <div class="container-fluid">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/profile-main.jsp">My Info</a>
    <div class="d-flex">
      <span class="text-white me-3">Welcome, ${sessionScope.user.fullName}</span>

    </div>
  </div>
</nav>

<div class="mb-4">
  <h2>Access Logs</h2>
  <form class="form-inline" action="${pageContext.request.contextPath}/logs">
    <input type="date" name="date" class="form-control mr-2"  value="${param.date}">
    <button type="submit" class="btn btn-primary">Filter</button>
    <a class="btn btn-success" href="${pageContext.request.contextPath}/profile-main.jsp" style="margin-left: 5px">back</a>
  </form>
</div>

<table class="table table-striped">
  <thead class="thead-dark">
  <tr>
    <th>Login Time</th>
    <th>Logout Time</th>
    <th>Duration</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach items="${logs}" var="log">
    <tr>
      <td><fmt:formatDate value="${log.loginTime}" pattern="yyyy-MM-dd HH:mm"/></td>
      <td>
        <c:choose>
          <c:when test="${not empty log.logoutTime}">
            <fmt:formatDate value="${log.logoutTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
          </c:when>
          <c:otherwise>N/A</c:otherwise>
        </c:choose>
      </td>
      <td>
        <c:if test="${not empty log.logoutTime}">
          ${(log.logoutTime.time - log.loginTime.time) / (1000 * 60)} mins
        </c:if>
      </td>
    </tr>
  </c:forEach>
  </tbody>
</table>
</body>
</html>
