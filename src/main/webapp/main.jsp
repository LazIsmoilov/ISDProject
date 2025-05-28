package uts.isd;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.jsp.*;
import uts.isd.model.*;
import java.util.List;

<%--
  Created by IntelliJ IDEA.
  User: [Your Name]
  Date: [Creation Date]
  Time: [Creation Time]
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="uts.isd.model.Device" %>
<%@ page import="uts.isd.model.User" %>
<%@ page import="java.util.List" %>
<%@ page session="true" %>

<%
    User user = (User) session.getAttribute("loggedInUser");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String role = user.getRole();
    String fullName = user.getFullName();
    List<Device> devices = (List<Device>) request.getAttribute("devices");
    // Normalize role to lowercase for consistent comparison
    request.setAttribute("role", role != null ? role.toLowerCase() : "none");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Main - IoT Devices</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <%@ include file="header.jsp" %>
    <style>
        .content-box {
            background-color: #ffffff;
            padding: 30px;
            border-radius: 10px;
            max-width: 95%;
            margin: 0 auto;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }
        .device-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
        }
        .device-card {
            background-color: #ffffff;
            padding: 20px;
            border-radius: 8px;
            text-align: center;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        .device-card h5 {
            margin-bottom: 10px;
        }
        .device-card p {
            font-size: 0.95rem;
            color: #555;
        }
        .btn-sm {
            margin: 4px 2px;
        }
        .search-controls {
            flex: 1;
            display: flex;
            gap: 10px;
        }
        .top-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }
        .error-message {
            color: red;
            text-align: center;
            margin-bottom: 20px;
        }
        .debug-info {
            color: blue;
            text-align: center;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>

<pref-header></pref-header>

<div class="container mt-5 content-box">
    <!-- Display error if present -->
    <c:if test="${not empty error}">
        <p class="error-message">${error}</p>
    </c:if>

    <!-- CENTERED Welcome + Role -->
    <div class="text-center mb-4">
        <h2>Welcome, <%= fullName %>!</h2>
        <p class="text-muted">Role: <%= role != null ? role : "Not set" %></p>
    </div>

    <!-- DASHBOARD -->
    <div class="row text-center my-4">
        <div class="col-md-3"><div class="border rounded py-3"><h5>Total Products</h5><h4>${total}</h4></div></div>
        <div class="col-md-3"><div class="border rounded py-3"><h5>Total Quantity</h5><h4>${totalQty}</h4></div></div>
        <div class="col-md-3"><div class="border rounded py-3"><h5>In Stock</h5><h4>${inStock}</h4></div></div>
        <div class="col-md-3"><div class="border rounded py-3"><h5>Out of Stock</h5><h4>${outStock}</h4></div></div>
    </div>

    <!-- SEARCH & ADD -->
    <div class="top-bar">
        <form method="get" action="DeviceServlet" class="search-controls">
            <input type="hidden" name="action" value="search" />
            <input type="text" name="search" placeholder="Search by name..." class="form-control" />
            <select name="type" class="form-select">
                <option value="">All Types</option>
                <option value="Energy">Energy</option>
                <option value="Security">Security</option>
                <option value="Home Automation">Home Automation</option>
                <option value="Health">Health</option>
                <option value="Sensors">Sensors</option>
            </select>
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
        <c:if test="${role eq 'staff'}">
            <a href="deviceForm.jsp" class="btn btn-success">Add New Device</a>
        </c:if>
    </div>

    <!-- CATALOGUE -->
    <div class="device-grid">
        <c:forEach var="d" items="${devices}">
            <div class="device-card">
                <h5>${d.name}</h5>
                <p><strong>Type:</strong> ${d.type}</p>
                <p><strong>Unit:</strong> ${d.unit}</p>
                <p><strong>Price:</strong> $${d.price}</p>
                <p><strong>Quantity:</strong> ${d.quantity}</p>

                <c:if test="${role eq 'customer'}">
                    <form method="post" action="order">
                        <input type="hidden" name="productId" value="${d.deviceId}" />
                        <input type="number" name="quantity" value="1" min="1" required class="form-control mb-2" />
                        <input type="hidden" name="unitPrice" value="${d.price}" />
                        <input type="hidden" name="totalAmount" value="${d.price}" />
                        <button type="submit" class="btn btn-primary btn-sm">Add to Order</button>
                    </form>
                </c:if>

                <c:if test="${role eq 'staff'}">
                    <a href="DeviceServlet?action=edit&deviceId=${d.deviceId}" class="btn btn-primary btn-sm">Edit</a>
                    <a href="DeviceServlet?action=delete&deviceId=${d.deviceId}" class="btn btn btn-primary btn-sm"
                       onclick="return confirm('Are you sure?')">Delete</a>
                </c:if>
            </div>
        </c:forEach>
    </div>

    <c:if test="${empty devices}">
        <p class="error-message mt-3">No devices found.</p>
    </c:if>
</div>

</body>
</html>