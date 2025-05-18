<%--
  Created by IntelliJ IDEA.
  User: Zheyuan Yao
  Date: 2025/5/7
  Time: 23:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="uts.isd.model.User, java.util.List, uts.isd.model.Order" %>
<jsp:include page="header.jsp" />
<pref-header></pref-header>
<style>
  table { border-collapse: collapse; width: 100%; margin-top: 1em; }
  th, td { border: 1px solid #333; padding: 8px; text-align: left; }
  th { background-color: #f2f2f2; }
</style>

<h2>My Orders</h2>
<%
  User currentUser = (User) session.getAttribute("user");
  List<Order> orders = (List<Order>) request.getAttribute("orders");
%>
<p>当前用户 ID：<%= currentUser.getId() %></p>
<p>查询到的订单数：<%= orders == null ? 0 : orders.size() %></p>

<table>
  <tr>
    <th>Order ID</th>
    <th>Order Date</th>
    <th>Total Price</th>
    <th>Status</th>
    <th>Actions</th>
  </tr>
  <%

    if (orders != null) {
      for (Order o : orders) {
  %>
  <tr>
    <td><%= o.getId() %></td>
    <td><%= o.getOrderDate() %></td>
    <td><%= o.getTotalPrice() %></td>
    <td><%= o.getStatus() %></td>
    <td>
      <a href="order?action=detail&id=<%= o.getId() %>">Details</a>
      <% if (!"Cancelled".equals(o.getStatus())) { %>
      | <a href="order?action=cancel&id=<%= o.getId() %>"
           onclick="return confirm('Cancel order #<%= o.getId() %>?');">Cancel</a>
      | <a href="shipment.jsp?orderId=<%= o.getId() %>">Shipment</a>
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


