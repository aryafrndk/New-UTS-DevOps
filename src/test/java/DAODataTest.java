package test;

import DAO.DAOData;
import model.TambahData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api .Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAODataTest {
    private DAOData daoData;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/db_mahasiswa";
        String user = "root";
        String password = ""; // Ganti dengan password yang sesuai
        connection = DriverManager.getConnection(url, user, password);
        daoData = new DAOData(connection);
        daoData.clearAll(); // Menghapus semua data sebelum setiap pengujian
    }

    @AfterEach
    public void tearDown() {
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
        Assertions.assertEquals(1, daoData.getAll().size(), "Data should be inserted successfully");
    }

    @Test
    public void testUpdate() {
        TambahData data = new TambahData();
        data.setNim("12345");
        data.setNama("John Doe");
        data.setJenisKelamin("Laki-laki");
        data.setKelas("A");
        daoData.insert(data);

        data.setNama("Jane Doe");
        daoData.update(data);

        Assertions.assertEquals("Jane Doe", daoData.getAll().get(0).getNama(), "Data should be updated successfully");
    }

    @Test
    public void testDelete() {
        TambahData data = new TambahData();
        data.setNim("12345");
        data.setNama("John Doe");
        data.setJenisKelamin("Laki-laki");
        data.setKelas("A");
        daoData.insert(data);

        daoData.delete("12345");
        Assertions.assertEquals(0, daoData.getAll().size(), "Data should be deleted successfully");
    }

    @Test
    public void testSearch() {
        TambahData data = new TambahData();
        data.setNim("12345");
        data.setNama("John Doe");
        data.setJenisKelamin("Laki-laki");
        data.setKelas("A");
        daoData.insert(data);

        Assertions.assertEquals(1, daoData.search("John").size(), "Search should return one result");
    }
}
