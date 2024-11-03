package koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/db_mahasiswa"; // Pastikan URL ini benar
    static final String USER = "root"; // Username sesuai
    static final String PASS = ""; // Password kosong

    public static Connection connectDB() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            return conn;
        } catch (SQLException ex) {
            System.out.println("There were errors while connecting to db: " + ex.getMessage());
            return null;
        }
    }
}
