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
import mvc.Model.Pelanggan;
/**
 *
 * @author dapa
 */
public class DAOPelanggan implements IDAOPelanggan {
    Connection connection;
    
    // Query SQL CRUD
    final String insert = "INSERT INTO tbl_pelanggan (nik, nama_lengkap, no_telp, alamat) VALUES (?, ?, ?, ?);";
    final String update = "UPDATE tbl_pelanggan SET nik=?, nama_lengkap=?, no_telp=?, alamat=? WHERE id_pelanggan=?;";
    final String delete = "DELETE FROM tbl_pelanggan WHERE id_pelanggan=?;";
    final String select = "SELECT * FROM tbl_pelanggan;";
    final String carinama = "SELECT * FROM tbl_pelanggan WHERE nama_lengkap LIKE ?;";

    public DAOPelanggan() {
        connection = Koneksi.connection();
    }

    @Override
    public void insert(Pelanggan b) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(insert);
            statement.setString(1, b.getNik());
            statement.setString(2, b.getNama_lengkap());
            statement.setString(3, b.getNo_telp());
            statement.setString(4, b.getAlamat());
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Gagal Insert: " + ex.getMessage());
        } finally {
            try { statement.close(); } catch (SQLException ex) {}
        }
    }

    @Override
    public void update(Pelanggan b) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(update);
            statement.setString(1, b.getNik());
            statement.setString(2, b.getNama_lengkap());
            statement.setString(3, b.getNo_telp());
            statement.setString(4, b.getAlamat());
            statement.setInt(5, b.getId_pelanggan());
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Gagal Update: " + ex.getMessage());
        } finally {
            try { statement.close(); } catch (SQLException ex) {}
        }
    }

    @Override
    public void delete(int id) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(delete);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Gagal Delete: " + ex.getMessage());
        } finally {
            try { statement.close(); } catch (SQLException ex) {}
        }
    }

    @Override
    public List<Pelanggan> getAll() {
        List<Pelanggan> lb = null;
        try {
            lb = new ArrayList<Pelanggan>();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(select);
            while (rs.next()) {
                Pelanggan b = new Pelanggan();
                b.setId_pelanggan(rs.getInt("id_pelanggan"));
                b.setNik(rs.getString("nik"));
                b.setNama_lengkap(rs.getString("nama_lengkap"));
                b.setNo_telp(rs.getString("no_telp"));
                b.setAlamat(rs.getString("alamat"));
                lb.add(b);
            }
        } catch (SQLException ex) {
            System.out.println("Gagal Tampil: " + ex.getMessage());
        }
        return lb;
    }

    @Override
    public List<Pelanggan> getCariNama(String nama) {
        List<Pelanggan> lb = null;
        try {
            lb = new ArrayList<Pelanggan>();
            PreparedStatement st = connection.prepareStatement(carinama);
            st.setString(1, "%" + nama + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Pelanggan b = new Pelanggan();
                b.setId_pelanggan(rs.getInt("id_pelanggan"));
                b.setNik(rs.getString("nik"));
                b.setNama_lengkap(rs.getString("nama_lengkap"));
                b.setNo_telp(rs.getString("no_telp"));
                b.setAlamat(rs.getString("alamat"));
                lb.add(b);
            }
        } catch (SQLException ex) {
            System.out.println("Gagal Cari: " + ex.getMessage());
        }
        return lb;
    }
}
