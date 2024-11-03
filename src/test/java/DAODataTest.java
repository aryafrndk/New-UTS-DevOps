import DAO.DAOData;
import model.TambahData;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DAODataTest {
    private DAOData daoData;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        // Mengatur koneksi ke database
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_mahasiswa", "root", "");
        daoData = new DAOData(connection);
        daoData.clearAll(); // Menghapus semua data sebelum setiap pengujian
    }

    @AfterEach
    public void tearDown() {
        // Menutup koneksi setelah pengujian
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testInsert() {
        TambahData data = new TambahData();
        data.setNim("12345");
        data.setNama("John Doe");
        data.setJenisKelamin("Laki-laki");
        data.setKelas("A");

        daoData.insert(data);
        assertEquals(1, daoData.getAll().size(), "Data should be inserted successfully");
    }

    @Test
    public void testUpdate() {
        // Pertama, kita harus menginsert data sebelum mengupdate
        TambahData data = new TambahData();
        data.setNim("12345");
        data.setNama("John Doe");
        data.setJenisKelamin("Laki-laki");
        data.setKelas("A");
        daoData.insert(data);

        // Sekarang kita update data
        data.setNama("Jane Doe");
        daoData.update(data);

        // Verifikasi bahwa data telah diperbarui
        assertEquals("Jane Doe", daoData.getAll().get(0).getNama(), "Data should be updated successfully");
    }

    @Test
    public void testDelete() {
        // Pertama, kita harus menginsert data sebelum menghapus
        TambahData data = new TambahData();
        data.setNim("12345");
        data.setNama("John Doe");
        data.setJenisKelamin("Laki-laki");
        data.setKelas("A");
        daoData.insert(data);

        // Sekarang kita hapus data
        daoData.delete("12345");
        assertEquals(0, daoData.getAll().size(), "Data should be deleted successfully");
    }

    @Test
    public void testSearch() {
        // Pertama, kita harus menginsert data sebelum mencari
        TambahData data = new TambahData();
        data.setNim("12345");
        data.setNama("John Doe");
        data.setJenisKelamin("Laki-laki");
        data.setKelas("A");
        daoData.insert(data);

        // Sekarang kita cari data
        assertEquals(1, daoData.search("John").size(), "Search should return one result");
    }
}
