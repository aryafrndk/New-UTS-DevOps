package DAO;

import DAOInterface.IDAOData;
import model.TambahData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DAOData implements IDAOData {
    private static final Logger LOGGER = Logger.getLogger(DAOData.class.getName());
    private Connection connection;

    private static final String READ_QUERY = "SELECT * FROM tb_mahasiswa";
    private static final String CHECK_QUERY = "SELECT COUNT(*) FROM tb_mahasiswa WHERE nim = ?";
    private static final String INSERT_QUERY = "INSERT INTO tb_mahasiswa(nim, nama, jenis_kelamin, kelas) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE tb_mahasiswa SET nama = ?, jenis_kelamin = ?, kelas = ? WHERE nim = ?";
    private static final String DELETE_QUERY = "DELETE FROM tb_mahasiswa WHERE nim = ?";

    public DAOData(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<TambahData> getAll() {
        List<TambahData> lstMhs = new ArrayList<>();
        if (connection == null) {
            LOGGER.warning("Connection is null. Cannot retrieve data.");
            return lstMhs;
        }
        try (Statement st = connection.createStatement(); ResultSet res = st.executeQuery(READ_QUERY)) {
            while (res.next()) {
                TambahData mhs = new TambahData();
                mhs.setNim(res.getString("nim"));
                mhs.setNama(res.getString("nama"));
                mhs.setJenisKelamin(res.getString("jenis_kelamin"));
                mhs.setKelas(res.getString("kelas"));
                lstMhs.add(mhs);
            }
            LOGGER.info("Data retrieved successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving data: {0}", e.getMessage());
        }
        return lstMhs;
    }

    @Override
    public void insert(TambahData b) {
        if (connection == null) {
            JOptionPane.showMessageDialog(null, "Connection is null. Cannot insert data.");
            return;
        }
        try (PreparedStatement checkStmt = connection.prepareStatement(CHECK_QUERY)) {
            checkStmt.setString(1, b.getNim());
            try (ResultSet resultSet = checkStmt.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(null, "Data already exists in the database!");
                    return;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking data: {0}", e.getMessage());
            return;
        }

        try (PreparedStatement insertStmt = connection.prepareStatement(INSERT_QUERY)) {
            insertStmt.setString(1, b.getNim());
            insertStmt.setString(2, b.getNama());
            insertStmt.setString(3, b.getJenisKelamin());
            insertStmt.setString(4, b.getKelas());
            insertStmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data inserted successfully!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting data: {0}", e.getMessage());
        }
    }

    @Override
    public void update(TambahData b) {
        if (connection == null) {
            JOptionPane.showMessageDialog(null, "Connection is null. Cannot update data.");
            return;
        }
        try (PreparedStatement updateStmt = connection.prepareStatement(UPDATE_QUERY)) {
            updateStmt.setString(1, b.getNama());
            updateStmt.setString(2, b.getJenisKelamin());
            updateStmt.setString(3, b.getKelas());
            updateStmt.setString(4, b.getNim());
            updateStmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data updated successfully!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating data: {0}", e.getMessage());
        }
    }

    @Override
    public void delete(String nim) {
        if (connection == null) {
            JOptionPane.showMessageDialog(null, "Connection is null. Cannot delete data.");
            return;
        }
        try (PreparedStatement deleteStmt = connection.prepareStatement(DELETE_QUERY)) {
            deleteStmt.setString(1, nim);
            deleteStmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data deleted successfully!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting data: {0}", e.getMessage());
        }
    }

    @Override
    public List<TambahData> search(String keyword) {
        List<TambahData> lstMhs = new ArrayList<>();
        String sql = "SELECT * FROM tb_mahasiswa WHERE nim LIKE ? OR nama LIKE ?";
        try (PreparedStatement searchStmt = connection.prepareStatement(sql)) {
            searchStmt.setString(1, "%" + keyword + "%");
            searchStmt.setString(2, "%" + keyword + "%");
            try (ResultSet res = searchStmt.executeQuery()) {
                while (res.next()) {
                    TambahData mhs = new TambahData();
                    mhs.setNim(res.getString("nim"));
                    mhs.setNama(res.getString("nama"));
                    mhs.setJenisKelamin(res.getString("jenis_kelamin"));
                    mhs.setKelas(res.getString("kelas"));
                    lstMhs.add(mhs);
                }
                LOGGER.info("Search completed successfully.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching data: {0}", e.getMessage());
        }
        return lstMhs;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing connection: {0}", e.getMessage());
            }
        }
    }
}
