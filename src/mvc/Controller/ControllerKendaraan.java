/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc.Controller;

import java.util.List;
import javax.swing.JOptionPane;
import mvc.DAO.DAOKendaraan;
import mvc.DAO.IDAOKendaraan;
import mvc.Model.Kendaraan;
import mvc.Model.TabelModelKendaraan;
import mvc.View.FormData;
/**
 *
 * @author ACER
 */
public class ControllerKendaraan {
    FormData frame;
    IDAOKendaraan implKendaraan;
    List<Kendaraan> lb;

    // 1. Constructor
    public ControllerKendaraan(FormData frame) {
        this.frame = frame;
        implKendaraan = new DAOKendaraan();
        lb = implKendaraan.getAll();
    }

    // 2. Reset Form
    public void reset() {
        frame.getTxtPlat().setText("");
        frame.getTxtModelKendaraan().setText("");
        frame.getTxtKategori().setText("");
        frame.getTxtHargaSewa().setText("");
        frame.getCbJenis().setSelectedIndex(0);
        frame.getCbStatus().setSelectedIndex(0);
        frame.getTxtCariKendaraan().setText("");
    }

    // 3. Tampil Tabel
    public void isiTable() {
        lb = implKendaraan.getAll();
        TabelModelKendaraan tmb = new TabelModelKendaraan(lb);
        frame.getTabelKendaraan().setModel(tmb);
    }

    // 4. Simpan Data (Insert)
    public void insert() {
        if (!frame.getTxtPlat().getText().trim().isEmpty() && !frame.getTxtModelKendaraan().getText().trim().isEmpty()) {
            Kendaraan k = new Kendaraan();
            
            k.setPlat_nomor(frame.getTxtPlat().getText());
            k.setJenis(frame.getCbJenis().getSelectedItem().toString());
            k.setModel_kendaraan(frame.getTxtModelKendaraan().getText());
            k.setKategori(frame.getTxtKategori().getText());
            k.setHarga_sewa(Integer.parseInt(frame.getTxtHargaSewa().getText()));
            k.setStatus(frame.getCbStatus().getSelectedItem().toString());

            implKendaraan.insert(k);
            JOptionPane.showMessageDialog(null, "Data Kendaraan Berhasil Disimpan.");
            reset();
            isiTable();
        } else {
            JOptionPane.showMessageDialog(frame, "Plat Nomor dan Model tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    // 5. Isi Field Saat Tabel Diklik
    public void isiField(int row) {
        frame.getTxtPlat().setText(lb.get(row).getPlat_nomor());
        frame.getCbJenis().setSelectedItem(lb.get(row).getJenis());
        frame.getTxtModelKendaraan().setText(lb.get(row).getModel_kendaraan());
        frame.getTxtKategori().setText(lb.get(row).getKategori());
        frame.getTxtHargaSewa().setText(String.valueOf(lb.get(row).getHarga_sewa()));
        frame.getCbStatus().setSelectedItem(lb.get(row).getStatus());
    }

    // 6. Ubah Data (Update)
    public void update() {
        int row = frame.getTabelKendaraan().getSelectedRow();
        if (row != -1) {
            Kendaraan k = new Kendaraan();
            
            k.setId(lb.get(row).getId());
            k.setJenis(lb.get(row).getJenis()); // Patokan tabel (Mobil/Motor) diambil dari data lama
            
            k.setPlat_nomor(frame.getTxtPlat().getText());
            k.setModel_kendaraan(frame.getTxtModelKendaraan().getText());
            k.setKategori(frame.getTxtKategori().getText());
            k.setHarga_sewa(Integer.parseInt(frame.getTxtHargaSewa().getText()));
            k.setStatus(frame.getCbStatus().getSelectedItem().toString());

            implKendaraan.update(k);
            JOptionPane.showMessageDialog(null, "Data Kendaraan Berhasil Diperbarui!");
            isiTable();
            reset();
        } else {
            JOptionPane.showMessageDialog(frame, "Pilih data di tabel terlebih dahulu!");
        }
    }

    // 7. Hapus Data (Delete)
    public void delete() {
        int row = frame.getTabelKendaraan().getSelectedRow();
        if (row != -1) {
            int confirm = JOptionPane.showConfirmDialog(null, "Yakin hapus kendaraan ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int id = lb.get(row).getId();
                String jenis = lb.get(row).getJenis();
                
                implKendaraan.delete(id, jenis);
                JOptionPane.showMessageDialog(null, "Data Kendaraan Berhasil Dihapus!");
                isiTable();
                reset();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Pilih dulu data yang mau dihapus!");
        }
    }

    // 8. Cari Data
    public void cari() {
        String keyword = frame.getTxtCariKendaraan().getText().trim();
        if (!keyword.isEmpty()) {
            lb = implKendaraan.getCari(keyword);
            TabelModelKendaraan tmb = new TabelModelKendaraan(lb);
            frame.getTabelKendaraan().setModel(tmb);
        } else {
            isiTable();
        }
    }
}
