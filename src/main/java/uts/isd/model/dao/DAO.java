package uts.isd.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAO {
    public ArrayList<DBManager<?>> tables;

    public DAO() throws SQLException {
        tables = new ArrayList<>();
        Connection connection = new DBConnector().openConnection();

        try {
            tables.add(new UserDBManager(connection));
        }
        catch (SQLException ex) {
            System.out.println("Error initializing DBManagers");
        }
    }

    public UserDBManager Users() {
        return (UserDBManager) tables.get(0);
    }
}