package test;

import DAO.DAOData;
import model.TambahData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DAODataTest {
    private DAOData daoData;
    private Connection connection;

    // Update with your database connection details
    private final String url = "jdbc:mysql://localhost:3306/";
    private final String user = "root";
    private final String password = ""; // Replace with your actual password
    private final String dbName = "db_mahasiswa"; // Nama database yang akan digunakan

    @BeforeEach
    public void setUp() {
        try {
            // Establish connection to MySQL server
            connection = DriverManager.getConnection(url, user, password);
            assertNotNull(connection, "Connection should not be null");
            System.out.println("Database connection established.");
    
            // Create the database if it doesn’t exist
            String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS " + dbName;
            try (Statement statement = connection.createStatement()) {
                statement.execute(createDatabaseSQL);
                System.out.println("Database " + dbName + " created or already exists.");
            }
    
            // Connect to the specific database
            connection.setCatalog(dbName);
    
            // Create the table if it doesn’t exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS tb_mahasiswa (" +
                    "nim VARCHAR(20) PRIMARY KEY, " +
                    "nama VARCHAR(20), " +
                    "jenis_kelamin VARCHAR(35), " +
                    "kelas VARCHAR(30))";
    
            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableSQL);
                System.out.println("Table tb_mahasiswa created or already exists.");
            }
    
            // Initialize DAO with connection
            daoData = new DAOData(connection);
            assertNotNull(daoData, "DAOData should not be null after initialization");
            System.out.println("DAOData initialized successfully.");
    
            // Clear data before each test
            daoData.clearAll();
            System.out.println("Data cleared before tests.");
    
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to set up database connection or DAOData: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            // Clean up data after each test
            if (daoData != null) {
                daoData.clearAll();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            fail("Failed to clean up after test: " + e.getMessage());
        }
    }

    @Test
    public void testInsert() {
        System.out.println("Running testInsert...");
        assertNotNull(daoData, "DAOData is null, setup failed.");
    
        TambahData mhs = new TambahData();
        mhs.setNim("12345");
        mhs.setNama("John Doe");
        mhs.setJenisKelamin("Laki-Laki");
        mhs.setKelas("1A");
    
        daoData.insert(mhs);
    
        List<TambahData> allData = daoData.getAll();
        assertTrue(allData.stream().anyMatch(data -> data.getNim().equals("12345")), "Data not found after insert");
    }

    @Test
    public void testUpdate() {
        System.out.println("Running testUpdate...");
        // Pastikan untuk memanggil insert sebelum update
        testInsert(); // Memanggil testInsert untuk memastikan data ada
    
        TambahData mhs = new TambahData();
        mhs.setNim("12345");
        mhs.setNama("John Smith"); // Update nama
        daoData.update(mhs);
    
        // Verify data update
        List<TambahData> allData = daoData.getAll();
        assertTrue(allData.stream().anyMatch(data -> data.getNim().equals("12345") && data.getNama().equals("John Smith")), "Data not updated correctly");
    }

    @Test
    public void testDelete() {
        System.out.println("Running testDelete...");
        TambahData mhs = new TambahData();
        mhs.setNim("12345");
        mhs.setNama("John Doe");
        mhs.setJenisKelamin("Laki-Laki");
        m hs.setKelas("1A");
        daoData.insert(mhs);

        // Delete data
        daoData.delete("12345");

        // Verify deletion
        List<TambahData> allData = daoData.getAll();
        assertFalse(allData.stream().anyMatch(data -> data.getNim().equals("12345")), "Data not deleted");
    }

    @Test
    public void testSearch() {
        System.out.println("Running testSearch...");
        TambahData mhs = new TambahData();
        mhs.setNim("12345");
        mhs.setNama("John Doe");
        mhs.setJenisKelamin("Laki-Laki");
        mhs.setKelas("1A");
        daoData.insert(mhs);

        // Search by NIM
        List<TambahData> searchResults = daoData.search("12345");
        assertFalse(searchResults.isEmpty(), "Search result is empty for NIM");
        assertEquals("John Doe", searchResults.get(0).getNama(), "Name does not match search result for NIM");

        // Search by name
        searchResults = daoData.search("John Doe");
        assertFalse(searchResults.isEmpty(), "Search result is empty for name");
        assertEquals("12345", searchResults.get(0).getNim(), "NIM does not match search result for name");
    }
}
