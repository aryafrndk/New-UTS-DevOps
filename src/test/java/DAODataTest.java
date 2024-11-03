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

    // Ganti dengan URL, user, dan password yang sesuai
    private String url = "jdbc:mysql://localhost:3306/db_mahasiswa";
    private String user = "root";
    private String password = "rootpassword"; // Ganti dengan password yang sesuai

    @BeforeEach
    public void setUp() {
        try {
            // Attempting connection setup
            connection = DriverManager.getConnection(url, user, password);
            assertNotNull(connection, "Connection should not be null");
    
            // Ensure the table exists
            String createTableSQL = "CREATE TABLE IF NOT EXISTS tb_mahasiswa ("
                    + "nim VARCHAR(20) PRIMARY KEY, "
                    + "nama VARCHAR(20), "
                    + "jenis_kelamin VARCHAR(35), "
                    + "kelas VARCHAR(30))";
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
    
            // Initialize DAO with connection
            daoData = new DAOData(connection);
            assertNotNull(daoData, "DAOData should not be null");
    
            // Clear data if needed
            daoData.clearAll();
        } catch (SQLException e) {
            fail("Failed to set up database connection or DAOData: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Membersihkan data setelah pengujian
        daoData.clearAll();
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testInsert() {
        TambahData mhs = new TambahData();
        mhs.setNim("12345");
        mhs.setNama("John Doe");
        mhs.setJenisKelamin("Laki-Laki");
        mhs.setKelas("1A");

        daoData.insert(mhs);
        // Verifikasi bahwa data berhasil dimasukkan
        List<TambahData> allData = daoData.getAll();
        assertTrue(allData.stream().anyMatch(data -> data.getNim().equals("12345")));
    }

    @Test
    public void testUpdate() {
        // Pertama, kita perlu memasukkan data untuk diperbarui
        TambahData mhs = new TambahData();
        mhs.setNim("12345");
        mhs.setNama("John Doe");
        mhs.setJenisKelamin("Laki-Laki");
        mhs.setKelas("1A");
        daoData.insert(mhs);

        // Sekarang kita akan memperbarui data
        mhs.setNama("John Smith");
        daoData.update(mhs);

        // Verifikasi bahwa data telah diperbarui
        List<TambahData> allData = daoData.getAll();
        assertTrue(allData.stream().anyMatch(data -> data.getNim().equals("12345") && data.getNama().equals("John Smith")));
    }

    @Test
    public void testDelete() {
        // Pertama, kita perlu memasukkan data untuk dihapus
        TambahData mhs = new TambahData();
        mhs.setNim("12345");
        mhs.setNama("John Doe");
        mhs.setJenisKelamin("Laki-Laki");
        mhs.setKelas("1A");
        daoData.insert(mhs);

        // Sekarang kita akan menghapus data
        daoData.delete("12345");

        // Verifikasi bahwa data telah dihapus
        List<TambahData> allData = daoData.getAll();
        assertFalse(allData.stream().anyMatch(data -> data.getNim().equals("12345")));
    }

    @Test
    public void testSearch() {
        // Memasukkan data untuk dicari
        TambahData mhs = new TambahData();
        mhs.setNim("12345");
        mhs.setNama("John Doe");
        mhs.setJenisKelamin("Laki-Laki");
        mhs.setKelas("1A");
        daoData.insert(mhs);

        // Mencari data berdasarkan NIM
        List<TambahData> searchResults = daoData.search("12345");
        assertFalse(searchResults.isEmpty());
        assertEquals("John Doe", searchResults.get(0).getNama());

        // Mencari data berdasarkan nama
        searchResults = daoData.search("John Doe");
        assertFalse(searchResults.isEmpty());
        assertEquals("12345", searchResults.get(0).getNim());
    }
}
