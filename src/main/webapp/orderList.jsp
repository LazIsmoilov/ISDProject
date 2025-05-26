<%--
  Created by IntelliJ IDEA.
  User: Zheyuan Yao
  Date: 2025/5/7
  Time: 23:31
  To change this template use File | Settings | File Templates.
--%>
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
    // 用脚本式循环替代 JSTL
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    if (orders != null) {
      for (Order o : orders) {
  %>
  <tr>
    <td><%= o.getId() %></td>
    <td><%= o.getOrderDate() %></td>
    <td><%= o.getTotalPrice() %></td>
    <td><%= o.getStatus() %></td>
    <td>
      <a href="order?action=detail&userId=<%= o.getId() %>">Details</a>
      <% if (!"Cancelled".equals(o.getStatus())) { %>
      | <a href="order?action=cancel&userId=<%= o.getId() %>"
           onclick="return confirm('Cancel order #<%= o.getId() %>?');">Cancel</a>
      | <a href="shipment.jsp?orderId=<%= o.getId() %>">Shipment</a> <!-- ✅ 新增 -->
      <!--int orderId = Integer.parseInt(request.getParameter("orderId"));

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


