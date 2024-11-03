package koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // H2 in-memory database URL
    static final String DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"; // In-memory database
    static final String USER = "sa"; // Default user for H2
    static final String PASS = ""; // Default password for H2

    public static Connection connectDB() {
        Connection conn = null;
        try {
            // Load the H2 JDBC driver (optional for JDBC 4.0 and later)
            // Class.forName("org.h2.Driver"); // Uncomment if necessary
            
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connection to H2 database established successfully.");
        } catch (SQLException ex) {
            System.out.println("Error while connecting to the H2 database: " + ex.getMessage());
        }
        return conn;
    }

    // Optional: Method to close the connection
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connection to H2 database closed successfully.");
            } catch (SQLException ex) {
                System.out.println("Error while closing the connection: " + ex.getMessage());
            }
        }
    }
}
