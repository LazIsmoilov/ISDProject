<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="uts.isd.model.Device" %>
<%@ page session="true" %>

<%
    String action = request.getParameter("action");
    Device device = (Device) request.getAttribute("device");
    boolean isEdit = (device != null);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= isEdit ? "Edit Device" : "Add Device" %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>

<div class="container mt-5">
    <h2><%= isEdit ? "Edit Device" : "Add New Device" %></h2>

    <form method="post" action="DeviceServlet">
        <input type="hidden" name="action" value="<%= isEdit ? "update" : "add" %>" />
        <c:if test="${device != null}">
            <input type="hidden" name="deviceId" value="${device.deviceId}" />
        </c:if>

        <div class="mb-3">
            <label>Name:</label>
            <input type="text" name="name" class="form-control" value="${device.name}" required />
        </div>

        <div class="mb-3">
            <label>Type:</label>
            <select name="type" class="form-select" required>
                <option value="">Select type</option>
                <option value="Energy" ${device.type == "Energy" ? "selected" : ""}>Energy</option>
                <option value="Security" ${device.type == "Security" ? "selected" : ""}>Security</option>
                <option value="Home Automation" ${device.type == "Home Automation" ? "selected" : ""}>Home Automation</option>
                <option value="Health" ${device.type == "Health" ? "selected" : ""}>Health</option>
                <option value="Sensors" ${device.type == "Sensors" ? "selected" : ""}>Sensors</option>
            </select>
        </div>

        <div class="mb-3">
            <label>Description:</label>
            <textarea name="description" class="form-control" required>${device.description}</textarea>
        </div>

        <div class="mb-3">
            <label>Unit:</label>
            <input type="text" name="unit" class="form-control" value="${device.unit}" required />
        </div>

        <div class="mb-3">
            <label>Price:</label>
            <input type="number" step="0.01" name="price" class="form-control" value="${device.price}" required />
        </div>

        <div class="mb-3">
            <label>Quantity:</label>
            <input type="number" name="quantity" class="form-control" value="${device.quantity}" required />
        </div>

        <button type="submit" class="btn btn-primary"><%= isEdit ? "Update Device" : "Add Device" %></button>
        <a href="main.jsp" class="btn btn-secondary">Cancel</a>
    </form>
</div>

</body>
</html>
