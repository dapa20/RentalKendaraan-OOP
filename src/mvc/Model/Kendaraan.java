/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.Model;


/**
 *
 * @author ACER
 */
public class Kendaraan {
    private Integer id; 
    private String plat_nomor;
    private String model_kendaraan; 
    private String kategori;
    private Integer harga_sewa;
    private String status;
    private String jenis; // Mobil atau Motor

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPlat_nomor() { return plat_nomor; }
    public void setPlat_nomor(String plat_nomor) { this.plat_nomor = plat_nomor; }

    public String getModel_kendaraan() { return model_kendaraan; }
    public void setModel_kendaraan(String model_kendaraan) { this.model_kendaraan = model_kendaraan; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public Integer getHarga_sewa() { return harga_sewa; }
    public void setHarga_sewa(Integer harga_sewa) { this.harga_sewa = harga_sewa; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }
}
