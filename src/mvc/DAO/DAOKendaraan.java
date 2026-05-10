/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mvc.Koneksi.Koneksi;
import mvc.Model.Kendaraan;
/**
 *
 * @author ACER
 */
public class DAOKendaraan implements IDAOKendaraan {
    Connection connection;
    public DAOKendaraan() {
        connection = Koneksi.connection();
    }

    @Override
    public void insert(Kendaraan k) {
        PreparedStatement statement = null;
        String sql = "";
        
        if (k.getJenis().equals("Mobil")) {
            sql = "INSERT INTO tbl_mobil (plat_nomor, model_mobil, kategori, harga_sewa, status) VALUES (?, ?, ?, ?, ?);";
        } else {
            sql = "INSERT INTO tbl_motor (plat_nomor, model_motor, kategori, harga_sewa, status) VALUES (?, ?, ?, ?, ?);";
        }
        
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, k.getPlat_nomor());
            statement.setString(2, k.getModel_kendaraan());
            statement.setString(3, k.getKategori());
            statement.setInt(4, k.getHarga_sewa());
            statement.setString(5, k.getStatus());
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Gagal Insert Kendaraan: " + ex.getMessage());
        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException ex) {}
        }
    }

    @Override
    public void update(Kendaraan k) {
        PreparedStatement statement = null;
        String sql = "";
        
        if (k.getJenis().equals("Mobil")) {
            sql = "UPDATE tbl_mobil SET plat_nomor=?, model_mobil=?, kategori=?, harga_sewa=?, status=? WHERE id_mobil=?;";
        } else {
            sql = "UPDATE tbl_motor SET plat_nomor=?, model_motor=?, kategori=?, harga_sewa=?, status=? WHERE id_motor=?;";
        }
        
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, k.getPlat_nomor());
            statement.setString(2, k.getModel_kendaraan());
            statement.setString(3, k.getKategori());
            statement.setInt(4, k.getHarga_sewa());
            statement.setString(5, k.getStatus());
            statement.setInt(6, k.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Gagal Update Kendaraan: " + ex.getMessage());
        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException ex) {}
        }
    }

    @Override
    public void delete(int id, String jenis) {
        PreparedStatement statement = null;
        String sql = "";
        
        if (jenis.equals("Mobil")) {
            sql = "DELETE FROM tbl_mobil WHERE id_mobil=?;";
        } else {
            sql = "DELETE FROM tbl_motor WHERE id_motor=?;";
        }
        
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Gagal Delete Kendaraan: " + ex.getMessage());
        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException ex) {}
        }
    }

    @Override
    public List<Kendaraan> getAll() {
        List<Kendaraan> lb = new ArrayList<>();
        String sql = "SELECT id_mobil AS id, plat_nomor, model_mobil AS model_kendaraan, kategori, harga_sewa, status, 'Mobil' AS jenis FROM tbl_mobil " +
                     "UNION ALL " +
                     "SELECT id_motor AS id, plat_nomor, model_motor AS model_kendaraan, kategori, harga_sewa, status, 'Motor' AS jenis FROM tbl_motor";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Kendaraan k = new Kendaraan();
                k.setId(rs.getInt("id"));
                k.setPlat_nomor(rs.getString("plat_nomor"));
                k.setModel_kendaraan(rs.getString("model_kendaraan"));
                k.setKategori(rs.getString("kategori"));
                k.setHarga_sewa(rs.getInt("harga_sewa"));
                k.setStatus(rs.getString("status"));
                k.setJenis(rs.getString("jenis"));
                lb.add(k);
            }
        } catch (SQLException ex) {
            System.out.println("Gagal Tampil Kendaraan: " + ex.getMessage());
        }
        return lb;
    }

    @Override
    public List<Kendaraan> getCari(String keyword) {
        List<Kendaraan> lb = new ArrayList<>();
        String sql = "SELECT id_mobil AS id, plat_nomor, model_mobil AS model_kendaraan, kategori, harga_sewa, status, 'Mobil' AS jenis FROM tbl_mobil WHERE model_mobil LIKE ? " +
                     "UNION ALL " +
                     "SELECT id_motor AS id, plat_nomor, model_motor AS model_kendaraan, kategori, harga_sewa, status, 'Motor' AS jenis FROM tbl_motor WHERE model_motor LIKE ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, "%" + keyword + "%");
            st.setString(2, "%" + keyword + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Kendaraan k = new Kendaraan();
                k.setId(rs.getInt("id"));
                k.setPlat_nomor(rs.getString("plat_nomor"));
                k.setModel_kendaraan(rs.getString("model_kendaraan"));
                k.setKategori(rs.getString("kategori"));
                k.setHarga_sewa(rs.getInt("harga_sewa"));
                k.setStatus(rs.getString("status"));
                k.setJenis(rs.getString("jenis"));
                lb.add(k);
            }
        } catch (SQLException ex) {
            System.out.println("Gagal Cari Kendaraan: " + ex.getMessage());
        }
        return lb;
    }
    
}
