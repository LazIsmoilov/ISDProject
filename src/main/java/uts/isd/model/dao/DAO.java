package uts.isd.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAO {
    public ArrayList<UserDBManager> tables;

    public DAO() throws SQLException {
        tables = new ArrayList<>();
        Connection connection = new DBConnector().getConnection();
        tables.add(new UserDBManager(connection));
    }

    public UserDBManager Users() {
        return (UserDBManager) tables.get(0);
    }
}