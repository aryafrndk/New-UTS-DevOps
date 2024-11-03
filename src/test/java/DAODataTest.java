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
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_lembaga_pelatihan", "root", "");
            daoData = new DAOData(connection);
        } catch (SQLException e) {
            e.printStackTrace(); // Jika terjadi kesalahan, cetak stack trace untuk debug
            fail("Failed to establish database connection");
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
