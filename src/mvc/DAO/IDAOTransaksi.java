/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mvc.DAO;

import java.util.List;
import java.util.Map;
import mvc.Model.Transaksi;

/**
 * Interface DAO untuk operasi Transaksi Sewa
 * @author RentalKendaraan
 */
public interface IDAOTransaksi {

    /** Simpan transaksi baru */
    void insert(Transaksi t);

    /** Update status transaksi (misal: Berjalan → Selesai) */
    void updateStatus(int id_transaksi, String status, int denda, int total_bayar);

    /** Ambil semua transaksi (JOIN pelanggan + kendaraan) */
    List<Transaksi> getAll();

    /** Ambil transaksi yang masih berjalan */
    List<Transaksi> getAktif();

    /** Hitung total kendaraan sedang disewa */
    int countDisewa();

    /** Hitung total pelanggan yang pernah/sedang rental */
    int countPelangganAktif();

    /**
     * Pendapatan per bulan dalam satu tahun.
     * Key = nomor bulan (1-12), Value = total_bayar
     */
    Map<Integer, Long> getPendapatanBulanan(int tahun);
}
