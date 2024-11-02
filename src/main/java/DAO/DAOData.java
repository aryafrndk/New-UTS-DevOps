/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author ASUS
 */
public class DAOData implements IDAOData {
    
    public DAOData(){
        con = DBConnection.connectDB();
    }

    @Override
    public List<TambahData> getAll() {
        List<TambahData> lstMhs = null;
        try{
            lstMhs = new ArrayList<TambahData>();
             Statement st = (Statement) DBConnection.connectDB().createStatement();
             ResultSet res = st.executeQuery(read);
             while(res.next()){
                 TambahData mhs = new TambahData();
                 mhs.setNim(res.getString("nim"));
                 mhs.setNama(res.getString("nama"));
                 mhs.setJenisKelamin(res.getString("jenis_kelamin"));
                 mhs.setKelas(res.getString("kelas"));
                 lstMhs.add(mhs);
             }
        }catch(SQLException e){  
            System.out.println("ERORR!"+e);
        }
        return lstMhs;
    }

    @Override
    public void insert(TambahData b) {
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try {
        // Mengecek keberadaan data dengan menggunakan SELECT statement
        statement = con.prepareStatement(checkQuery);
        statement.setString(1, b.getNim());
        resultSet = statement.executeQuery();
        // Jika data sudah ada di dalam database, tampilkan dialog popup
        if (resultSet.next() && resultSet.getInt(1) > 0) {
            JOptionPane.showMessageDialog(null, 
                    "Data sudah ada di dalam database!");
            return;
        }
        // Jika data belum ada di dalam database, lakukan proses INSERT
        statement = con.prepareStatement(insert);
        statement.setString(1, b.getNim());
        statement.setString(2, b.getNama());
        statement.setString(3, b.getJenisKelamin());
        statement.setString(4, b.getKelas());
        statement.execute();
        JOptionPane.showMessageDialog(null, "Data berhasil diinput!");
    } catch (SQLException e) {
        System.out.println("Gagal Input Data!");
    } finally {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException ex) {
            System.out.println("Gagal Input Data!");
        }
    }
}

    @Override
    public void update(TambahData b) {
        PreparedStatement statement = null;
        try{
            statement = con.prepareStatement(update);
            statement.setString(1, b.getNama());
            statement.setString(2, b.getJenisKelamin());
            statement.setString(3, b.getKelas());
            statement.setString(4, b.getNim());
            statement.execute();
        }catch(SQLException e){
            System.out.println("Gagal Update Data!");
        }
        finally{
            try {
                statement.close();
            } catch (SQLException ex) {
                System.out.println("Gagal Update Data!");
            } 
        }
    }

    @Override
    public void delete(String nim) {
        PreparedStatement statement = null;
        try{
            statement = con.prepareStatement(delete);
            statement.setString(1, nim);
            statement.execute();
        }catch(SQLException e){
            System.out.println("Gagal Hapus Data!");
        }
        finally{
            try {
                statement.close();
            } catch (SQLException ex) {
                System.out.println("Gagal Hapus Data!");
            } 
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
        System.out.println("Error while searching data: " + e.getMessage());
    }
    
    return lstMhs;
}
    
    //koneksi db
    Connection con;
    //SQL Query
    String read = "SELECT * FROM tb_mahasiswa";
    String checkQuery = "SELECT COUNT(*) FROM tb_mahasiswa WHERE nim = ?";
    String insert = "INSERT INTO tb_mahasiswa(nim,nama,jenis_kelamin,kelas) VALUES(?,?,?,?)";
    String update = "UPDATE tb_mahasiswa set nama=?,jenis_kelamin=?,kelas=? WHERE nim=?";
    String delete = "DELETE FROM tb_mahasiswa WHERE nim=?";
    
}
