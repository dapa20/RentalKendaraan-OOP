/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.Model;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;

/**
 * TableModel untuk JTable Daftar Transaksi Sewa.
 * Kolom: No | Pelanggan | Kendaraan | Tgl Sewa | Tgl Kembali | Lama | Total Biaya | Status
 *
 * @author RentalKendaraan
 */
public class TabelModelTransaksi extends AbstractTableModel {

    private final List<Transaksi> listTransaksi;

    private static final String[] KOLOM = {
        "No Transaksi", "Pelanggan", "Kendaraan",
        "Tgl Sewa", "Tgl Kembali", "Lama (hr)",
        "Total Biaya", "Status"
    };

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("id", "ID"));

    public TabelModelTransaksi(List<Transaksi> list) {
        this.listTransaksi = list;
    }

    @Override
    public int getRowCount() {
        return listTransaksi.size();
    }

    @Override
    public int getColumnCount() {
        return KOLOM.length;
    }

    @Override
    public String getColumnName(int column) {
        return KOLOM[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Transaksi t = listTransaksi.get(rowIndex);
        switch (columnIndex) {
            case 0: return t.getId_transaksi();
            case 1: return t.getNama_pelanggan();
            case 2: return t.getNama_kendaraan();
            case 3: return t.getTanggal_sewa() != null ? sdf.format(t.getTanggal_sewa()) : "-";
            case 4: return t.getTanggal_kembali() != null ? sdf.format(t.getTanggal_kembali()) : "-";
            case 5: return t.getLama_hari();
            case 6: return nf.format(t.getTotal_bayar());
            case 7: return t.getStatus_rental();
            default: return null;
        }
    }

    /** Kembalikan objek Transaksi di baris tertentu */
    public Transaksi getTransaksiAt(int row) {
        return listTransaksi.get(row);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // semua sel read-only
    }
}
