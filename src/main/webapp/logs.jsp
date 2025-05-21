<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<html>
<head>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
  <title>Access Logs</title>
  <%@ include file="header.jsp" %>
</head>
<body>
<pref-header></pref-header>

<div>
  <h2>Access Logs</h2>
  <form class="form-inline mb-3" action="${pageContext.request.contextPath}/logs" method="get">
    <input type="date" name="date" class="form-control mr-2" value="${param.date}" required>
    <button type="submit" class="btn btn-primary">Filter</button>
    <a class="btn btn-secondary ml-2" href="${pageContext.request.contextPath}/logs">Clear</a>
    <a class="btn btn-success ml-2" href="${pageContext.request.contextPath}/profile-dashboard.jsp">Back</a>
  </form>
</div>

<table class="table table-striped">
  <thead class="thead-dark">
  <tr>
    <th>Login Time</th>
    <th>Logout Time</th>
  </tr>
  </thead>

  <%--show logs from back-end--%>
  <tbody>
  <c:forEach items="${logs}" var="log">
    <tr>
      <td><fmt:formatDate value="${log.loginTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
      <td>
        <c:choose>
          <c:when test="${not empty log.logoutTime}">
            <fmt:formatDate value="${log.logoutTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
          </c:when>
          <c:otherwise>N/A</c:otherwise>
        </c:choose>
      </td>
    </tr>
  </c:forEach>
  </tbody>
</table>
</body>
</html>
