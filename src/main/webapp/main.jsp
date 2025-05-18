<%--
  Created by IntelliJ IDEA.
  User: laz
  Date: 30/4/2025
  Time: 4:04 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="uts.isd.model.User" %>
<%@ page import="uts.isd.model.dao.UserDAO" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Main - IoT Bay</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<pref-header></pref-header>
<div class="content">
  <h1>Recommended IoT Devices</h1>
  <div class="device">
    <!-- Device Cards Section -->
    <div class="device-card">
      <h3>🔦 Smart Light Bulbs</h3>
      <p>Brighten your home with customizable lighting that adapts to your schedule and preferences.</p>
      <form method="post" action="order">
        <input type="hidden" name="productId" value="1" />
        <input type="number" name="quantity" value="1" min="1" required />
        <input type="hidden" name="unitPrice" value="29.99" />
        <input type="hidden" name="totalPrice" value="29.99" />
        <button type="submit">🛒 Add to Order</button>
      </form>
    </div>

    <div class="device-card">
      <h3>📸 Security Camera</h3>
      <p>Protect your home with 4K resolution, night vision, and smart motion detection.</p>
      <form method="post" action="order">
        <input type="hidden" name="productId" value="2" />
        <input type="number" name="quantity" value="1" min="1" required />
        <input type="hidden" name="unitPrice" value="89.99" />
        <input type="hidden" name="totalPrice" value="89.99" />
        <button type="submit">🛒 Add to Order</button>
      </form>
    </div>

    <div class="device-card">
      <h3>🌡️ Smart Thermostat</h3>
      <p>Save energy with intelligent temperature control that learns your habits and preferences.</p>
      <form method="post" action="order">
        <input type="hidden" name="productId" value="3" />
        <input type="number" name="quantity" value="1" min="1" required />
        <input type="hidden" name="unitPrice" value="69.99" />
        <input type="hidden" name="totalPrice" value="69.99" />
        <button type="submit">🛒 Add to Order</button>
      </form>
    </div>

    <div class="device-card">
      <h3>🔐 Smart Door Lock</h3>
      <p>Secure your home with keyless entry and remote access.</p>
      <form method="post" action="order">
        <input type="hidden" name="productId" value="4" />
        <input type="number" name="quantity" value="1" min="1" required />
        <input type="hidden" name="unitPrice" value="59.99" />
        <input type="hidden" name="totalPrice" value="59.99" />
        <button type="submit">🛒 Add to Order</button>
      </form>
    </div>

  </div>
</div>
</body>
</html>

