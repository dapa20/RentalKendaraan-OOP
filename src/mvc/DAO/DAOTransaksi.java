/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import mvc.Koneksi.Koneksi;
import mvc.Model.Transaksi;

/**
 *
 * @author shofwan
 */
public class DAOTransaksi implements IDAOTransaksi {

    private final Connection connection;

    // ─── Query SQL ────────────────────────────────────────
    private static final String SQL_INSERT =
        "INSERT INTO tbl_transaksi_rental "
        + "(id_pelanggan, id_mobil, id_motor, tanggal_sewa, tanggal_kembali, lama_hari, denda, total_bayar, status_rental) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE_STATUS =
        "UPDATE tbl_transaksi_rental "
        + "SET status_rental=?, denda=?, total_bayar=? "
        + "WHERE id_transaksi=?";

    /** Query JOIN untuk mendapatkan nama pelanggan & kendaraan sekaligus */
    private static final String SQL_SELECT_ALL =
        "SELECT t.id_transaksi, t.id_pelanggan, t.id_mobil, t.id_motor, "
        + "  t.tanggal_sewa, t.tanggal_kembali, t.lama_hari, t.denda, t.total_bayar, t.status_rental, "
        + "  p.nama_lengkap AS nama_pelanggan, "
        + "  COALESCE(m.model_mobil, mo.model_motor) AS nama_kendaraan, "
        + "  COALESCE(m.plat_nomor, mo.plat_nomor) AS plat_kendaraan, "
        + "  IF(t.id_mobil IS NOT NULL, 'Mobil', 'Motor') AS jenis_kendaraan "
        + "FROM tbl_transaksi_rental t "
        + "JOIN tbl_pelanggan p ON t.id_pelanggan = p.id_pelanggan "
        + "LEFT JOIN tbl_mobil  m  ON t.id_mobil  = m.id_mobil "
        + "LEFT JOIN tbl_motor  mo ON t.id_motor  = mo.id_motor "
        + "ORDER BY t.id_transaksi DESC";

    private static final String SQL_SELECT_AKTIF =
        SQL_SELECT_ALL.replace(
            "ORDER BY t.id_transaksi DESC",
            "WHERE t.status_rental = 'Berjalan' ORDER BY t.id_transaksi DESC"
        );

    public DAOTransaksi() {
        connection = Koneksi.connection();
    }

    // ─── INSERT ───────────────────────────────────────────
    @Override
    public void insert(Transaksi t) {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(SQL_INSERT);
            stmt.setInt(1, t.getId_pelanggan());

            // id_mobil / id_motor: salah satu null
            if (t.getId_mobil() != null) {
                stmt.setInt(2, t.getId_mobil());
                stmt.setNull(3, java.sql.Types.INTEGER);
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
                stmt.setInt(3, t.getId_motor());
            }

            stmt.setDate(4, new Date(t.getTanggal_sewa().getTime()));
            stmt.setDate(5, new Date(t.getTanggal_kembali().getTime()));
            stmt.setInt(6, t.getLama_hari());
            stmt.setInt(7, t.getDenda());
            stmt.setInt(8, t.getTotal_bayar());
            stmt.setString(9, t.getStatus_rental());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Gagal Insert Transaksi: " + ex.getMessage());
        } finally {
            tutupStatement(stmt);
        }
    }

    // ─── UPDATE STATUS ────────────────────────────────────
    @Override
    public void updateStatus(int id_transaksi, String status, int denda, int total_bayar) {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(SQL_UPDATE_STATUS);
            stmt.setString(1, status);
            stmt.setInt(2, denda);
            stmt.setInt(3, total_bayar);
            stmt.setInt(4, id_transaksi);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Gagal Update Status Transaksi: " + ex.getMessage());
        } finally {
            tutupStatement(stmt);
        }
    }

    // ─── GET ALL ──────────────────────────────────────────
    @Override
    public List<Transaksi> getAll() {
        return eksekusiQuery(SQL_SELECT_ALL);
    }

    // ─── GET AKTIF ────────────────────────────────────────
    @Override
    public List<Transaksi> getAktif() {
        return eksekusiQuery(SQL_SELECT_AKTIF);
    }

    // ─── COUNT DISEWA ─────────────────────────────────────
    @Override
    public int countDisewa() {
        int count = 0;
        String sql = "SELECT "
            + "(SELECT COUNT(*) FROM tbl_mobil  WHERE status='Disewa') + "
            + "(SELECT COUNT(*) FROM tbl_motor  WHERE status='Disewa') AS total";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) count = rs.getInt("total");
        } catch (SQLException ex) {
            System.out.println("Gagal Count Disewa: " + ex.getMessage());
        }
        return count;
    }

    // ─── COUNT PELANGGAN AKTIF ────────────────────────────
    @Override
    public int countPelangganAktif() {
        int count = 0;
        String sql = "SELECT COUNT(*) AS total FROM tbl_pelanggan";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) count = rs.getInt("total");
        } catch (SQLException ex) {
            System.out.println("Gagal Count Pelanggan: " + ex.getMessage());
        }
        return count;
    }

    // ─── PENDAPATAN BULANAN ───────────────────────────────
    @Override
    public Map<Integer, Long> getPendapatanBulanan(int tahun) {
        // Inisialisasi semua bulan dengan 0
        Map<Integer, Long> result = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) result.put(i, 0L);

        String sql = "SELECT MONTH(tanggal_sewa) AS bulan, SUM(total_bayar) AS total "
            + "FROM tbl_transaksi_rental "
            + "WHERE YEAR(tanggal_sewa) = ? "
            + "GROUP BY MONTH(tanggal_sewa)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, tahun);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getInt("bulan"), rs.getLong("total"));
            }
        } catch (SQLException ex) {
            System.out.println("Gagal Ambil Pendapatan Bulanan: " + ex.getMessage());
        }
        return result;
    }

    // ─── Helper ───────────────────────────────────────────

    /** Eksekusi query SELECT dan mapping ResultSet → List<Transaksi> */
    private List<Transaksi> eksekusiQuery(String sql) {
        List<Transaksi> list = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Transaksi t = new Transaksi();
                t.setId_transaksi(rs.getInt("id_transaksi"));
                t.setId_pelanggan(rs.getInt("id_pelanggan"));

                int idMobil = rs.getInt("id_mobil");
                t.setId_mobil(rs.wasNull() ? null : idMobil);

                int idMotor = rs.getInt("id_motor");
                t.setId_motor(rs.wasNull() ? null : idMotor);

                t.setTanggal_sewa(rs.getDate("tanggal_sewa"));
                t.setTanggal_kembali(rs.getDate("tanggal_kembali"));
                t.setLama_hari(rs.getInt("lama_hari"));
                t.setDenda(rs.getInt("denda"));
                t.setTotal_bayar(rs.getInt("total_bayar"));
                t.setStatus_rental(rs.getString("status_rental"));
                t.setNama_pelanggan(rs.getString("nama_pelanggan"));

                String plat = rs.getString("plat_kendaraan");
                String namaK = rs.getString("nama_kendaraan");
                t.setNama_kendaraan(namaK + " " + plat);
                t.setJenis_kendaraan(rs.getString("jenis_kendaraan"));
                list.add(t);
            }
        } catch (SQLException ex) {
            System.out.println("Gagal Ambil Data Transaksi: " + ex.getMessage());
        }
        return list;
    }

    private void tutupStatement(PreparedStatement stmt) {
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException ex) { /* abaikan */ }
        }
    }
}
