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

<h2>Checkout</h2>

<%
  // 模拟从 Session 里拿购物车
  List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
  if(cart == null || cart.isEmpty()) {
%>
<p>Your cart is empty. <a href="main.jsp">Go shopping</a></p>
<%
    return;
  }
  // 计算总价
  double total = 0;
  for(CartItem ci : cart) total += ci.getUnitPrice() * ci.getQuantity();
%>

<table style="border-collapse: collapse; margin-bottom:1em;">
  <tr><th>Product ID</th><th>Quantity</th><th>Unit Price</th><th>Subtotal</th></tr>
  <% for(CartItem ci : cart){ %>
  <tr>
    <td><%=ci.getProductId()%></td>
    <td><%=ci.getQuantity()%></td>
    <td><%=ci.getUnitPrice()%></td>
    <td><%=ci.getUnitPrice()*ci.getQuantity()%></td>
  </tr>
  <% } %>
  <tr>
    <td colspan="3" style="text-align:right;"><strong>Total:</strong></td>
    <td><strong><%=total%></strong></td>
  </tr>
</table>

<form method="post" action="order">
  <!-- 把明细当数组传给后端 -->
  <% for(int i=0; i<cart.size(); i++){ CartItem ci = cart.get(i); %>
  <input type="hidden" name="productId" value="<%=ci.getProductId()%>" />
  <input type="hidden" name="quantity" value="<%=ci.getQuantity()%>" />
  <input type="hidden" name="unitPrice" value="<%=ci.getUnitPrice()%>" />
  <% } %>
  <input type="hidden" name="totalAmount" value="<%=total%>" />
  <button type="submit">Place Order</button>
</form>
