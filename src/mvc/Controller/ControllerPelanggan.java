package mvc.Controller;

import java.util.List;
import javax.swing.JOptionPane;
import mvc.DAO.DAOPelanggan;
import mvc.DAO.IDAOPelanggan;
import mvc.Model.Pelanggan;
import mvc.Model.TabelModelPelanggan;
import mvc.View.FormData;

/**
 *
 * @author shofwan
 */
public class ControllerPelanggan {
    FormData frame;
    IDAOPelanggan implPelanggan;
    List<Pelanggan> lb;

    // 1. Constructor: Menyambungkan Controller dengan FormData & Database (DAO)
    public ControllerPelanggan(FormData frame) {
        this.frame = frame;
        implPelanggan = new DAOPelanggan();
        lb = implPelanggan.getAll(); // Mengambil data awal dari database
    }

    // 2. Fungsi Batal/Reset: Mengosongkan semua kotak isian di layar
    public void reset() {
        frame.getTxtNik().setText("");
        frame.getTxtNama().setText("");
        frame.getTxtTelp().setText("");
        frame.getTxtAlamat().setText("");
        frame.getTxtCari().setText("");
    }

    // 3. Fungsi Tampil Data: Memasukkan data dari database ke dalam JTable
    public void isiTable() {
        lb = implPelanggan.getAll();
        TabelModelPelanggan tmb = new TabelModelPelanggan(lb);
        frame.getTabelPelanggan().setModel(tmb);
    }

    // 4. Fungsi Simpan: Mengambil ketikan dari FormData, lalu kirim ke Database
    public void insert() {
        if (!frame.getTxtNik().getText().trim().isEmpty() && !frame.getTxtNama().getText().trim().isEmpty()) {
            
            Pelanggan b = new Pelanggan();
            b.setNik(frame.getTxtNik().getText());
            b.setNama_lengkap(frame.getTxtNama().getText());
            b.setNo_telp(frame.getTxtTelp().getText());
            b.setAlamat(frame.getTxtAlamat().getText());
            
            implPelanggan.insert(b); 
            JOptionPane.showMessageDialog(null, "Data Pelanggan Berhasil Disimpan.");
            
            reset();     
            isiTable();  
            
        } else {
            JOptionPane.showMessageDialog(frame, "Ada Data yang Belum Diisi", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // 5. Fungsi Mengisi Field saat Tabel diklik
    public void isiField(int row) {
        frame.getTxtNik().setText(lb.get(row).getNik());
        frame.getTxtNama().setText(lb.get(row).getNama_lengkap());
        frame.getTxtTelp().setText(lb.get(row).getNo_telp());
        frame.getTxtAlamat().setText(lb.get(row).getAlamat());
    }
    
    // 6. Fungsi Tombol Ubah (Update)
    public void update() {
        // Cek baris mana yang sedang diklik di tabel
        int row = frame.getTabelPelanggan().getSelectedRow();
        
        // Kalau ada baris yang dipilih (row tidak sama dengan -1)
        if (row != -1) {
            Pelanggan b = new Pelanggan();
            
            // INI KUNCI UTAMANYA: Ambil ID Pelanggan dari list (lb) dan masukkan ke objek
            b.setId_pelanggan(lb.get(row).getId_pelanggan()); 
            
            b.setNik(frame.getTxtNik().getText());
            b.setNama_lengkap(frame.getTxtNama().getText());
            b.setNo_telp(frame.getTxtTelp().getText());
            b.setAlamat(frame.getTxtAlamat().getText());

            implPelanggan.update(b);
            JOptionPane.showMessageDialog(null, "Data Berhasil Diperbarui!");
            isiTable();
            reset();
        } else {
            JOptionPane.showMessageDialog(frame, "Pilih data di tabel terlebih dahulu untuk diubah!");
        }
    }
    
    // 7. Fungsi Tombol Hapus (Delete)
    public void delete() {
        int row = frame.getTabelPelanggan().getSelectedRow();
        if (row != -1) {
            int confirm = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Ambil ID Pelanggan yang asli dari list, bukan dari text field
                int id = lb.get(row).getId_pelanggan();
            
            implPelanggan.delete(id);
                JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus!");
                isiTable();
                reset();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Pilih dulu data yang mau dihapus di tabel!");
        }
    }
    
    // 8. Fungsi Cari
    public void cari() {
        String keyword = frame.getTxtCari().getText().trim();
        if (!keyword.isEmpty()) {
            // PENTING: Update isi variabel 'lb' dengan hasil pencarian
            lb = implPelanggan.getCariNama(keyword); 
        
            // Tampilkan ke tabel
            TabelModelPelanggan tmb = new TabelModelPelanggan(lb);
            frame.getTabelPelanggan().setModel(tmb);
        } else {
        // Kalau kolom cari kosong, tampilkan semua data lagi
            isiTable();
        }
    }
}