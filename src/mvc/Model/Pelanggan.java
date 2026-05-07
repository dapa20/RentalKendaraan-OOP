/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.Model;

/**
 *
 * @author dapa
 */
public class Pelanggan {
    private Integer id_pelanggan;
    private String nik;
    private String nama_lengkap;
    private String no_telp;
    private String alamat;

    public Integer getId_pelanggan() {
        return id_pelanggan;
    }

    public void setId_pelanggan(Integer id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
    
    
}
