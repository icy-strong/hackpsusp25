/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mj.hackpsusp25;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author icy
 */

public class DBConnection {
    private static Connection connection;
    private static final String user = "java";
    private static final String password = "java";
    // Use the full JDBC URL for Derby embedded mode
    private static final String database = "jdbc:derby://localhost:1527/InventoryDB";

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Optionally load the Derby embedded driver explicitly
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                connection = DriverManager.getConnection(database);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Could not open database.");
                System.exit(1);
            }
        }
        return connection;
    }
    
    
}
