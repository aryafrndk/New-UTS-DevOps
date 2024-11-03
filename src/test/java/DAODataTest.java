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

    private final String url = "jdbc:mysql://localhost:3306/db_lembaga_pelatihan"; // Update to your actual database
    private final String user = "root";
    private final String password = ""; // Replace with your actual password

    @BeforeEach
    public void setUp() {
        try {
            // Establish connection
            connection = DriverManager.getConnection(url, user, password);
            assertNotNull(connection, "Connection should not be null");
            System.out.println("Database connection established.");
    
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
        mhs.setNim("123456");
        mhs.setNama("Test User");
        mhs.setJenisKelamin("Male");
        mhs.setKelas("A");

        daoData.insert(mhs);
        
        List<TambahData> result = daoData.getAll();
        assertEquals(1, result.size(), "Data not inserted correctly.");
        assertEquals("Test User", result.get(0).getNama(), "Inserted name does not match.");
    }

    @Test
    public void testUpdate() {
        System.out.println("Running testUpdate...");
        assertNotNull(daoData, "DAOData is null, setup failed.");

        TambahData mhs = new TambahData();
        mhs.setNim("123456");
        mhs.setNama("Test User");
        mhs.setJenisKelamin("Male");
        mhs.setKelas("A");
        daoData.insert(mhs);

        mhs.setNama("Updated User");
        daoData.update(mhs);

        List<TambahData> result = daoData.getAll();
        assertEquals(1, result.size(), "Data not updated correctly.");
        assertEquals("Updated User", result.get(0).getNama(), "Updated name does not match.");
    }

    @Test
    public void testDelete() {
        System.out.println("Running testDelete...");
        assertNotNull(daoData, "DAOData is null, setup failed.");

        TambahData mhs = new TambahData();
        mhs.setNim("123456");
        mhs.setNama("Test User");
        mhs.setJenisKelamin("Male");
        mhs.setKelas("A");
        daoData.insert(mhs);
        
        daoData.delete("123456");

        List<TambahData> result = daoData.getAll();
        assertEquals(0, result.size(), "Data not deleted correctly.");
    }

    @Test
    public void testSearch() {
        System.out.println("Running testSearch...");
        assertNotNull(daoData, "DAOData is null, setup failed.");

        TambahData mhs = new TambahData();
        mhs.setNim("123456");
        mhs.setNama("Test User");
        mhs.setJenisKelamin("Male");
        mhs.setKelas("A");
        daoData.insert(mhs);

        List<TambahData> result = daoData.search("123456");
        assertEquals(1, result.size(), "Search by nim failed.");
    }
}
