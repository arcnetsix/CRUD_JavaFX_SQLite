package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:cadastro.db");
    }
}
