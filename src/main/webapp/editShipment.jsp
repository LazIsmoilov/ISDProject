<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Edit Shipment</title>
  <style>
    body { font-family: Arial, sans-serif; background: #f9f9f9; padding: 30px; }
    form { background: white; padding: 20px; border-radius: 10px; width: 400px; margin: auto; box-shadow: 0 0 10px #ccc; }
    input[type="text"], input[type="date"] { width: 100%; padding: 8px; margin: 10px 0; box-sizing: border-box; }
    input[type="submit"] { background-color: #28a745; color: white; padding: 10px; width: 100%; border: none; border-radius: 5px; }
    .error { color: red; }
  </style>
</head>
<body>
<form action="editShipment" method="post">
  <h2>Edit Shipment</h2>
  <input type="hidden" name="shipmentId" value="${param.shipmentId}" />
  <input type="hidden" name="orderId" value="${param.orderId}" />

  <label>Shipment Method:</label>
  <input type="text" name="method" value="${param.method}" required />

  <label>Shipment Date:</label>
  <input type="date" name="shipmentDate" value="${param.shipmentDate}" required />

  <label>Shipping Address:</label>
  <input type="text" name="address" value="${param.address}" required />

  <input type="submit" value="Update Shipment" />

  <c:if test="${not empty error}">
    <p class="error">${error}</p>
  </c:if>
</form>
</body>
</html>
