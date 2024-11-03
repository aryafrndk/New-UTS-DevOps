import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import DAO.DAOData;
import model.TambahData;

import java.util.List;

public class DAODataTest {
    private DAOData daoData;

    @Before
    public void setUp() throws Exception {
        // Koneksi ke database
        Connection connection = DriverManager.getConnection(url, user, password);
        
        // Membuat tabel jika belum ada
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tb_mahasiswa ("
                + "nim VARCHAR(20), "
                + "nama VARCHAR(20), "
                + "jenis_kelamin VARCHAR(35), "
                + "kelas VARCHAR(30)";
        Statement statement = connection.createStatement();
        statement.execute(createTableSQL);
        
        // Inisialisasi DAO
        dao = new DAOData(connection);
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
        List<TambahData> searchResults = daoData.search(" 12345");
        assertFalse(searchResults.isEmpty());
        assertEquals("John Doe", searchResults.get(0).getNama());

        // Mencari data berdasarkan nama
        searchResults = daoData.search("John Doe");
        assertFalse(searchResults.isEmpty());
        assertEquals("12345", searchResults.get(0).getNim());
    }
}
