<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Shipment List</title>
  <style>
    body { font-family: Arial, sans-serif; background: #f0f0f0; padding: 30px; }
    .container { width: 90%; margin: auto; }
    table { width: 100%; border-collapse: collapse; background: white; }
    th, td { padding: 10px; border: 1px solid #ddd; text-align: center; }
    th { background-color: #f2f2f2; }
    .action a { margin: 0 5px; text-decoration: none; color: #007BFF; }
    .success { color: green; }
    .error { color: red; }
    form { margin-bottom: 20px; text-align: center; }
    input[type="text"] { padding: 8px; width: 300px; }
    input[type="submit"] { padding: 8px 15px; background: #007BFF; color: white; border: none; border-radius: 5px; }
  </style>
</head>
<body>
<div class="container">
  <h2>Shipment List for Order #${orderId}</h2>

  <form action="searchShipment" method="get">
    <input type="text" name="keyword" placeholder="Search by ID or Date" />
    <input type="submit" value="Search" />
  </form>

  <c:if test="${not empty successMsg}">
    <p class="success">${successMsg}</p>
  </c:if>
  <c:if test="${not empty error}">
    <p class="error">${error}</p>
  </c:if>
  <c:if test="${not empty infoMsg}">
    <p>${infoMsg}</p>
  </c:if>

  <c:if test="${not empty shipments}">
    <table>
      <tr>
        <th>ID</th>
        <th>Method</th>
        <th>Date</th>
        <th>Address</th>
        <th>Actions</th>
      </tr>
      <c:forEach var="s" items="${shipments}">
        <tr>
          <td>${s.shipmentId}</td>
          <td>${s.method}</td>
          <td>${s.shipmentDate}</td>
          <td>${s.address}</td>
          <td class="action">
            <a href="editShipment.jsp?shipmentId=${s.shipmentId}&orderId=${s.orderId}&method=${s.method}&shipmentDate=${s.shipmentDate}&address=${s.address}">Edit</a>
            <a href="deleteShipment?shipmentId=${s.shipmentId}&orderId=${s.orderId}">Delete</a>
          </td>
        </tr>
      </c:forEach>
    </table>
  </c:if>
</div>
</body>
</html>
