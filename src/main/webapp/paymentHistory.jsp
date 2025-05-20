<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="uts.isd.model.User" %>
<%@ page import="uts.isd.model.Payment" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <title>Payment History</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <pref-header></pref-header>
    
    <div class="container">
        <h1>Payment History</h1>
        
        <%
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            List<Payment> payments = (List<Payment>) session.getAttribute("payments");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        %>
        
        <div class="form-container">
            <form action="payment" method="get" class="form-group">
                <input type="hidden" name="action" value="search">
                <div style="display: flex; gap: 10px; margin-bottom: 20px;">
                    <input type="date" name="startDate" placeholder="Start Date">
                    <input type="date" name="endDate" placeholder="End Date">
                    <input type="submit" value="Search" class="buttons">
                </div>
            </form>
            
            <% if (payments != null && !payments.isEmpty()) { %>
                <table style="width: 100%; border-collapse: collapse; margin-top: 20px;">
                    <thead>
                        <tr style="background-color: #f5f5f5;">
                            <th style="padding: 10px; text-align: left;">Payment ID</th>
                            <th style="padding: 10px; text-align: left;">Order ID</th>
                            <th style="padding: 10px; text-align: left;">Amount</th>
                            <th style="padding: 10px; text-align: left;">Payment Method</th>
                            <th style="padding: 10px; text-align: left;">Date</th>
                            <th style="padding: 10px; text-align: left;">Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Payment payment : payments) { %>
                            <tr style="border-bottom: 1px solid #ddd;">
                                <td style="padding: 10px;"><%= payment.getId() %></td>
                                <td style="padding: 10px;"><%= payment.getOrderId() %></td>
                                <td style="padding: 10px;">$<%= String.format("%.2f", payment.getAmount()) %></td>
                                <td style="padding: 10px;"><%= payment.getPaymentMethod() %></td>
                                <td style="padding: 10px;"><%= dateFormat.format(payment.getPaymentDate()) %></td>
                                <td style="padding: 10px;"><%= payment.getStatus() %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <p>No payment records found.</p>
            <% } %>
            
            <div style="margin-top: 20px;">
                <a href="order?action=list"><button type="button" class="buttons">Back to Orders</button></a>
            </div>
        </div>
    </div>
</body>
</html> 