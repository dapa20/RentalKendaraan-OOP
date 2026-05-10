/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import mvc.DAO.DAOTransaksi;
import mvc.Koneksi.Koneksi;
import mvc.Model.Kendaraan;
import mvc.Model.TabelModelTransaksi;
import mvc.Model.Transaksi;
import mvc.View.FormTransaksi;

/**
 * Controller untuk Form Transaksi Sewa Kendaraan.
 *
 * Bertanggung-jawab atas:
 *  1. Mengisi ComboBox pelanggan & kendaraan
 *  2. Menampilkan detail kendaraan saat dipilih
 *  3. Menghitung lama sewa, denda, dan total biaya
 *  4. Menyimpan transaksi → mengubah status kendaraan menjadi "Disewa"
 *  5. Menyelesaikan sewa → mengembalikan status kendaraan ke "Tersedia"
 *  6. Memuat tabel Daftar Sewa Aktif
 *
 * @author RentalKendaraan
 */
public class ControllerTransaksi {

    private final FormTransaksi view;
    private final DAOTransaksi daoTransaksi;
    private final Connection connection;

    // ─── Format ───────────────────────────────────────────
    private final NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("id", "ID"));
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Denda per hari = 10% × harga_sewa kendaraan.
     * Berlaku jika tanggal kembali aktual melebihi rencana.
     */
    private static final double PERSEN_DENDA = 0.10;

    // ─── State kendaraan terpilih ─────────────────────────
    private Kendaraan kendaraanDipilih = null;

    public ControllerTransaksi(FormTransaksi view) {
        this.view      = view;
        this.daoTransaksi = new DAOTransaksi();
        this.connection   = Koneksi.connection();
    }

    // ══════════════════════════════════════════════════════
    //  1. ISI COMBO BOX
    // ══════════════════════════════════════════════════════

    /** Mengisi comboBox pelanggan dari tbl_pelanggan */
    public void isiComboPelanggan() {
        JComboBox<String> combo = view.getComboPelanggan();
        combo.removeAllItems();
        combo.addItem("-- Pilih Pelanggan --");

        String sql = "SELECT id_pelanggan, nama_lengkap FROM tbl_pelanggan ORDER BY nama_lengkap";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Format: "id|nama" — diurai saat dibutuhkan
                combo.addItem(rs.getInt("id_pelanggan") + "|" + rs.getString("nama_lengkap"));
            }
        } catch (SQLException ex) {
            System.out.println("Gagal isi combo pelanggan: " + ex.getMessage());
        }

        // Tampilkan hanya nama (bukan id) via custom renderer
        combo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null) {
                    String s = value.toString();
                    setText(s.contains("|") ? s.split("\\|", 2)[1] : s);
                }
                return this;
            }
        });
    }

    /**
     * Mengisi comboBox kendaraan: hanya kendaraan berstatus "Tersedia".
     * Format item: "jenis|id|plat|model|harga"
     */
    public void isiComboKendaraan() {
        JComboBox<String> combo = view.getComboKendaraan();
        combo.removeAllItems();
        combo.addItem("-- Pilih Kendaraan --");

        String sqlMobil = "SELECT id_mobil AS id, plat_nomor, model_mobil AS model, harga_sewa, 'Mobil' AS jenis "
            + "FROM tbl_mobil WHERE status='Tersedia'";
        String sqlMotor = "SELECT id_motor AS id, plat_nomor, model_motor AS model, harga_sewa, 'Motor' AS jenis "
            + "FROM tbl_motor WHERE status='Tersedia'";
        String sql = sqlMobil + " UNION ALL " + sqlMotor + " ORDER BY model";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String item = rs.getString("jenis") + "|"
                    + rs.getInt("id")         + "|"
                    + rs.getString("plat_nomor") + "|"
                    + rs.getString("model")      + "|"
                    + rs.getInt("harga_sewa");
                combo.addItem(item);
            }
        } catch (SQLException ex) {
            System.out.println("Gagal isi combo kendaraan: " + ex.getMessage());
        }

        combo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null) {
                    String s = value.toString();
                    if (s.contains("|")) {
                        String[] p = s.split("\\|");
                        // Tampilkan: "Model Plat (Jenis)"
                        setText(p[3] + " " + p[2] + " (" + p[0] + ")");
                    } else {
                        setText(s);
                    }
                }
                return this;
            }
        });
    }

    // ══════════════════════════════════════════════════════
    //  2. EVENT: KENDARAAN DIPILIH
    // ══════════════════════════════════════════════════════

    /**
     * Dipanggil saat item di comboKendaraan berubah.
     * Mengisi panel Detail Kendaraan Dipilih dan menyimpan referensi lokal.
     */
    public void onKendaraanDipilih() {
        Object selected = view.getComboKendaraan().getSelectedItem();
        if (selected == null || !selected.toString().contains("|")) {
            bersihkanDetailKendaraan();
            kendaraanDipilih = null;
            return;
        }

        String[] parts = selected.toString().split("\\|");
        // parts: [jenis, id, plat, model, harga]
        kendaraanDipilih = new Kendaraan();
        kendaraanDipilih.setJenis(parts[0]);
        kendaraanDipilih.setId(Integer.parseInt(parts[1]));
        kendaraanDipilih.setPlat_nomor(parts[2]);
        kendaraanDipilih.setModel_kendaraan(parts[3]);
        kendaraanDipilih.setHarga_sewa(Integer.parseInt(parts[4]));

        // Tampilkan di panel kanan
        view.getLblDetailPlat().setText(kendaraanDipilih.getPlat_nomor());
        view.getLblDetailMerk().setText(kendaraanDipilih.getModel_kendaraan());
        view.getLblDetailJenis().setText(kendaraanDipilih.getJenis());
        view.getLblDetailHarga().setText(nf.format(kendaraanDipilih.getHarga_sewa()));

        // Hitung ulang kalau tanggal sudah terisi
        hitungTotalBiaya();
    }

    private void bersihkanDetailKendaraan() {
        view.getLblDetailPlat().setText("-");
        view.getLblDetailMerk().setText("-");
        view.getLblDetailJenis().setText("-");
        view.getLblDetailHarga().setText("-");
    }

    // ══════════════════════════════════════════════════════
    //  3. KALKULASI LAMA SEWA & TOTAL BIAYA
    // ══════════════════════════════════════════════════════

    /**
     * Hitung lama sewa (hari) = tanggal_kembali − tanggal_sewa.
     * Dipanggil setiap kali salah satu tanggal berubah.
     * Hasilnya dimasukkan ke txtLamaSewa (read-only).
     */
    public void hitungLamaSewa() {
        try {
            Date tglSewa    = (Date) view.getSpinnerTglSewa().getValue();
            Date tglKembali = (Date) view.getSpinnerTglKembali().getValue();

            if (tglSewa == null || tglKembali == null) return;

            long diffMs   = tglKembali.getTime() - tglSewa.getTime();
            long lamaHari = TimeUnit.MILLISECONDS.toDays(diffMs);

            if (lamaHari < 0) {
                JOptionPane.showMessageDialog(view,
                    "Tanggal kembali tidak boleh sebelum tanggal sewa!",
                    "Perhatian", JOptionPane.WARNING_MESSAGE);
                view.getTxtLamaSewa().setText("0");
                view.getLblTotalBiaya().setText("Rp 0");
                return;
            }

            view.getTxtLamaSewa().setText(String.valueOf(lamaHari));
            hitungTotalBiaya();

        } catch (Exception ex) {
            System.out.println("Error hitung lama sewa: " + ex.getMessage());
        }
    }

    /**
     * Hitung total biaya:
     *   total = (lama_hari × harga_sewa) + denda
     *
     * Denda otomatis: jika tanggal kembali aktual sudah lewat,
     *   denda = hari_terlambat × harga_sewa × PERSEN_DENDA
     * Namun denda tetap bisa diubah manual di txtDenda.
     */
    public void hitungTotalBiaya() {
        if (kendaraanDipilih == null) return;

        try {
            int lamaHari = Integer.parseInt(view.getTxtLamaSewa().getText().trim());
            int hargaSewa = kendaraanDipilih.getHarga_sewa();

            // Ambil denda dari field (bisa 0 atau sudah diisi)
            String dendaStr = view.getTxtDenda().getText().trim();
            int denda = dendaStr.isEmpty() ? 0 : Integer.parseInt(dendaStr);

            long total = (long) lamaHari * hargaSewa + denda;
            view.getLblTotalBiaya().setText("Rp " + nf.format(total));

        } catch (NumberFormatException ex) {
            view.getLblTotalBiaya().setText("Rp 0");
        }
    }

    /**
     * Hitung denda otomatis berdasarkan keterlambatan.
     * Dipanggil saat menyelesaikan sewa (kembali lebih telat dari rencana).
     *
     * @param tglRencana  tanggal kembali yang disepakati
     * @param tglAktual   tanggal kembali aktual
     * @param hargaSewa   harga sewa per hari kendaraan
     * @return nominal denda dalam rupiah
     */
    public int hitungDendaOtomatis(Date tglRencana, Date tglAktual, int hargaSewa) {
        if (tglAktual.after(tglRencana)) {
            long selisihMs    = tglAktual.getTime() - tglRencana.getTime();
            long hariTerlambat = TimeUnit.MILLISECONDS.toDays(selisihMs);
            return (int) (hariTerlambat * hargaSewa * PERSEN_DENDA);
        }
        return 0;
    }

    // ══════════════════════════════════════════════════════
    //  4. SIMPAN TRANSAKSI
    // ══════════════════════════════════════════════════════

    /**
     * Menyimpan transaksi baru:
     *  (a) Validasi input
     *  (b) INSERT tbl_transaksi_rental
     *  (c) UPDATE status kendaraan → "Disewa"
     *  (d) Refresh tabel & reset form
     */
    public void simpanTransaksi() {
        // (a) Validasi
        if (!validasiInput()) return;

        // Parse pelanggan
        String itemPelanggan = view.getComboPelanggan().getSelectedItem().toString();
        int idPelanggan = Integer.parseInt(itemPelanggan.split("\\|")[0]);

        // Parse tanggal
        Date tglSewa    = (Date) view.getSpinnerTglSewa().getValue();
        Date tglKembali = (Date) view.getSpinnerTglKembali().getValue();

        int lamaHari = Integer.parseInt(view.getTxtLamaSewa().getText().trim());
        int hargaSewa = kendaraanDipilih.getHarga_sewa();

        // Denda (bisa 0)
        String dendaStr = view.getTxtDenda().getText().trim();
        int denda = dendaStr.isEmpty() ? 0 : Integer.parseInt(dendaStr);

        int totalBayar = lamaHari * hargaSewa + denda;

        // (b) Buat objek Transaksi
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

        // (c) Update status kendaraan → "Disewa"
        updateStatusKendaraan(kendaraanDipilih.getJenis(), kendaraanDipilih.getId(), "Disewa");

        JOptionPane.showMessageDialog(view,
            "Transaksi berhasil disimpan!\nTotal Biaya: Rp " + nf.format(totalBayar),
            "Sukses", JOptionPane.INFORMATION_MESSAGE);

        // (d) Refresh
        isiTableTransaksi();
        isiComboKendaraan();     // refresh kendaraan tersedia
        resetForm();
    }

    // ══════════════════════════════════════════════════════
    //  5. SELESAIKAN SEWA
    // ══════════════════════════════════════════════════════

    /**
     * Dipanggil saat baris di tabel dipilih lalu tombol "Selesaikan" diklik.
     * Menghitung denda keterlambatan (jika ada), update status transaksi,
     * dan mengembalikan status kendaraan ke "Tersedia".
     */
    public void selesaikanSewa() {
        JTable tabel = view.getTabelTransaksi();
        int row = tabel.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view,
                "Pilih transaksi yang akan diselesaikan!",
                "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        mvc.Model.TabelModelTransaksi model =
            (TabelModelTransaksi) tabel.getModel();
        Transaksi t = model.getTransaksiAt(row);

        if ("Selesai".equals(t.getStatus_rental())) {
            JOptionPane.showMessageDialog(view,
                "Transaksi ini sudah selesai.",
                "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Hitung denda otomatis berdasarkan tanggal hari ini
        Date tglAktual  = new Date();
        Date tglRencana = t.getTanggal_kembali();

        // Dapatkan harga sewa dari DB
        int hargaSewa = getHargaSewaDariDB(t);
        int dendaBaru = hitungDendaOtomatis(tglRencana, tglAktual, hargaSewa);
        int totalBaru = t.getLama_hari() * hargaSewa + dendaBaru;

        String pesan = "Selesaikan sewa kendaraan?\n"
            + "Kendaraan : " + t.getNama_kendaraan() + "\n"
            + "Pelanggan : " + t.getNama_pelanggan()  + "\n";
        if (dendaBaru > 0) {
            pesan += "⚠ Denda terlambat: Rp " + nf.format(dendaBaru) + "\n";
        }
        pesan += "Total Bayar: Rp " + nf.format(totalBaru);

        int konfirmasi = JOptionPane.showConfirmDialog(view, pesan,
            "Konfirmasi Selesai Sewa", JOptionPane.YES_NO_OPTION);
        if (konfirmasi != JOptionPane.YES_OPTION) return;

        // Update status transaksi
        daoTransaksi.updateStatus(t.getId_transaksi(), "Selesai", dendaBaru, totalBaru);

        // Kembalikan status kendaraan → "Tersedia"
        String jenis = t.getJenis_kendaraan();
        int idKendaraan = "Mobil".equals(jenis)
            ? (t.getId_mobil() != null ? t.getId_mobil() : 0)
            : (t.getId_motor() != null ? t.getId_motor() : 0);
        updateStatusKendaraan(jenis, idKendaraan, "Tersedia");

        JOptionPane.showMessageDialog(view,
            "Sewa berhasil diselesaikan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        isiTableTransaksi();
        isiComboKendaraan();
    }

    // ══════════════════════════════════════════════════════
    //  6. ISI TABEL DAFTAR SEWA AKTIF
    // ══════════════════════════════════════════════════════

    /** Memuat semua transaksi ke JTable di FormTransaksi */
    public void isiTableTransaksi() {
        List<Transaksi> list = daoTransaksi.getAll();
        TabelModelTransaksi tableModel = new TabelModelTransaksi(list);
        view.getTabelTransaksi().setModel(tableModel);
        aturLebarKolom();
    }

    // ══════════════════════════════════════════════════════
    //  7. RESET FORM
    // ══════════════════════════════════════════════════════

    public void resetForm() {
        view.getComboPelanggan().setSelectedIndex(0);
        view.getComboKendaraan().setSelectedIndex(0);
        view.getSpinnerTglSewa().setValue(new Date());
        view.getSpinnerTglKembali().setValue(new Date());
        view.getTxtLamaSewa().setText("0");
        view.getTxtDenda().setText("");
        view.getLblTotalBiaya().setText("Rp 0");
        bersihkanDetailKendaraan();
        kendaraanDipilih = null;
    }

    // ══════════════════════════════════════════════════════
    //  Private Helpers
    // ══════════════════════════════════════════════════════

    private boolean validasiInput() {
        Object pilihPelanggan = view.getComboPelanggan().getSelectedItem();
        if (pilihPelanggan == null || !pilihPelanggan.toString().contains("|")) {
            JOptionPane.showMessageDialog(view, "Pilih pelanggan terlebih dahulu!",
                "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (kendaraanDipilih == null) {
            JOptionPane.showMessageDialog(view, "Pilih kendaraan terlebih dahulu!",
                "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String lamaStr = view.getTxtLamaSewa().getText().trim();
        if (lamaStr.isEmpty() || Integer.parseInt(lamaStr) <= 0) {
            JOptionPane.showMessageDialog(view,
                "Lama sewa harus lebih dari 0 hari!\nPeriksa tanggal sewa dan tanggal kembali.",
                "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Update status pada tbl_mobil atau tbl_motor.
     * @param jenis "Mobil" / "Motor"
     */
    private void updateStatusKendaraan(String jenis, int id, String status) {
        String tabel   = "Mobil".equals(jenis) ? "tbl_mobil"  : "tbl_motor";
        String kolumId = "Mobil".equals(jenis) ? "id_mobil"   : "id_motor";
        String sql = "UPDATE " + tabel + " SET status=? WHERE " + kolumId + "=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Gagal update status kendaraan: " + ex.getMessage());
        }
    }

    /** Ambil harga sewa kendaraan dari DB berdasarkan jenis transaksi */
    private int getHargaSewaDariDB(Transaksi t) {
        String jenis = t.getJenis_kendaraan();
        String tabel   = "Mobil".equals(jenis) ? "tbl_mobil"  : "tbl_motor";
        String kolumId = "Mobil".equals(jenis) ? "id_mobil"   : "id_motor";
        int id = "Mobil".equals(jenis)
            ? (t.getId_mobil() != null ? t.getId_mobil() : 0)
            : (t.getId_motor() != null ? t.getId_motor() : 0);

        String sql = "SELECT harga_sewa FROM " + tabel + " WHERE " + kolumId + "=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("harga_sewa");
        } catch (SQLException ex) {
            System.out.println("Gagal ambil harga sewa: " + ex.getMessage());
        }
        return 0;
    }

    /** Atur lebar kolom tabel agar lebih rapi */
    private void aturLebarKolom() {
        JTable tabel = view.getTabelTransaksi();
        int[] lebar = {80, 140, 170, 90, 90, 70, 120, 80};
        for (int i = 0; i < lebar.length && i < tabel.getColumnCount(); i++) {
            tabel.getColumnModel().getColumn(i).setPreferredWidth(lebar[i]);
        }
    }
}
