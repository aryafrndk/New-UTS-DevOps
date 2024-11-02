/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.List;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author ASUS
 */
public class TabelModelData extends AbstractTableModel{
    
    public TabelModelData(List<TambahData> lstMhs){
        this.lstMhs = lstMhs;
    }
    @Override
    public int getRowCount() {
        return this.lstMhs.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }
    
    @Override
    public String getColumnName(int column){
        switch(column){
            case 0:
                return "NIM";
            case 1:
                return "Nama";
            case 2:
                return "Jenis Kelamin";
            case 3:
                return "Kelas";
            default:
                return null;
        }
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                return lstMhs.get(rowIndex).getNim();
            case 1:
                return lstMhs.get(rowIndex).getNama();
            case 2:
                return lstMhs.get(rowIndex).getJenisKelamin();
            case 3:
                return lstMhs.get(rowIndex).getKelas();
            default:
                return null;
        }
    }
    List<TambahData> lstMhs;
    
}
