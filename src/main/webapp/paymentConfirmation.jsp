<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="uts.isd.model.User" %>
<%@ page import="uts.isd.model.Payment" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <title>Payment Confirmation</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <pref-header></pref-header>
    
    <div class="container">
        <h1>Payment Confirmation</h1>
        
        <%
            User user = (User) session.getAttribute("user");
            Payment payment = (Payment) session.getAttribute("payment");
            
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            if (payment == null) {
                response.sendRedirect("order?action=list");
                return;
            }
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        %>
        
        <div class="form-container">
            <div style="text-align: center; margin-bottom: 30px;">
                <div style="color: #4CAF50; font-size: 48px; margin-bottom: 20px;">✓</div>
                <h2 style="color: #4CAF50;">Payment Successful!</h2>
            </div>
            
            <div style="background-color: #f9f9f9; padding: 20px; border-radius: 8px; margin-bottom: 20px;">
                <h3>Payment Details</h3>
                <table style="width: 100%; border-collapse: collapse;">
                    <tr>
                        <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Payment ID:</strong></td>
                        <td style="padding: 10px; border-bottom: 1px solid #ddd;"><%= payment.getId() %></td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Order ID:</strong></td>
                        <td style="padding: 10px; border-bottom: 1px solid #ddd;"><%= payment.getOrderId() %></td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Amount:</strong></td>
                        <td style="padding: 10px; border-bottom: 1px solid #ddd;">$<%= String.format("%.2f", payment.getAmount()) %></td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Payment Method:</strong></td>
                        <td style="padding: 10px; border-bottom: 1px solid #ddd;"><%= payment.getPaymentMethod() %></td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Card Number:</strong></td>
                        <td style="padding: 10px; border-bottom: 1px solid #ddd;">**** **** **** <%= payment.getCardNumber().substring(payment.getCardNumber().length() - 4) %></td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Date:</strong></td>
                        <td style="padding: 10px; border-bottom: 1px solid #ddd;"><%= dateFormat.format(payment.getPaymentDate()) %></td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Status:</strong></td>
                        <td style="padding: 10px; border-bottom: 1px solid #ddd;"><%= payment.getStatus() %></td>
                    </tr>
                </table>
            </div>
            
            <div style="text-align: center; margin-top: 20px;">
                <a href="payment?action=search"><button class="buttons">View Payment History</button></a>
                <a href="order?action=list"><button class="buttons">Back to Orders</button></a>
            </div>
        </div>
    </div>
</body>
</html> 