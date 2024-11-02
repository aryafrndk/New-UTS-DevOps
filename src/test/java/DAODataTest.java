import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import DAO.DAOData;
import model.TambahData;

import java.util.List;

public class DAODataTest {
    private DAOData daoData;

    private Connection connection;

    public DAOData() {
        try {
            // Replace with your actual database URL, username, and password
            String url = "jdbc:mysql://localhost:3306/db_mahasiswa";
            String user = "root";
            String password = "";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.err.println("There were errors while connecting to db.");
            e.printStackTrace();
        }

    @Before
    public void setUp() {
    daoData = new DAOData();
    // Optionally, clear the database or set it to a known state here
    // daoData.clearAll(); // Implement a method to clear test data if needed
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
        assertTrue(allData.stream().anyMatch(data -> data.getNama().equals("John Smith")));
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
