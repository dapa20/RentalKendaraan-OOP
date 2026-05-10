/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.Model;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author dapa
 */
public class TabelModelPelanggan extends AbstractTableModel {
    List<Pelanggan> lb;
    
    public TabelModelPelanggan(List<Pelanggan> lb) {
        this.lb = lb;
    }

    @Override
    public int getRowCount() {
        return lb.size();
    }

    @Override
    public int getColumnCount() {
        return 5; // Total ada 5 kolom: ID, NIK, Nama, Telp, Alamat
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ID";
            case 1: return "NIK";
            case 2: return "Nama Lengkap";
            case 3: return "No. Telepon";
            case 4: return "Alamat";
            default: return null;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: return lb.get(rowIndex).getId_pelanggan();
            case 1: return lb.get(rowIndex).getNik();
            case 2: return lb.get(rowIndex).getNama_lengkap();
            case 3: return lb.get(rowIndex).getNo_telp();
            case 4: return lb.get(rowIndex).getAlamat();
            default: return null;
        }
    }
}
