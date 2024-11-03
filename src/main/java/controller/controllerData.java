package controller;

import DAO.DAOData;
import DAOInterface.IDAOData;
import java.sql.Connection; 
import java.util.List;
import javax.swing.JOptionPane;
import model.TabelModelData;
import model.TambahData;
import view.formcrud;
import koneksi.DBConnection; 

/**
 * Controller untuk mengelola data mahasiswa
 */
public class controllerData {
    private formcrud fc;
    private IDAOData iData;
    private List<TambahData> lstMhs;
    private Connection connection; // Store the connection

    public controllerData(formcrud fc) {
        this.fc = fc;
        connection = DBConnection.connectDB(); // Get the connection
        iData = new DAOData(connection); // Pass the connection to DAOData
        isiTable(); // Load initial data into the table
    }
    
    // Method to populate the table with student data
    public void isiTable() {
        lstMhs = iData.getAll();
        TabelModelData tabelMhs = new TabelModelData(lstMhs);
        fc.getTabelData().setModel(tabelMhs); 
    }
    
    // Method to add student data
    public void insert() {
        try {
            TambahData b = new TambahData();
            b.setNim(fc.gettxtNim().getText());
            b.setNama(fc.gettxtNama().getText());
            b.setJenisKelamin(fc.getjenisKelamin().getSelectedItem().toString());
            b.setKelas(fc.gettxtKelas().getText());
            iData.insert(b);
            JOptionPane.showMessageDialog(fc, "Data berhasil ditambahkan!");
            reset(); // Optionally reset the form after adding
            isiTable(); // Refresh the table
        } catch (Exception e) {
            JOptionPane.showMessageDialog(fc, "Error: " + e.getMessage());
        }
    }
    
    // Method to reset the input form
    public void reset() {
        fc.gettxtNim().setEnabled(true);
        fc.gettxtNim().setText("");
        fc.gettxtNama().setText("");
        fc.getjenisKelamin().setSelectedItem("Pilih Jenis Kelamin");
        fc.gettxtKelas().setText("");
    }
    
    // Method to fill fields with selected data from the table
    public void isiField(int row) {
        if (lstMhs != null && row >= 0 && row < lstMhs.size()) {
            fc.gettxtNim().setEnabled(false);
            fc.gettxtNim().setText(lstMhs.get(row).getNim());
            fc.gettxtNama().setText(lstMhs.get(row).getNama());
            fc.getjenisKelamin().setSelectedItem(lstMhs.get(row).getJenisKelamin());
            fc.gettxtKelas().setText(lstMhs.get(row).getKelas());
        }
    }
    
    // Method to update student data
    public void update() {
        try {
            TambahData b = new TambahData();
            b.setNama(fc.gettxtNama().getText());
            b.setJenisKelamin(fc.getjenisKelamin().getSelectedItem().toString());
            b.setKelas(fc.gettxtKelas().getText());
            b.setNim(fc.gettxtNim().getText());
            iData.update(b);
            JOptionPane.showMessageDialog(fc, "Berhasil Melakukan Update!");
            reset(); // Optionally reset the form after updating
            isiTable(); // Refresh the table
        } catch (Exception e) {
            JOptionPane.showMessageDialog(fc, "Error: " + e.getMessage());
        }
    }
    
    // Method to delete student data
    public void delete() {
        try {
            String nim = fc.gettxtNim().getText();
            iData.delete(nim);
            JOptionPane.showMessageDialog (fc, "Berhasil Menghapus Data!");
            reset(); // Optionally reset the form after deleting
            isiTable(); // Refresh the table
        } catch (Exception e) {
            JOptionPane.showMessageDialog(fc, "Error: " + e.getMessage());
        }
    }
    
    // Method to search for student data by NIM or name
    public void cari(String keyword) {
        try {
            List<TambahData> searchResults = iData.search(keyword);
            TabelModelData tabelMhs = new TabelModelData(searchResults);
            fc.getTabelData().setModel(tabelMhs); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(fc, "Error: " + e.getMessage());
        }
    }
    
    // Method to close the database connection
    public void close() {
        DBConnection.closeConnection(connection);
    }
}
