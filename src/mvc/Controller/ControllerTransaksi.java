package mvc.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import mvc.DAO.DAOTransaksi;
import mvc.Koneksi.Koneksi;
import mvc.Model.Kendaraan;
import mvc.Model.TabelModelTransaksi;
import mvc.Model.Transaksi;
import mvc.View.FormTransaksi;
/**
 *
 * @author dapa
 */
public class ControllerTransaksi {

    private final FormTransaksi view;
    private final DAOTransaksi daoTransaksi;
    private final Connection connection;

    private final NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("id", "ID"));
    private static final double PERSEN_DENDA = 0.10;
    private Kendaraan kendaraanDipilih = null;

    public ControllerTransaksi(FormTransaksi view) {
        this.view = view;
        this.daoTransaksi = new DAOTransaksi();
        this.connection = Koneksi.connection();
    }

   public void isiComboPelanggan(Integer includeId) {
        JComboBox<String> combo = view.getPelangganCombo();
        combo.removeAllItems();
        combo.addItem("-- Pilih Pelanggan --");

        String sql = "SELECT id_pelanggan, nama_lengkap FROM tbl_pelanggan "
                   + "WHERE id_pelanggan NOT IN (SELECT id_pelanggan FROM tbl_transaksi_rental)";
        
        if (includeId != null) {
            sql += " OR id_pelanggan = " + includeId;
        }
        
        sql += " ORDER BY nama_lengkap";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                combo.addItem(rs.getInt("id_pelanggan") + "|" + rs.getString("nama_lengkap"));
            }
        } catch (SQLException ex) {
            System.out.println("Gagal isi combo pelanggan: " + ex.getMessage());
        }

        combo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null) {
                    String s = value.toString();
                    setText(s.contains("|") ? s.split("\\|", 2)[1] : s);
                }
                return this;
            }
        });
    }

    // FUNGSI CADANGAN (Dipanggil saat form pertama dibuka)
    public void isiComboPelanggan() {
        isiComboPelanggan(null);
    }

    // Method default (dipanggil saat awal form dibuka)
    public void isiComboKendaraan() {
        isiComboKendaraan(null);
    }

    // Method khusus mode Edit (menarik kendaraan yang sedang disewa kembali ke list)
    public void isiComboKendaraan(Transaksi editT) {
        JComboBox<String> combo = view.getKendaraanCombo();
        combo.removeAllItems();
        combo.addItem("-- Pilih Kendaraan --");

        String kondisiMobil = "status='Tersedia'";
        String kondisiMotor = "status='Tersedia'";

        // LOGIKA BARU: Kebal dari error walaupun ID tidak terbawa dari database!
        if (editT != null) {
            int idM = editT.getId_mobil() != null ? editT.getId_mobil() : 0;
            int idMot = editT.getId_motor() != null ? editT.getId_motor() : 0;
            String nama = editT.getNama_kendaraan() != null ? editT.getNama_kendaraan() : "";
            
            kondisiMobil = "(status='Tersedia' OR id_mobil=" + idM + " OR model_mobil='" + nama + "')";
            kondisiMotor = "(status='Tersedia' OR id_motor=" + idMot + " OR model_motor='" + nama + "')";
        }

        String sqlMobil = "SELECT id_mobil AS id, plat_nomor, model_mobil AS model, harga_sewa, 'Mobil' AS jenis FROM tbl_mobil WHERE " + kondisiMobil;
        String sqlMotor = "SELECT id_motor AS id, plat_nomor, model_motor AS model, harga_sewa, 'Motor' AS jenis FROM tbl_motor WHERE " + kondisiMotor;
        String sql = sqlMobil + " UNION ALL " + sqlMotor + " ORDER BY model";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String item = rs.getString("jenis") + "|" + rs.getInt("id") + "|" + rs.getString("plat_nomor") + "|" + rs.getString("model") + "|" + rs.getInt("harga_sewa");
                combo.addItem(item);
            }
        } catch (SQLException ex) {
            System.out.println("Gagal isi combo kendaraan: " + ex.getMessage());
        }

        combo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null) {
                    String s = value.toString();
                    if (s.contains("|")) {
                        String[] p = s.split("\\|");
                        setText(p[3] + " " + p[2] + " (" + p[0] + ")");
                    } else {
                        setText(s);
                    }
                }
                return this;
            }
        });
    }

    public void onKendaraanDipilih() {
        Object selected = view.getKendaraanCombo().getSelectedItem();
        if (selected == null || !selected.toString().contains("|")) {
            bersihkanDetailKendaraan();
            kendaraanDipilih = null;
            return;
        }

        String[] parts = selected.toString().split("\\|");
        kendaraanDipilih = new Kendaraan();
        kendaraanDipilih.setJenis(parts[0]);
        kendaraanDipilih.setId(Integer.parseInt(parts[1]));
        kendaraanDipilih.setPlat_nomor(parts[2]);
        kendaraanDipilih.setModel_kendaraan(parts[3]);
        kendaraanDipilih.setHarga_sewa(Integer.parseInt(parts[4]));

        view.getJLabel6().setText(kendaraanDipilih.getPlat_nomor()); 
        view.getJLabel7().setText(kendaraanDipilih.getModel_kendaraan()); 
        view.getJLabel9().setText(kendaraanDipilih.getJenis()); 
        view.getJLabel10().setText(nf.format(kendaraanDipilih.getHarga_sewa())); 

        hitungTotalBiaya();
    }

    private void bersihkanDetailKendaraan() {
        view.getJLabel6().setText("-");
        view.getJLabel7().setText("-");
        view.getJLabel9().setText("-");
        view.getJLabel10().setText("-");
    }

    public void hitungLamaSewa() {
        try {
            Date tglSewa = (Date) view.getSewaSpinner().getValue();
            Date tglKembali = (Date) view.getKembaliSpinner().getValue();

            if (tglSewa == null || tglKembali == null) return;

            long diffMs = tglKembali.getTime() - tglSewa.getTime();
            long lamaHari = TimeUnit.MILLISECONDS.toDays(diffMs);

            if (lamaHari < 0) {
                JOptionPane.showMessageDialog(view, "Tanggal kembali tidak boleh sebelum tanggal sewa!", "Perhatian", JOptionPane.WARNING_MESSAGE);
                view.getLamaSewaTF().setText("0");
                view.getTotalBiayaTF().setText("Rp 0");
                return;
            }

            view.getLamaSewaTF().setText(String.valueOf(lamaHari));
            hitungTotalBiaya();

        } catch (Exception ex) {
            System.out.println("Error hitung lama sewa: " + ex.getMessage());
        }
    }

    public void hitungTotalBiaya() {
        if (kendaraanDipilih == null) return;

        try {
            int lamaHari = Integer.parseInt(view.getLamaSewaTF().getText().trim());
            int hargaSewa = kendaraanDipilih.getHarga_sewa();
            String dendaStr = view.getDendaTF().getText().trim();
            int denda = dendaStr.isEmpty() ? 0 : Integer.parseInt(dendaStr);

            long total = (long) lamaHari * hargaSewa + denda;
            view.getTotalBiayaTF().setText("Rp " + nf.format(total));
        } catch (NumberFormatException ex) {
            view.getTotalBiayaTF().setText("Rp 0");
        }
    }

    public void simpanTransaksi() {
        Object pilihPelanggan = view.getPelangganCombo().getSelectedItem();
        if (pilihPelanggan == null || !pilihPelanggan.toString().contains("|") || kendaraanDipilih == null) {
            JOptionPane.showMessageDialog(view, "Pastikan Pelanggan dan Kendaraan sudah dipilih!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPelanggan = Integer.parseInt(pilihPelanggan.toString().split("\\|")[0]);
        Date tglSewa = (Date) view.getSewaSpinner().getValue();
        Date tglKembali = (Date) view.getKembaliSpinner().getValue();
        int lamaHari = Integer.parseInt(view.getLamaSewaTF().getText().trim());
        int hargaSewa = kendaraanDipilih.getHarga_sewa();
        String dendaStr = view.getDendaTF().getText().trim();
        int denda = dendaStr.isEmpty() ? 0 : Integer.parseInt(dendaStr);
        int totalBayar = lamaHari * hargaSewa + denda;

        if (lamaHari <= 0) {
            JOptionPane.showMessageDialog(view, "Lama sewa minimal 1 hari!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Transaksi t = new Transaksi();
        t.setId_pelanggan(idPelanggan);
        t.setTanggal_sewa(tglSewa);
        t.setTanggal_kembali(tglKembali);
        t.setLama_hari(lamaHari);
        t.setDenda(denda);
        t.setTotal_bayar(totalBayar);
        t.setStatus_rental("Berjalan");

        if ("Mobil".equals(kendaraanDipilih.getJenis())) {
            t.setId_mobil(kendaraanDipilih.getId());
        } else {
            t.setId_motor(kendaraanDipilih.getId());
        }

        daoTransaksi.insert(t);
        updateStatusKendaraan(kendaraanDipilih.getJenis(), kendaraanDipilih.getId(), "Disewa");

        JOptionPane.showMessageDialog(view, "Transaksi berhasil disimpan!\nTotal Biaya: Rp " + nf.format(totalBayar), "Sukses", JOptionPane.INFORMATION_MESSAGE);
        
        isiTableTransaksi();
        isiComboKendaraan();
        resetForm();
    }

    public void selesaikanSewa() {
        JTable tabel = view.getJTable1();
        int row = tabel.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Pilih transaksi di tabel yang akan diselesaikan!", "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TabelModelTransaksi model = (TabelModelTransaksi) tabel.getModel();
        Transaksi t = model.getTransaksiAt(row);

        if ("Selesai".equals(t.getStatus_rental())) {
            JOptionPane.showMessageDialog(view, "Transaksi ini sudah selesai.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int hargaSewa = getHargaSewaDariDB(t);
        Date tglAktual = new Date();
        Date tglRencana = t.getTanggal_kembali();
        
        int dendaBaru = 0;
        if (tglAktual.after(tglRencana)) {
            long selisihMs = tglAktual.getTime() - tglRencana.getTime();
            long hariTerlambat = TimeUnit.MILLISECONDS.toDays(selisihMs);
            dendaBaru = (int) (hariTerlambat * hargaSewa * PERSEN_DENDA);
        }

        int totalBaru = t.getLama_hari() * hargaSewa + dendaBaru;

        int konfirmasi = JOptionPane.showConfirmDialog(view, "Selesaikan sewa?\nTotal Bayar: Rp " + nf.format(totalBaru), "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            daoTransaksi.updateStatus(t.getId_transaksi(), "Selesai", dendaBaru, totalBaru);
            String jenis = t.getJenis_kendaraan();
            int idKendaraan = "Mobil".equals(jenis) ? (t.getId_mobil() != null ? t.getId_mobil() : 0) : (t.getId_motor() != null ? t.getId_motor() : 0);
            updateStatusKendaraan(jenis, idKendaraan, "Tersedia");

            JOptionPane.showMessageDialog(view, "Sewa diselesaikan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            isiTableTransaksi();
            isiComboKendaraan();
        }
    }
    
    public void hapusTransaksi() {
        JTable tabel = view.getJTable1();
        int row = tabel.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Pilih data di tabel yang mau dihapus!", "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TabelModelTransaksi model = (TabelModelTransaksi) tabel.getModel();
        Transaksi t = model.getTransaksiAt(row);

        int konfirmasi = JOptionPane.showConfirmDialog(view, "Yakin ingin menghapus data transaksi pelanggan " + t.getNama_pelanggan() + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            daoTransaksi.delete(t.getId_transaksi()); 
            JOptionPane.showMessageDialog(view, "Data transaksi berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            isiTableTransaksi();
            isiComboKendaraan(); 
            resetForm();
        }
    }
    
    public void editTransaksi() {
        JTable tabel = view.getJTable1();
        int row = tabel.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Pilih data di tabel yang mau diedit!", "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TabelModelTransaksi model = (TabelModelTransaksi) tabel.getModel();
        int idTransaksi = model.getTransaksiAt(row).getId_transaksi();

        Object pilihPelanggan = view.getPelangganCombo().getSelectedItem();
        if (pilihPelanggan == null || !pilihPelanggan.toString().contains("|") || kendaraanDipilih == null) {
            JOptionPane.showMessageDialog(view, "Pastikan Pelanggan dan Kendaraan sudah dipilih!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPelanggan = Integer.parseInt(pilihPelanggan.toString().split("\\|")[0]);
        Date tglSewa = (Date) view.getSewaSpinner().getValue();
        Date tglKembali = (Date) view.getKembaliSpinner().getValue();
        int lamaHari = Integer.parseInt(view.getLamaSewaTF().getText().trim());
        int hargaSewa = kendaraanDipilih.getHarga_sewa();
        String dendaStr = view.getDendaTF().getText().trim();
        int denda = dendaStr.isEmpty() ? 0 : Integer.parseInt(dendaStr);
        int totalBayar = lamaHari * hargaSewa + denda;

        Transaksi t = new Transaksi();
        t.setId_transaksi(idTransaksi); 
        t.setId_pelanggan(idPelanggan);
        t.setTanggal_sewa(tglSewa);
        t.setTanggal_kembali(tglKembali);
        t.setLama_hari(lamaHari);
        t.setDenda(denda);
        t.setTotal_bayar(totalBayar);
        
        if ("Mobil".equals(kendaraanDipilih.getJenis())) {
            t.setId_mobil(kendaraanDipilih.getId());
        } else {
            t.setId_motor(kendaraanDipilih.getId());
        }

        daoTransaksi.update(t); 
        JOptionPane.showMessageDialog(view, "Data transaksi berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        
        isiTableTransaksi();
        resetForm();
    }
    
    public void isiField(int row) {
        TabelModelTransaksi model = (TabelModelTransaksi) view.getJTable1().getModel();
        Transaksi t = model.getTransaksiAt(row);
        // 1. Panggil ulang isi combo kendaraan (menarik yang sedang disewa)
        isiComboKendaraan(t);
        isiComboPelanggan(t.getId_pelanggan());
        // 2. Pilih otomatis Pelanggan (Mencocokkan NAMA, bukan ID)
        String namaPelangganTabel = t.getNama_pelanggan(); 
        for (int i = 0; i < view.getPelangganCombo().getItemCount(); i++) {
            String item = view.getPelangganCombo().getItemAt(i);
            if (item != null && namaPelangganTabel != null && item.toLowerCase().contains(namaPelangganTabel.toLowerCase())) {
                view.getPelangganCombo().setSelectedIndex(i);
                break;
            }
        }
        
        // 3. Pilih otomatis Kendaraan (Mencocokkan NAMA, bukan ID)
        String namaKendaraanTabel = t.getNama_kendaraan();
        for (int i = 0; i < view.getKendaraanCombo().getItemCount(); i++) {
            String item = view.getKendaraanCombo().getItemAt(i);
            if (item != null && namaKendaraanTabel != null && item.toLowerCase().contains(namaKendaraanTabel.toLowerCase())) {
                view.getKendaraanCombo().setSelectedIndex(i);
                break;
            }
        }
        
        // 4. Isi tanggal dan nominal
        view.getSewaSpinner().setValue(t.getTanggal_sewa());
        view.getKembaliSpinner().setValue(t.getTanggal_kembali());
        view.getLamaSewaTF().setText(String.valueOf(t.getLama_hari()));
        view.getDendaTF().setText(String.valueOf(t.getDenda()));
        view.getTotalBiayaTF().setText("Rp " + nf.format(t.getTotal_bayar()));
    }

    // INI DIA FUNGSI YANG TADI MENGHILANG!
    public void isiTableTransaksi() {
        List<Transaksi> list = daoTransaksi.getAktif();
        TabelModelTransaksi tableModel = new TabelModelTransaksi(list);
        view.getJTable1().setModel(tableModel);
    }

    public void resetForm() {
        isiComboPelanggan();
        isiComboKendaraan();
        view.getSewaSpinner().setValue(new Date());
        view.getKembaliSpinner().setValue(new Date());
        view.getLamaSewaTF().setText("0");
        view.getDendaTF().setText("0");
        view.getTotalBiayaTF().setText("Rp 0");
        view.getPenyewaTF().setText("");
        view.getJTable1().clearSelection();
        bersihkanDetailKendaraan();
        kendaraanDipilih = null;
    }

    private void updateStatusKendaraan(String jenis, int id, String status) {
        String tabel = "Mobil".equals(jenis) ? "tbl_mobil" : "tbl_motor";
        String kolumId = "Mobil".equals(jenis) ? "id_mobil" : "id_motor";
        String sql = "UPDATE " + tabel + " SET status=? WHERE " + kolumId + "=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Gagal update status: " + ex.getMessage());
        }
    }

    private int getHargaSewaDariDB(Transaksi t) {
        String jenis = t.getJenis_kendaraan();
        String tabel = "Mobil".equals(jenis) ? "tbl_mobil" : "tbl_motor";
        String kolumId = "Mobil".equals(jenis) ? "id_mobil" : "id_motor";
        int id = "Mobil".equals(jenis) ? (t.getId_mobil() != null ? t.getId_mobil() : 0) : (t.getId_motor() != null ? t.getId_motor() : 0);
        String sql = "SELECT harga_sewa FROM " + tabel + " WHERE " + kolumId + "=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("harga_sewa");
        } catch (SQLException ex) { }
        return 0;
    }
    
    // ══════════════════════════════════════════════════════
    //  11. FUNGSI CARI DATA PENYEWA
    // ══════════════════════════════════════════════════════
    public void cariData() {
        // Ambil teks dari JTextField pencarian (asumsi namanya jTextField1)
        String keyword = view.getPenyewaTF().getText().trim();
        if (keyword.isEmpty()) {
            isiTableTransaksi(); // Jika kosong, tampilkan semua data
            return;
        }
        List<Transaksi> list = daoTransaksi.getCariNama(keyword);
        TabelModelTransaksi tableModel = new TabelModelTransaksi(list);
        view.getJTable1().setModel(tableModel);
    }
}