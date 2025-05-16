<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List, uts.isd.model.Order" %>
<jsp:include page="header.jsp" />
<pref-header></pref-header>

<style>
  table { border-collapse: collapse; width: 100%; margin-top: 1em; }
  th, td { border: 1px solid #333; padding: 8px; text-align: left; }
  th { background-color: #f2f2f2; }
</style>

<h2>My Orders</h2>

<table>
  <tr>
    <th>Order ID</th>
    <th>Order Date</th>
    <th>Total Price</th>
    <th>Status</th>
    <th>Actions</th>
  </tr>
  <%
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    if (orders != null) {
      for (Order o : orders) {
        int orderId = o.getId();
  %>
  <tr>
    <td><%= orderId %></td>
    <td><%= o.getOrderDate() %></td>
    <td><%= o.getTotalPrice() %></td>
    <td><%= o.getStatus() %></td>
    <td>
      <a href="order?action=detail&id=<%= orderId %>">Details</a>
      <% if (!"Cancelled".equals(o.getStatus())) { %>
      | <a href="order?action=cancel&id=<%= orderId %>"
           onclick="return confirm('Cancel order #<%= orderId %>?');">Cancel</a>
      | <a href="createShipment?orderId=<%= orderId %>">Shipment</a>
      <% } else { %>
      | <span style="color: gray;">Cancelled</span>
      <% } %>
    </td>
  </tr>
  <%
      }
    }
  %>
</table>
