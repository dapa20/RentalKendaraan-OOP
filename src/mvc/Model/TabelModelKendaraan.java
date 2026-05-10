/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.Model;

import java.util.List;
import javax.swing.table.AbstractTableModel;
    
/**
 *
 * @author ACER
 */
public class TabelModelKendaraan extends AbstractTableModel {
    List<Kendaraan>lb;
    
public TabelModelKendaraan(List<Kendaraan> lb) { this.lb = lb; }
    @Override public int getRowCount() { return lb.size(); }
    @Override public int getColumnCount() { return 6; }
    @Override public String getColumnName(int column) {
        switch (column) {
            case 0: return "Plat Nomor";
            case 1: return "Jenis";
            case 2: return "Model";
            case 3: return "Kategori";
            case 4: return "Harga";
            case 5: return "Status";
            default: return null;
        }
    }
    @Override public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: return lb.get(row).getPlat_nomor();
            case 1: return lb.get(row).getJenis();
            case 2: return lb.get(row).getModel_kendaraan();
            case 3: return lb.get(row).getKategori();
            case 4: return lb.get(row).getHarga_sewa();
            case 5: return lb.get(row).getStatus();
            default: return null;
        }
    }
}
