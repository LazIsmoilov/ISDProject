<%--
  Created by IntelliJ IDEA.
  User: Zheyuan Yao
  Date: 2025/5/8
  Time: 0:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List, uts.isd.model.CartItem" %>
<jsp:include page="header.jsp" />
<pref-header></pref-header>

<h2>🧾 Order Summary</h2>

<%
  List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
  if(cart == null || cart.isEmpty()) {
%>
<p>Your cart is empty. <a href="main.jsp">Go shopping</a></p>
<%
    return;
  }

  double total = 0;
  for(CartItem ci : cart) total += ci.getUnitPrice() * ci.getQuantity();
%>

<table border="1" cellpadding="8" cellspacing="0">
  <tr><th>Product ID</th><th>Quantity</th><th>Unit Price</th><th>Subtotal</th></tr>
  <% for(CartItem ci : cart){ %>
  <tr>
    <td><%= ci.getProductId() %></td>
    <td><%= ci.getQuantity() %></td>
    <td><%= String.format("%.2f", ci.getUnitPrice()) %></td>
    <td><%= String.format("%.2f", ci.getUnitPrice() * ci.getQuantity()) %></td>
  </tr>
  <% } %>
  <tr>
    <td colspan="3" align="right"><strong>Total:</strong></td>
    <td><strong><%= String.format("%.2f", total) %></strong></td>
  </tr>
</table>

<form method="post" action="order">
  <% for(CartItem ci : cart) { %>
  <input type="hidden" name="productId" value="<%= ci.getProductId() %>" />
  <input type="hidden" name="quantity" value="<%= ci.getQuantity() %>" />
  <input type="hidden" name="unitPrice" value="<%= ci.getUnitPrice() %>" />
  <% } %>
  <input type="hidden" name="totalPrice" value="<%= String.format("%.2f", total) %>" />
  <button type="submit">✅ Confirm Order</button>
</form>

