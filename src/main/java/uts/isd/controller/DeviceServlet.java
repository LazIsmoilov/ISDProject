package uts.isd.controller;

import uts.isd.model.Device;
import uts.isd.model.User;
import uts.isd.model.dao.DAO;
import uts.isd.model.dao.DeviceDBManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/DeviceServlet")
public class DeviceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        DAO dao = (DAO) session.getAttribute("db");

        if (dao == null) {
            request.setAttribute("error", "Database access not initialized. Please try logging in again.");
            request.getRequestDispatcher("main.jsp").forward(request, response);
            return;
        }

        DeviceDBManager db;
        try {
            Connection conn = dao.getConnection(); // Assumes DAO has getConnection()
            db = new DeviceDBManager(conn);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to connect to database: " + e.getMessage());
            request.getRequestDispatcher("main.jsp").forward(request, response);
            return;
        }

        try {
            List<Device> devices = new ArrayList<>();

            switch (action) {
                case "search":
                    String keyword = request.getParameter("search");
                    String typeFilter = request.getParameter("type");
                    devices = db.searchDevices(keyword, typeFilter);
                    break;

                case "edit":
                    if (user != null && "staff".equals(user.getRole())) {
                        int id = Integer.parseInt(request.getParameter("id"));
                        Device device = db.getDeviceById(id);
                        request.setAttribute("device", device);
                        request.getRequestDispatcher("deviceForm.jsp").forward(request, response);
                        return;
                    } else {
                        response.sendRedirect("main.jsp");
                        return;
                    }

                case "delete":
                    if (user != null && "staff".equals(user.getRole())) {
                        int deleteId = Integer.parseInt(request.getParameter("id"));
                        db.deleteDevice(deleteId);
                    }
                    response.sendRedirect("DeviceServlet");
                    return;

                default:
                    devices = db.getAllDevices();
                    break;
            }

            // Set filtered or full device list
            request.setAttribute("devices", devices);

            // Group by type
            Map<String, List<Device>> deviceMap = new HashMap<>();
            for (Device d : devices) {
                deviceMap.computeIfAbsent(d.getType(), k -> new ArrayList<>()).add(d);
            }
            request.setAttribute("groupedDevices", deviceMap);

            // Dashboard stats
            request.setAttribute("total", db.getTotalProductCount());
            request.setAttribute("totalQty", db.getTotalQuantity());
            request.setAttribute("inStock", db.getInStockCount());
            request.setAttribute("outStock", db.getOutOfStockCount());

            request.getRequestDispatcher("main.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Something went wrong: " + e.getMessage());
            request.getRequestDispatcher("main.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        DAO dao = (DAO) session.getAttribute("db");

        if (dao == null) {
            request.setAttribute("error", "Database access not initialized. Please try logging in again.");
            request.getRequestDispatcher("main.jsp").forward(request, response);
            return;
        }

        DeviceDBManager db;
        try {
            Connection conn = dao.getConnection(); // Assumes DAO has getConnection()
            db = new DeviceDBManager(conn);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to connect to database: " + e.getMessage());
            request.getRequestDispatcher("main.jsp").forward(request, response);
            return;
        }

        try {
            if (user == null || !"staff".equals(user.getRole())) {
                response.sendRedirect("main.jsp");
                return;
            }

            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String description = request.getParameter("description");
            String unit = request.getParameter("unit");
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if ("add".equals(action)) {
                Device newDevice = new Device(name, type, description, unit, price, quantity);
                db.addDevice(newDevice);
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("deviceId"));
                Device updatedDevice = new Device(id, name, type, description, unit, price, quantity);
                db.updateDevice(updatedDevice);
            }

            response.sendRedirect("DeviceServlet");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("main.jsp").forward(request, response);
        }
    }
}