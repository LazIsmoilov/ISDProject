<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="uts.isd.model.User" %>
<%@ page import="uts.isd.model.Payment" %>
<%@ page import="uts.isd.model.Order" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <title>Payment Receipt</title>
    <link rel="stylesheet" href="style.css">
    <style>
        @media print {
            .no-print {
                display: none;
            }
            .receipt-container {
                width: 100%;
                margin: 0;
                padding: 20px;
            }
        }
        .receipt-container {
            max-width: 800px;
            margin: 20px auto;
            padding: 30px;
            border: 1px solid #ddd;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .receipt-header {
            text-align: center;
            margin-bottom: 30px;
            border-bottom: 2px solid #333;
            padding-bottom: 20px;
        }
        .receipt-details {
            margin-bottom: 30px;
        }
        .receipt-footer {
            text-align: center;
            margin-top: 30px;
            border-top: 1px solid #ddd;
            padding-top: 20px;
            font-size: 0.9em;
            color: #666;
        }
    </style>
</head>
<body>
    <div class="no-print">
        <pref-header></pref-header>
    </div>
    
    <div class="receipt-container">
        <div class="receipt-header">
            <h1>Payment Receipt</h1>
            <p>Thank you for your payment</p>
        </div>
        
        <%
            User user = (User) session.getAttribute("user");
            Payment payment = (Payment) session.getAttribute("payment");
            Order order = (Order) session.getAttribute("order");
            
            if (user == null || payment == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        %>
        
        <div class="receipt-details">
            <table style="width: 100%; border-collapse: collapse;">
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Receipt Number:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><%= payment.getPaymentId() %></td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Date:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><%= dateFormat.format(payment.getPaymentDate()) %></td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Customer:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><%= user.getFullName() %></td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Email:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><%= user.getEmail() %></td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Order ID:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><%= payment.getOrderId() %></td>
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
                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Amount Paid:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #ddd;">$<%= String.format("%.2f", payment.getAmount()) %></td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Status:</strong></td>
                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><%= payment.getStatus() %></td>
                </tr>
            </table>
        </div>
        
        <div class="receipt-footer">
            <p>This is a computer-generated receipt and does not require a signature.</p>
            <p>Thank you for your business!</p>
        </div>
    </div>
    
    <div class="no-print" style="text-align: center; margin: 20px;">
        <button onclick="window.print()" class="buttons">Print Receipt</button>
        <a href="payment?action=search"><button class="buttons">Back to Payment History</button></a>
    </div>
</body>
</html> 