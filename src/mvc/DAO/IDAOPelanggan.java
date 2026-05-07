/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mvc.DAO;

import java.util.List;
import mvc.Model.Pelanggan;
/**
 *
 * @author dapa
 */
public interface IDAOPelanggan {
    //CRUD
    // Fungsi untuk menambah data (Create)
    public void insert(Pelanggan b);
    // Fungsi untuk mengubah data (Update)
    public void update(Pelanggan b);
    // Fungsi untuk menghapus data (Delete)
    public void delete(int id);
    // Fungsi untuk menampilkan semua data ke tabel (Read)
    public List<Pelanggan> getAll();
    // Fungsi untuk mencari data berdasarkan nama
    public List<Pelanggan> getCariNama(String nama);
}
