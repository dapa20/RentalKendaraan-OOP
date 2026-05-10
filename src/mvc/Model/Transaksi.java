/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.Model;

import java.util.Date;

/**
 * Model untuk data Transaksi Sewa Kendaraan
 * @author RentalKendaraan
 */
public class Transaksi {

    private Integer id_transaksi;
    private Integer id_pelanggan;
    private Integer id_mobil;    // null jika kendaraan motor
    private Integer id_motor;    // null jika kendaraan mobil
    private Date tanggal_sewa;
    private Date tanggal_kembali;
    private Integer lama_hari;
    private Integer denda;
    private Integer total_bayar;
    private String status_rental;

    // ─── Field tampilan (JOIN) ─────────────────────────────
    private String nama_pelanggan;
    private String nama_kendaraan;
    private String jenis_kendaraan; // "Mobil" / "Motor"

    // ─── Getters & Setters ────────────────────────────────
    public Integer getId_transaksi() { return id_transaksi; }
    public void setId_transaksi(Integer id_transaksi) { this.id_transaksi = id_transaksi; }

    public Integer getId_pelanggan() { return id_pelanggan; }
    public void setId_pelanggan(Integer id_pelanggan) { this.id_pelanggan = id_pelanggan; }

    public Integer getId_mobil() { return id_mobil; }
    public void setId_mobil(Integer id_mobil) { this.id_mobil = id_mobil; }

    public Integer getId_motor() { return id_motor; }
    public void setId_motor(Integer id_motor) { this.id_motor = id_motor; }

    public Date getTanggal_sewa() { return tanggal_sewa; }
    public void setTanggal_sewa(Date tanggal_sewa) { this.tanggal_sewa = tanggal_sewa; }

    public Date getTanggal_kembali() { return tanggal_kembali; }
    public void setTanggal_kembali(Date tanggal_kembali) { this.tanggal_kembali = tanggal_kembali; }

    public Integer getLama_hari() { return lama_hari; }
    public void setLama_hari(Integer lama_hari) { this.lama_hari = lama_hari; }

    public Integer getDenda() { return denda; }
    public void setDenda(Integer denda) { this.denda = denda; }

    public Integer getTotal_bayar() { return total_bayar; }
    public void setTotal_bayar(Integer total_bayar) { this.total_bayar = total_bayar; }

    public String getStatus_rental() { return status_rental; }
    public void setStatus_rental(String status_rental) { this.status_rental = status_rental; }

    public String getNama_pelanggan() { return nama_pelanggan; }
    public void setNama_pelanggan(String nama_pelanggan) { this.nama_pelanggan = nama_pelanggan; }

    public String getNama_kendaraan() { return nama_kendaraan; }
    public void setNama_kendaraan(String nama_kendaraan) { this.nama_kendaraan = nama_kendaraan; }

    public String getJenis_kendaraan() { return jenis_kendaraan; }
    public void setJenis_kendaraan(String jenis_kendaraan) { this.jenis_kendaraan = jenis_kendaraan; }
}
