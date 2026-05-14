/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package mvc.View;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author dapa
 */
public class FormData extends javax.swing.JFrame {
    mvc.Controller.ControllerPelanggan cbt;
    mvc.Controller.ControllerKendaraan cbtKendaraan;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormData.class.getName());

    /**
     * Creates new form FormDat
     */
    public FormData() {
        initComponents();
        this.setLocationRelativeTo(null); // Biar form muncul di tengah layar
        cbt = new mvc.Controller.ControllerPelanggan(this); // Menghubungkan ke controller
        cbt.isiTable();
        initKendaraanPanel();
        cbtKendaraan = new mvc.Controller.ControllerKendaraan(this);
        cbtKendaraan.isiTable();
    }

    private void initKendaraanPanel() {
        jPanel2.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Row 0: Cari ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        jPanel2.add(new JLabel("Cari Kendaraan:"), gbc);
        txtCariKendaraan = new JTextField(20);
        gbc.gridx = 1; gbc.weightx = 1.0;
        jPanel2.add(txtCariKendaraan, gbc);
        btnCariKendaraan = new JButton("Cari");
        gbc.gridx = 2; gbc.weightx = 0;
        jPanel2.add(btnCariKendaraan, gbc);
        btnCariKendaraan.addActionListener(e -> cbtKendaraan.cari());

        // --- Row 1: Plat Nomor ---
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        jPanel2.add(new JLabel("Plat Nomor:"), gbc);
        txtPlat = new JTextField(20);
        gbc.gridx = 1; gbc.weightx = 1.0;
        jPanel2.add(txtPlat, gbc);

        // --- Row 2: Jenis ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        jPanel2.add(new JLabel("Jenis:"), gbc);
        cbJenis = new JComboBox<>(new String[]{"Mobil", "Motor"});
        gbc.gridx = 1; gbc.weightx = 1.0;
        jPanel2.add(cbJenis, gbc);

        // --- Row 3: Model Kendaraan ---
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        jPanel2.add(new JLabel("Model Kendaraan:"), gbc);
        txtModelKendaraan = new JTextField(20);
        gbc.gridx = 1; gbc.weightx = 1.0;
        jPanel2.add(txtModelKendaraan, gbc);

        // --- Row 4: Kategori ---
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        jPanel2.add(new JLabel("Kategori:"), gbc);
        txtKategori = new JTextField(20);
        gbc.gridx = 1; gbc.weightx = 1.0;
        jPanel2.add(txtKategori, gbc);

        // --- Row 5: Harga Sewa ---
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        jPanel2.add(new JLabel("Harga Sewa:"), gbc);
        txtHargaSewa = new JTextField(20);
        gbc.gridx = 1; gbc.weightx = 1.0;
        jPanel2.add(txtHargaSewa, gbc);

        // --- Row 6: Status ---
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
        jPanel2.add(new JLabel("Status:"), gbc);
        cbStatus = new JComboBox<>(new String[]{"Tersedia", "Disewa"});
        gbc.gridx = 1; gbc.weightx = 1.0;
        jPanel2.add(cbStatus, gbc);

        // --- Buttons column (rows 1-6) ---
        JPanel btnPanel = new JPanel(new GridBagLayout());
        GridBagConstraints bg = new GridBagConstraints();
        bg.fill = GridBagConstraints.HORIZONTAL;
        bg.insets = new Insets(4, 4, 4, 4);
        bg.gridx = 0;

        btnSimpanKendaraan = new JButton("Simpan");
        bg.gridy = 0; btnPanel.add(btnSimpanKendaraan, bg);
        btnSimpanKendaraan.addActionListener(e -> cbtKendaraan.insert());

        btnUbahKendaraan = new JButton("Ubah");
        bg.gridy = 1; btnPanel.add(btnUbahKendaraan, bg);
        btnUbahKendaraan.addActionListener(e -> cbtKendaraan.update());

        btnHapusKendaraan = new JButton("Hapus");
        bg.gridy = 2; btnPanel.add(btnHapusKendaraan, bg);
        btnHapusKendaraan.addActionListener(e -> cbtKendaraan.delete());

        btnBatalKendaraan = new JButton("Batal");
        bg.gridy = 3; btnPanel.add(btnBatalKendaraan, bg);
        btnBatalKendaraan.addActionListener(e -> cbtKendaraan.reset());

        gbc.gridx = 2; gbc.gridy = 1; gbc.gridheight = 6;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.NORTH;
        jPanel2.add(btnPanel, gbc);
        gbc.gridheight = 1; gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Row 7: Table ---
        tabelKendaraan = new JTable();
        tabelKendaraan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tabelKendaraan.getSelectedRow();
                if (row != -1) cbtKendaraan.isiField(row);
            }
        });
        JScrollPane scrollKendaraan = new JScrollPane(tabelKendaraan);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 3;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jPanel2.add(scrollKendaraan, gbc);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnCari = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelPelanggan = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTelp = new javax.swing.JTextField();
        txtNik = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        txtAlamat = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(this::btnSimpanActionPerformed);

        btnUbah.setText("Ubah");
        btnUbah.addActionListener(this::btnUbahActionPerformed);

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(this::btnHapusActionPerformed);

        btnBatal.setText("Batal");
        btnBatal.addActionListener(this::btnBatalActionPerformed);

        btnCari.setText("Cari");
        btnCari.addActionListener(this::btnCariActionPerformed);

        tabelPelanggan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelPelanggan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelPelangganMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelPelanggan);

        jLabel1.setText("Alamat: ");

        jLabel2.setText("Nama:");

        jLabel3.setText("NIK: ");

        jLabel4.setText("No. Telepon: ");

        txtTelp.addActionListener(this::txtTelpActionPerformed);

        txtNik.addActionListener(this::txtNikActionPerformed);

        txtNama.addActionListener(this::txtNamaActionPerformed);

        txtAlamat.addActionListener(this::txtAlamatActionPerformed);

        jLabel5.setText("Cari Nama/NIK:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtCari, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNik, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtAlamat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                                .addComponent(txtTelp, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtNama, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGap(79, 79, 79)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnCari, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .addComponent(btnSimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnUbah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBatal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 775, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCari)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnSimpan)
                        .addGap(18, 18, 18)
                        .addComponent(btnUbah)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapus)
                        .addGap(18, 18, 18)
                        .addComponent(btnBatal))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(txtNik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );

        jTabbedPane1.addTab("Data Pelanggan", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 851, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 657, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Data Kendaraan", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 851, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 692, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        cbt.insert();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        cbt.cari();
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        cbt.update();
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        cbt.delete();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void txtNikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNikActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNikActionPerformed

    private void txtAlamatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAlamatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAlamatActionPerformed

    private void txtTelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelpActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        cbt.reset();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void tabelPelangganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelPelangganMouseClicked
        int row = tabelPelanggan.getSelectedRow();
        cbt.isiField(row);
    }//GEN-LAST:event_tabelPelangganMouseClicked

    private void txtNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FormData().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tabelPelanggan;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNik;
    private javax.swing.JTextField txtTelp;
    // End of variables declaration//GEN-END:variables

    public static Logger getLogger() {
        return logger;
    }

    public JButton getBtnBatal() {
        return btnBatal;
    }

    public JButton getBtnCari() {
        return btnCari;
    }

    public JButton getBtnHapus() {
        return btnHapus;
    }

    public JButton getBtnSimpan() {
        return btnSimpan;
    }

    public JButton getBtnUbah() {
        return btnUbah;
    }

    public JLabel getjLabel1() {
        return jLabel1;
    }

    public JLabel getjLabel2() {
        return jLabel2;
    }

    public JLabel getjLabel3() {
        return jLabel3;
    }

    public JLabel getjLabel4() {
        return jLabel4;
    }

    public JLabel getjLabel5() {
        return jLabel5;
    }

    public JPanel getjPanel1() {
        return jPanel1;
    }

    public JPanel getjPanel2() {
        return jPanel2;
    }

    public JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public JTabbedPane getjTabbedPane1() {
        return jTabbedPane1;
    }

    public JTable getTabelPelanggan() {
        return tabelPelanggan;
    }

    public JTextField getTxtAlamat() {
        return txtAlamat;
    }

    public JTextField getTxtCari() {
        return txtCari;
    }

    public JTextField getTxtNama() {
        return txtNama;
    }

    public JTextField getTxtNik() {
        return txtNik;
    }

    public JTextField getTxtTelp() {
        return txtTelp;
    }

    // ===== Kendaraan Components =====
    private javax.swing.JTextField txtPlat;
    private javax.swing.JTextField txtModelKendaraan;
    private javax.swing.JTextField txtKategori;
    private javax.swing.JTextField txtHargaSewa;
    private javax.swing.JComboBox<String> cbJenis;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JTextField txtCariKendaraan;
    private javax.swing.JTable tabelKendaraan;
    private javax.swing.JButton btnSimpanKendaraan;
    private javax.swing.JButton btnUbahKendaraan;
    private javax.swing.JButton btnHapusKendaraan;
    private javax.swing.JButton btnBatalKendaraan;
    private javax.swing.JButton btnCariKendaraan;

    public JTextField getTxtPlat() { return txtPlat; }
    public JTextField getTxtModelKendaraan() { return txtModelKendaraan; }
    public JTextField getTxtKategori() { return txtKategori; }
    public JTextField getTxtHargaSewa() { return txtHargaSewa; }
    public JComboBox<String> getCbJenis() { return cbJenis; }
    public JComboBox<String> getCbStatus() { return cbStatus; }
    public JTextField getTxtCariKendaraan() { return txtCariKendaraan; }
    public JTable getTabelKendaraan() { return tabelKendaraan; }
}
