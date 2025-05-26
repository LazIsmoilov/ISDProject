<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="uts.isd.model.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Payment</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="container">
        <h1>Payment Details</h1>
        
        <% if (session.getAttribute("paymentError") != null) { %>
            <div class="payment-error">
                <%= session.getAttribute("paymentError") %>
                <% session.removeAttribute("paymentError"); %>
            </div>
        <% } %>
        
        <form class="payment-form" action="payment" method="post">
            <input type="hidden" name="action" value="process">
            <input type="hidden" name="orderId" value="<%= request.getParameter("orderId") %>">
            
            <div class="form-group">
                <label for="amount">Amount to Pay</label>
                <input type="number" id="amount" name="amount" value="<%= request.getParameter("amount") %>" readonly>
            </div>
            
            <div class="form-group">
                <label for="paymentMethod">Payment Method</label>
                <select id="paymentMethod" name="paymentMethod" required>
                    <option value="">Select Payment Method</option>
                    <option value="Credit Card">Credit Card</option>
                    <option value="Debit Card">Debit Card</option>
                </select>
            </div>
            
            <div class="form-group">
                <label for="cardNumber">Card Number</label>
                <input type="text" id="cardNumber" name="cardNumber"
                       pattern="[0-9\s-]{13,19}" 
                       title="Please enter a valid card number (13-19 digits)"
                       placeholder="1234 5678 9012 3456"
                       required>
            </div>
            
            <div class="form-group">
                <label for="expiryDate">Expiry Date</label>
                <input type="text" id="expiryDate" name="expiryDate"
                       pattern="(0[1-9]|1[0-2])\/([0-9]{2})" 
                       title="Please enter a valid expiry date (MM/YY)"
                       placeholder="MM/YY"
                       required>
            </div>
            
            <div class="form-group">
                <label for="cvv">CVV</label>
                <input type="text" id="cvv" name="cvv"
                       pattern="[0-9]{3,4}" 
                       title="Please enter a valid CVV (3-4 digits)"
                       placeholder="123"
                       required>
            </div>
            
            <div class="payment-actions">
                <button type="submit" class="btn btn-primary">Process Payment</button>
                <a href="order?action=list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
    
    <script>
        // Format card number with spaces
        document.getElementById('cardNumber').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\s+/g, '').replace(/[^0-9]/gi, '');
            let formattedValue = '';
            for(let i = 0; i < value.length; i++) {
                if(i > 0 && i % 4 === 0) {
                    formattedValue += ' ';
                }
                formattedValue += value[i];
            }
            e.target.value = formattedValue;
        });
        
        // Format expiry date
        document.getElementById('expiryDate').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\s+/g, '').replace(/[^0-9]/gi, '');
            if (value.length >= 2) {
                value = value.substring(0,2) + '/' + value.substring(2);
            }
            e.target.value = value;
        });
        
        // Auto-fill form if retrying payment
        window.onload = function() {
            <% 
            Payment retryPayment = (Payment) session.getAttribute("paymentRetry");
            if (retryPayment != null) { 
            %>
                document.getElementById('paymentMethod').value = '<%= retryPayment.getPaymentMethod() %>';
                document.getElementById('cardNumber').value = '<%= retryPayment.getCardNumber() %>';
                document.getElementById('expiryDate').value = '<%= retryPayment.getExpiryDate() %>';
                document.getElementById('cvv').value = '<%= retryPayment.getCvv() %>';
            <% 
                session.removeAttribute("paymentRetry");
            } 
            %>
        };
    </script>
</body>
</html> 