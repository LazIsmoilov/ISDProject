package uts.isd.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import uts.isd.model.dao.DBConnector;
import uts.isd.model.dao.UserDBManager;
import uts.isd.model.dao.OrderDBManager;
import uts.isd.model.dao.OrderItemDBManager;
import java.sql.Connection;
@WebListener
public class StartupListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        // 1. 初始化连接器
        DBConnector connector = new DBConnector();
        ctx.setAttribute("dbConnector", connector);

        // 2. 从连接器拿到 JDBC Connection
        Connection conn = connector.getConnection();

        // 3. 创建各模块的 DBManager
        try {
            UserDBManager     userDB  = new UserDBManager(conn);
            OrderDBManager    orderDB = new OrderDBManager(conn);
            OrderItemDBManager itemDB = new OrderItemDBManager(conn);
            ctx.setAttribute("userDBManager",     userDB);
            ctx.setAttribute("orderDBManager",    orderDB);
            ctx.setAttribute("orderItemDBManager", itemDB);
            System.out.println("[StartupListener] DBManagers initialized");
        } catch (Exception e) {
            throw new RuntimeException("初始化 DBManager 失败", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        // 关闭底层连接
        DBConnector connector = (DBConnector) ctx.getAttribute("dbConnector");
        if(connector != null) {
            connector.closeConnection();
            System.out.println("[StartupListener] DBConnector closed");
        }
    }
}

