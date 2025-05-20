package uts.isd.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import uts.isd.model.dao.DBConnector;
import uts.isd.model.dao.UserDBManager;
import uts.isd.model.dao.OrderDBManager;
import uts.isd.model.dao.OrderItemDBManager;
import uts.isd.model.dao.DeviceDBManager;

import java.sql.Connection;

@WebListener
public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        // 1. Initialize DBConnector
        DBConnector connector = new DBConnector();
        ctx.setAttribute("dbConnector", connector);

        // 2. Get JDBC Connection
        Connection conn = connector.getConnection();

        // 3. Create and store DBManagers
        try {
            UserDBManager userDB = new UserDBManager(conn);
            OrderDBManager orderDB = new OrderDBManager(conn);
            OrderItemDBManager itemDB = new OrderItemDBManager(conn);
            DeviceDBManager deviceDB = new DeviceDBManager(conn); // ← add this

            ctx.setAttribute("userDBManager", userDB);
            ctx.setAttribute("orderDBManager", orderDB);
            ctx.setAttribute("orderItemDBManager", itemDB);
            ctx.setAttribute("deviceManager", deviceDB); // ← and this line

            System.out.println("[StartupListener] All DBManagers initialized");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DBManagers", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        DBConnector connector = (DBConnector) ctx.getAttribute("dbConnector");
        if (connector != null) {
            connector.closeConnection();
            System.out.println("[StartupListener] DBConnector closed");
        }
    }
}
