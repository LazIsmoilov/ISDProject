<%--
  Created by IntelliJ IDEA.
  User: Zheyuan Yao
  Date: 2025/5/13
  Time: 3:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head><title>Test Add to Cart</title></head>
<body>
<h2>Test: Add Item to Cart</h2>
<form method="post" action="cart">
  <label>Product ID:
    <input type="number" name="productId" value="101" required>
  </label><br>
  <label>Quantity:
    <input type="number" name="quantity" value="1" required>
  </label><br>
  <label>Unit Price:
    <input type="number" step="0.01" name="unitPrice" value="49.99" required>
  </label><br>
  <button type="submit">Add to Cart</button>
</form>
<p><a href="order.jsp">Go to Checkout</a></p>
</body>
</html>

