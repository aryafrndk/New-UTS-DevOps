package koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    static final String DB_URL = "jdbc:mysql://localhost:3306/db_mahasiswa";
    static final String USER = "root";
    static final String PASS = "";

    public static Connection connectDB() {
        Connection conn = null;
        try {
            // Optional: Load the MySQL JDBC driver (not always necessary with newer JDBC versions)
            // Class.forName("com.mysql.cj.jdbc.Driver"); // Uncomment if needed

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            return conn;
        } catch (SQLException ex) {
            System.out.println("There were errors while connecting to db: " + ex.getMessage());
            return null;
        }
    }
}
