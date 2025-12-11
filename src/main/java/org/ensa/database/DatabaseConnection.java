package org.ensa.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bibliotheque";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    // Instance unique de la connexion (Singleton)
    private static Connection connection;

    public DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        // Lazy initialization : crée la connexion seulement quand nécessaire
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database!");
        }
        else {
            System.out.println("Already connected to the database!");
        }
        return connection;
    }
}
