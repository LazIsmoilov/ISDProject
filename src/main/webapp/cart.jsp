<%--
  Created by IntelliJ IDEA.
  User: Zheyuan Yao
  Date: 2025/5/21
  Time: 17:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, uts.isd.model.CartItem" %>
<jsp:include page="header.jsp" />
<pref-header></pref-header>

<h2>Your Shopping Cart</h2>

<%
  List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
  double total = 0;
%>

<% if (cart == null || cart.isEmpty()) { %>
<p>Your cart is empty.</p>
<% } else { %>
<table border="1" cellpadding="8" cellspacing="0">
  <tr>
    <th>Product ID</th>
    <th>Quantity</th>
    <th>Unit Price</th>
    <th>Subtotal</th>
  </tr>

  <% for (CartItem item : cart) {
    double subtotal = item.getQuantity() * item.getUnitPrice();
    total += subtotal;
  %>
  <tr>
    <td><%= item.getProductId() %></td>
    <td><%= item.getQuantity() %></td>
    <td><%= item.getUnitPrice() %></td>
    <td><%= subtotal %></td>
  </tr>
  <% } %>

  <tr>
    <td colspan="3" align="right"><strong>Total:</strong></td>
    <td><%= String.format("%.2f", total) %></td>

  </tr>
</table>

<form action="order.jsp" method="post">
  <button type="submit">✅ Checkout</button>
</form>

<% } %>
