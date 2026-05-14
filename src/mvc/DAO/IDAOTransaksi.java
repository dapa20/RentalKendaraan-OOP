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
 * @author dapa
 */
public interface IDAOTransaksi {

    /** Simpan transaksi baru */
    void insert(Transaksi t);

    void updateStatus(int id_transaksi, String status, int denda, int total_bayar);
    List<Transaksi> getAll();
    List<Transaksi> getAktif();
    List<Transaksi> getCariNama (String nama);
    public void update(Transaksi t); 
    public void delete(int id);      
    int countDisewa();
    int countPelangganAktif();
    Map<Integer, Long> getPendapatanBulanan(int tahun);
}
