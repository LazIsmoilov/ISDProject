package uts.isd.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import uts.isd.model.dao.DAO;
import uts.isd.model.dao.DBConnector;
import uts.isd.model.dao.UserDBManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebListener
public class StartupListener implements ServletContextListener, HttpSessionListener {
    private static final Logger logger = Logger.getLogger(StartupListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Server Started");
        ServletContext context = sce.getServletContext();
        try {
            DBConnector dbConnector = new DBConnector();
            Connection conn = dbConnector.getConnection();
            UserDBManager userDbManager = new UserDBManager(conn);
            context.setAttribute("userDBManager", userDbManager);
            context.setAttribute("dbConnector", dbConnector); // Store for cleanup
            logger.info("UserDBManager initialized in application scope");
        } catch (SQLException e) {
            logger.severe("Failed to initialize UserDBManager: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        try {
            DAO dao = new DAO();
            session.setAttribute("db", dao);
            logger.info("DAO initialized in session scope");
        } catch (SQLException e) {
            logger.severe("Could not connect to database: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Server Stopped");
        ServletContext context = sce.getServletContext();
        DBConnector dbConnector = (DBConnector) context.getAttribute("dbConnector");
        if (dbConnector != null) {
            dbConnector.closeConnection();
            logger.info("Database connection closed");
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // Optional: Cleanup session resources if needed
    }
}