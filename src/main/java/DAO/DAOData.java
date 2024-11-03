package DAO;

import javax.swing.JOptionPane;
import DAOInterface.IDAOData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.TambahData;
import koneksi.DBConnection;
import java.sql.DriverManager;

public class DAOData implements IDAOData {

    private Connection con;

    // SQL Queries
    private static final String READ_QUERY = "SELECT * FROM tb_mahasiswa";
    private static final String CHECK_QUERY = "SELECT COUNT(*) FROM tb_mahasiswa WHERE nim = ?";
    private static final String INSERT_QUERY = "INSERT INTO tb_mahasiswa(nim,nama,jenis_kelamin,kelas) VALUES(?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE tb_mahasiswa SET nama=?, jenis_kelamin=?, kelas=? WHERE nim=?";
    private static final String DELETE_QUERY = "DELETE FROM tb_mahasiswa WHERE nim=?";

    private Connection connection;

    public DAOData() {
        try {
            String url = "jdbc:mysql://localhost:3306/db_mahasiswa";
            String user = "root";
            String password = "rootpassword"; // Password kosong
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void clearAll() {
        if (connection == null) {
            System.out.println("Connection is null. Cannot clear data.");
            return; // Atau lempar exception sesuai kebutuhan
        }
        String sql = "DELETE FROM tb_mahasiswa"; // Ganti dengan nama tabel Anda
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<TambahData> getAll() {
        List<TambahData> lstMhs = new ArrayList<>();
        try (Statement st = con.createStatement(); ResultSet res = st.executeQuery(READ_QUERY)) {
            while (res.next()) {
                TambahData mhs = new TambahData();
                mhs.setNim(res.getString("nim"));
                mhs.setNama(res.getString("nama"));
                mhs.setJenisKelamin(res.getString("jenis_kelamin"));
                mhs.setKelas(res.getString("kelas"));
                lstMhs.add(mhs);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving data: " + e.getMessage());
        }
        return lstMhs;
    }

    @Override
    public void insert(TambahData b) {
        try (PreparedStatement statement = con.prepareStatement(CHECK_QUERY)) {
            statement.setString(1, b.getNim());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "Data sudah ada di dalam database!");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error checking data: " + e.getMessage());
        }

        try (PreparedStatement statement = con.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, b.getNim());
            statement.setString(2, b.getNama());
            statement.setString(3, b.getJenisKelamin());
            statement.setString(4, b.getKelas());
            statement.execute();
            JOptionPane.showMessageDialog(null, "Data berhasil diinput!");
        } catch (SQLException e) {
            System.out.println("Error inserting data: " + e.getMessage());
        }
    }

    @Override
    public void update(TambahData b) {
        try (PreparedStatement statement = con.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, b.getNama());
            statement.setString(2, b.getJenisKelamin());
            statement.setString(3, b.getKelas());
            statement.setString(4, b.getNim());
            statement.execute();
        } catch (SQLException e) {
            System.out.println("Error updating data: " + e.getMessage());
        }
    }

    @Override
    public void delete(String nim) {
        try (PreparedStatement statement = con.prepareStatement(DELETE_QUERY)) {
            statement.setString(1, nim);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("Error deleting data: " + e.getMessage());
        }
    }

    public List<TambahData> search(String keyword) {
        List<TambahData> lstMhs = new ArrayList<>();
        String sql = "SELECT * FROM tb_mahasiswa WHERE nim LIKE ? OR nama LIKE ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, "%" + keyword + "%");
            statement.setString(2, "%" + keyword + "%");
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                TambahData mhs = new TambahData();
                mhs.setNim(res.getString("nim"));
                mhs.setNama(res.getString("nama"));
                mhs.setJenisKelamin(res.getString("jenis_kelamin"));
                mhs.setKelas(res.getString("kelas"));
                lstMhs.add(mhs);
            }
        } catch (SQLException e) {
            System.out.println("Error searching data: " + e.getMessage());
        }
        return lstMhs;
    }

    // Close the connection when the DAOData instance is no longer needed
    public void closeConnection() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
