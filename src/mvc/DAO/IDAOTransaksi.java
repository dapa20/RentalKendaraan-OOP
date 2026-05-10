/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mvc.DAO;

import java.util.List;
import java.util.Map;
import mvc.Model.Transaksi;

/**
 *
 * @author shofwan
 */
public interface IDAOTransaksi {

    /** Simpan transaksi baru */
    void insert(Transaksi t);

    void updateStatus(int id_transaksi, String status, int denda, int total_bayar);
    List<Transaksi> getAll();
    List<Transaksi> getAktif();
    int countDisewa();
    int countPelangganAktif();
    Map<Integer, Long> getPendapatanBulanan(int tahun);
}
