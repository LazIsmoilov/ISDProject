<%--
  Created by IntelliJ IDEA.
  User: Zheyuan Yao
  Date: 2025/5/7
  Time: 23:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List, uts.isd.model.OrderItem" %>
<jsp:include page="header.jsp" />

<style>
    table { border-collapse: collapse; width: 100%; margin-top: 1em; }
    th, td { border: 1px solid #333; padding: 8px; text-align: left; }
    th { background-color: #f2f2f2; }
</style>

<h2>Order Details — #<%= ((uts.isd.model.Order)request.getAttribute("order")).getOrderId() %></h2>

<%
    uts.isd.model.Order order = (uts.isd.model.Order) request.getAttribute("order");
%>
<p><strong>Order Date:</strong> <%= order.getOrderDate() %></p>
<p><strong>Total Price:</strong> <%= order.getTotalAmount() %></p>
<p><strong>Status:</strong> <%= order.getStatus() %></p>
<p>
    <a href="viewShipment?orderId=<%= order.getOrderId() %>">📦 Manage Shipment for this Order</a>
</p>


<h3>Order Items</h3>
<table>
    <tr><th>Product ID</th><th>Quantity</th><th>Unit Price</th></tr>
    <%
        List<OrderItem> items = (List<OrderItem>) request.getAttribute("items");
        if (items != null) {
            for (OrderItem item : items) {
    %>
    <tr>
        <td><%= item.getProductId() %></td>
        <td><%= item.getQuantity() %></td>
        <td><%= item.getUnitPrice() %></td>
    </tr>
    <%
            }
        }
    %>
</table>
