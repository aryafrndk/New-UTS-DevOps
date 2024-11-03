import DAO.DAOData;
import DAOInterface.IDAOData;
import model.TambahData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class DAODataTest {
    private IDAOData daoData;
    private Connection connection;

    @BeforeEach
    public void setUp() {
        connection = DBConnection.connectDB(); // Ensure the connection is established
        daoData = new DAOData(connection); // Initialize DAOData with the connection
    }

    @Test
    public void testInsert() {
        TambahData data = new TambahData();
        data.setNim("12345");
        data.setNama("John Doe");
        data.setJenisKelamin("Laki-laki");
        data.setKelas("A");

        assertTrue(daoData.insert(data)); // Assuming insert returns a boolean
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
