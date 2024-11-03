package DAO;

import model.TambahData;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DAODataTest {

    private static Connection connection;
    private DAOData daoData;

    @BeforeAll
    public static void setUpBeforeClass() throws SQLException {
        // Create an in-memory database connection
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        // Create table
        connection.createStatement().execute("CREATE TABLE tb_mahasiswa (nim VARCHAR(10), nama VARCHAR(50), jenis_kelamin VARCHAR(10), kelas VARCHAR(10))");
    }

    @BeforeEach
    public void setUp() {
        daoData = new DAOData();
        daoData.con = connection; // Use the in-memory connection
    }

    @AfterAll
    public static void tearDownAfterClass() throws SQLException {
        connection.close();
    }

    @Test
    public void testInsert() {
        TambahData data = new TambahData();
        data.setNim("123456");
        data.setNama("John Doe");
        data.setJenisKelamin("Laki-laki");
        data.setKelas("A");

        daoData.insert(data);

        List<TambahData> allData = daoData.getAll();
        assertEquals(1, allData.size());
        assertEquals("John Doe", allData.get(0).getNama());
    }

    @Test
    public void testUpdate() {
        TambahData data = new TambahData();
        data.setNim("123456");
        data.setNama("John Doe");
        data.setJenisKelamin("Laki-laki");
        data.setKelas("A");

        daoData.insert(data);

        data.setNama("Jane Doe");
        daoData.update(data);

        List<TambahData> allData = daoData.getAll();
        assertEquals("Jane Doe", allData.get(0).getNama());
    }

    @Test
    public void testDelete() {
        TambahData data = new TambahData();
        data.setNim("123456");
        data.setNama("John Doe");
        data.setJenisKelamin("Laki-laki");
        data.setKelas("A");

        daoData.insert(data);
        daoData.delete("123456");

        List<TambahData> allData = daoData.getAll();
        assertEquals(0, allData.size());
    }

    @Test
    public void testSearch() {
        TambahData data1 = new TambahData();
        data1.setNim("123456");
        data1.setNama("John Doe");
        data1.setJenisKelamin("Laki-laki");
        data1.setKelas("A");

        TambahData data2 = new TambahData();
        data2.setNim("789012");
        data2.setNama("Jane Smith");
        data2.setJenisKelamin("Perempuan");
        data2.setKelas("B");

        daoData.insert(data1);
        daoData.insert(data2);

        List<TambahData> results = daoData.search("John");
        assertEquals(1, results.size());
        assertEquals("John Doe", results.get(0).getNama());
    }
}
