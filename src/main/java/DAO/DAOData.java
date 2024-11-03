package DAO;

import DAOInterface.IDAOData;
import model.TambahData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOData implements IDAOData {
    private static final Logger LOGGER = Logger.getLogger(DAOData.class.getName());

    private final Connection connection;

    // SQL Queries
    private static final String READ_QUERY = "SELECT * FROM tb_mahasiswa";
    private static final String CHECK_QUERY = "SELECT COUNT(*) FROM tb_mahasiswa WHERE nim = ?";
    private static final String INSERT_QUERY = "INSERT INTO tb_mahasiswa(nim,nama,jenis_kelamin,kelas) VALUES(?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE tb_mahasiswa SET nama=?, jenis_kelamin=?, kelas=? WHERE nim=?";
    private static final String DELETE_QUERY = "DELETE FROM tb_mahasiswa WHERE nim=?";

    public DAOData(Connection connection) {
        this.connection = connection;
    }

    public void clearAll() {
        if (connection == null) {
            LOGGER.warning("Connection is null. Cannot clear data.");
            return;
        }
        String sql = "DELETE FROM tb_mahasiswa";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            LOGGER.info("All records deleted from tb_mahasiswa");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error clearing all data: {0}", e.getMessage());
        }
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
    public boolean insert(TambahData b) {
        if (connection == null) {
            LOGGER.warning("Connection is null. Cannot insert data.");
            return false;
        }
        try (PreparedStatement statement = connection.prepareStatement(CHECK_QUERY)) {
            statement.setString(1, b.getNim());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                LOGGER.warning("Data already exists in the database.");
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking data: {0}", e.getMessage());
            return false;
        }

        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, b.getNim());
            statement.setString(2, b.getNama());
            statement.setString(3, b.getJenisKelamin());
            statement.setString(4, b.getKelas());
            statement.execute();
            LOGGER.info("Data inserted successfully.");
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting data: {0}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(TambahData b) {
        if (connection == null) {
            LOGGER.warning("Connection is null. Cannot update data.");
            return false;
        }
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, b.getNama());
            statement.setString(2, b.getJenisKelamin());
            statement.setString(3, b.getKelas());
            statement.setString(4, b.getNim());
            int rowsAffected = statement.executeUpdate();
            LOGGER.info("Data updated successfully.");
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating data: {0}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String nim) {
        if (connection == null) {
            LOGGER.warning("Connection is null. Cannot delete data.");
            return false;
        }
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setString(1, nim);
            int rowsAffected = statement.executeUpdate();
            LOGGER.info("Data deleted successfully.");
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting data: {0}", e.getMessage());
            return false;
        }
    }

    @Override
    public List<TambahData> search(String keyword) {
        List<TambahData> lstMhs = new ArrayList<>();
        String sql = "SELECT * FROM tb_mahasiswa WHERE nim LIKE ? OR nama LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
            LOGGER.info("Search completed successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching data: {0}", e.getMessage());
        }
        return lstMhs;
    }
}
