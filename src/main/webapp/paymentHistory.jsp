<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="uts.isd.model.User" %>
<%@ page import="uts.isd.model.Payment" %>
<%@ page import="uts.isd.model.PaymentStatus" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Comparator" %>
<!DOCTYPE html>
<html>
<head>
    <title>Payment History</title>
    <link rel="stylesheet" href="style.css">
    <style>
        .filter-container {
            background-color: #f5f5f5;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .filter-row {
            display: flex;
            gap: 20px;
            margin-bottom: 15px;
            align-items: center;
        }
        .filter-group {
            flex: 1;
        }
        .payment-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .payment-table th {
            background-color: #f5f5f5;
            padding: 12px;
            text-align: left;
            cursor: pointer;
        }
        .payment-table th:hover {
            background-color: #e0e0e0;
        }
        .payment-table td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
        }
        .status-badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: 500;
            display: inline-block;
            text-align: center;
            min-width: 100px;
        }
        .status-completed {
            background-color: #e8f5e9;
            color: #2e7d32;
            border: 1px solid #2e7d32;
        }
        .status-cancelled {
            background-color: #ffebee;
            color: #c62828;
            border: 1px solid #c62828;
        }
        .status-pending {
            background-color: #fff3e0;
            color: #ef6c00;
            border: 1px solid #ef6c00;
        }
        .status-failed {
            background-color: #fce4ec;
            color: #c2185b;
            border: 1px solid #c2185b;
        }
        .status-refunded {
            background-color: #e3f2fd;
            color: #1565c0;
            border: 1px solid #1565c0;
        }
    </style>
    <script>
        function sortTable(n) {
            var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
            table = document.getElementById("paymentTable");
            switching = true;
            dir = "asc";
            
            while (switching) {
                switching = false;
                rows = table.rows;
                
                for (i = 1; i < (rows.length - 1); i++) {
                    shouldSwitch = false;
                    x = rows[i].getElementsByTagName("TD")[n];
                    y = rows[i + 1].getElementsByTagName("TD")[n];
                    
                    if (dir == "asc") {
                        if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                            shouldSwitch = true;
                            break;
                        }
                    } else if (dir == "desc") {
                        if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                            shouldSwitch = true;
                            break;
                        }
                    }
                }
                
                if (shouldSwitch) {
                    rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                    switching = true;
                    switchcount++;
                } else {
                    if (switchcount == 0 && dir == "asc") {
                        dir = "desc";
                        switching = true;
                    }
                }
            }
        }
        
        function filterTable() {
            var input, filter, table, tr, td, i, txtValue;
            input = document.getElementById("statusFilter");
            filter = input.value.toUpperCase();
            table = document.getElementById("paymentTable");
            tr = table.getElementsByTagName("tr");
            
            for (i = 1; i < tr.length; i++) {
                td = tr[i].getElementsByTagName("td")[5]; // Status column
                if (td) {
                    txtValue = td.textContent || td.innerText;
                    if (filter === "" || txtValue.toUpperCase().indexOf(filter) > -1) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }
            }
        }
    </script>
</head>
<body>
    <pref-header></pref-header>
    
    <div class="container">
        <h1>Payment History</h1>
        
        <%
            User user = (User) session.getAttribute("user");
            List<Payment> payments = (List<Payment>) session.getAttribute("payments");
            
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            if (payments == null) {
                payments = Collections.emptyList();
            }
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        %>
        
        <div class="filter-container">
            <form action="payment" method="get">
                <input type="hidden" name="action" value="search">
                <div class="filter-row">
                    <div class="filter-group">
                        <label for="startDate">Start Date:</label>
                        <input type="date" id="startDate" name="startDate" class="form-control" required>
                    </div>
                    <div class="filter-group">
                        <label for="endDate">End Date:</label>
                        <input type="date" id="endDate" name="endDate" class="form-control" required>
                    </div>
                    <div class="filter-group">
                        <label for="statusFilter">Status:</label>
                        <select id="statusFilter" class="form-control" onchange="filterTable()">
                            <option value="">All</option>
                            <option value="COMPLETED">Completed</option>
                            <option value="CANCELLED">Cancelled</option>
                            <option value="PENDING">Pending</option>
                            <option value="FAILED">Failed</option>
                            <option value="REFUNDED">Refunded</option>
                        </select>
                    </div>
                    <div class="filter-group">
                        <button type="submit" class="buttons">Search</button>
                    </div>
                </div>
            </form>
        </div>
        
        <% if (payments.isEmpty()) { %>
            <div class="alert alert-info">No payments found for the selected date range.</div>
        <% } else { %>
            <table id="paymentTable" class="payment-table">
                <thead>
                    <tr>
                        <th onclick="sortTable(0)">Payment ID</th>
                        <th onclick="sortTable(1)">Order ID</th>
                        <th onclick="sortTable(2)">Amount</th>
                        <th onclick="sortTable(3)">Payment Method</th>
                        <th onclick="sortTable(4)">Date</th>
                        <th onclick="sortTable(5)">Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Payment payment : payments) { %>
                        <tr>
                            <td><%= payment.getId() %></td>
                            <td><%= payment.getOrderId() %></td>
                            <td>$<%= String.format("%.2f", payment.getAmount()) %></td>
                            <td><%= payment.getPaymentMethod() %></td>
                            <td><%= dateFormat.format(payment.getPaymentDate()) %></td>
                            <td>
                                <span class="status-badge status-<%= payment.getStatus().toString().toLowerCase() %>">
                                    <%= payment.getStatus().getDisplayName() %>
                                </span>
                            </td>
                            <td>
                                <% if (payment.getStatus() == PaymentStatus.COMPLETED) { %>
                                    <a href="payment?action=receipt&paymentId=<%= payment.getId() %>" class="buttons">View Receipt</a>
                                <% } %>
                                <% if (payment.getStatus() == PaymentStatus.PENDING) { %>
                                    <form action="payment" method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="cancel">
                                        <input type="hidden" name="paymentId" value="<%= payment.getId() %>">
                                        <button type="submit" class="buttons" onclick="return confirm('Are you sure you want to cancel this payment?')">Cancel</button>
                                    </form>
                                <% } %>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <% } %>
        
        <div style="text-align: center; margin-top: 20px;">
            <a href="order?action=list"><button class="buttons">Back to Orders</button></a>
        </div>
    </div>
</body>
</html> 