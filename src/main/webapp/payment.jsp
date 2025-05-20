<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="uts.isd.model.User" %>
<%@ page import="uts.isd.model.Order" %>
<!DOCTYPE html>
<html>
<head>
    <title>Payment</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <pref-header></pref-header>
    
    <div class="container">
        <h1>Payment Details</h1>
        
        <%
            User user = (User) session.getAttribute("user");
            Order order = (Order) session.getAttribute("order");
            
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            if (order == null) {
                response.sendRedirect("order?action=list");
                return;
            }
        %>
        
        <form action="payment" method="post" class="form-container">
            <input type="hidden" name="action" value="process">
            <input type="hidden" name="orderId" value="<%= order.getId() %>">
            <input type="hidden" name="amount" value="<%= order.getTotalPrice() %>">
            
            <div class="form-group">
                <label for="paymentMethod">Payment Method</label>
                <select name="paymentMethod" id="paymentMethod" required>
                    <option value="Credit Card">Credit Card</option>
                    <option value="Debit Card">Debit Card</option>
                </select>
            </div>
            
            <div class="form-group">
                <label for="cardNumber">Card Number</label>
                <input type="text" id="cardNumber" name="cardNumber" 
                       pattern="[0-9]{16}" maxlength="16" required
                       placeholder="Enter 16-digit card number">
            </div>
            
            <div class="form-group">
                <label for="cardHolderName">Card Holder Name</label>
                <input type="text" id="cardHolderName" name="cardHolderName" required
                       placeholder="Enter name as shown on card">
            </div>
            
            <div class="form-group">
                <label for="expiryDate">Expiry Date</label>
                <input type="text" id="expiryDate" name="expiryDate" 
                       pattern="(0[1-9]|1[0-2])\/([0-9]{2})" maxlength="5" required
                       placeholder="MM/YY">
            </div>
            
            <div class="form-group">
                <label for="cvv">CVV</label>
                <input type="text" id="cvv" name="cvv" 
                       pattern="[0-9]{3,4}" maxlength="4" required
                       placeholder="Enter 3 or 4 digit CVV">
            </div>
            
            <div class="form-group">
                <h3>Total Amount: $<%= String.format("%.2f", order.getTotalPrice()) %></h3>
            </div>
            
            <div class="form-group">
                <input type="submit" value="Process Payment" class="buttons">
                <a href="order?action=list"><button type="button" class="buttons">Cancel</button></a>
            </div>
        </form>
    </div>
    
    <script>
        // Format card number with spaces
        document.getElementById('cardNumber').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\s+/g, '').replace(/[^0-9]/gi, '');
            e.target.value = value;
        });
        
        // Format expiry date
        document.getElementById('expiryDate').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\s+/g, '').replace(/[^0-9]/gi, '');
            if (value.length >= 2) {
                value = value.substring(0,2) + '/' + value.substring(2);
            }
            e.target.value = value;
        });
        
        // Format CVV
        document.getElementById('cvv').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\s+/g, '').replace(/[^0-9]/gi, '');
            e.target.value = value;
        });
    </script>
</body>
</html> 