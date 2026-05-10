/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package mvc.View;

import java.awt.*;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import mvc.Controller.ControllerTransaksi;

/**
 *
 * @author shofwan
 */
public class FormTransaksi extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(FormTransaksi.class.getName());

    // ─── Komponen UI ──────────────────────────────────────
    // Sidebar
    private JPanel sidebarPanel;
    private JButton btnNavDashboard, btnNavDataMaster, btnNavTransaksi, btnNavKeluar;

    // Form input
    private JComboBox<String> comboPelanggan;
    private JComboBox<String> comboKendaraan;
    private JSpinner spinnerTglSewa;
    private JSpinner spinnerTglKembali;
    private JTextField txtLamaSewa;
    private JTextField txtDenda;
    private JLabel lblTotalBiaya;
    private JButton btnSimpan, btnBatal, btnSelesaikan;

    // Detail kendaraan (panel kanan)
    private JLabel lblDetailPlat, lblDetailMerk, lblDetailJenis, lblDetailHarga;

    // Tabel
    private JTable tabelTransaksi;
    private JScrollPane scrollTabel;

    // Controller
    private ControllerTransaksi controller;

    // ─── Warna tema (sama dengan Dashboard) ───────────────
    private static final Color SIDEBAR_BG      = new Color(30, 39, 46);
    private static final Color SIDEBAR_ACTIVE  = new Color(41, 128, 185);
    private static final Color SIDEBAR_HOVER   = new Color(44, 62, 80);
    private static final Color SIDEBAR_TEXT    = Color.WHITE;
    private static final Color CONTENT_BG      = new Color(245, 246, 250);
    private static final Color HEADER_BG       = new Color(41, 128, 185);
    private static final Color BTN_SIMPAN      = new Color(39, 174, 96);
    private static final Color BTN_BATAL       = new Color(192, 57, 43);
    private static final Color BTN_SELESAI     = new Color(243, 156, 18);

    public FormTransaksi() {
        initComponents();
        this.setLocationRelativeTo(null);
        controller = new ControllerTransaksi(this);
        controller.isiComboPelanggan();
        controller.isiComboKendaraan();
        controller.isiTableTransaksi();
    }

    // ──────────────────────────────────────────────────────
    //  initComponents  (NetBeans-style)
    // ──────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private void initComponents() {
        setTitle("RentalKendaraan-OOP");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(950, 680);
        setLayout(new BorderLayout());

        // ── Sidebar ──────────────────────────────────────
        sidebarPanel = buatSidebar();
        add(sidebarPanel, BorderLayout.WEST);

        // ── Content ──────────────────────────────────────
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(CONTENT_BG);

        // Header
        JLabel lblHeader = new JLabel("  Transaksi Sewa Kendaraan");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setOpaque(true);
        lblHeader.setBackground(HEADER_BG);
        lblHeader.setPreferredSize(new Dimension(0, 50));
        contentPanel.add(lblHeader, BorderLayout.NORTH);

        // Form area
        JPanel formArea = new JPanel(new BorderLayout(10, 10));
        formArea.setBackground(CONTENT_BG);
        formArea.setBorder(BorderFactory.createEmptyBorder(12, 15, 10, 15));

        // ── Baris atas: form input + detail kendaraan ───
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        topPanel.setBackground(CONTENT_BG);
        topPanel.add(buatPanelDetailTransaksi());
        topPanel.add(buatPanelDetailKendaraan());
        formArea.add(topPanel, BorderLayout.NORTH);

        // ── Tabel ────────────────────────────────────────
        JPanel tablePanel = buatPanelTabel();
        formArea.add(tablePanel, BorderLayout.CENTER);

        contentPanel.add(formArea, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        pack();
        setSize(950, 680);
    }

    // ══════════════════════════════════════════════════════
    //  Sidebar
    // ══════════════════════════════════════════════════════
    private JPanel buatSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(180, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Logo / Judul
        JLabel lblLogo = new JLabel("  RentalKendaraan", SwingConstants.LEFT);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 0));
        sidebar.add(lblLogo);

        sidebar.add(Box.createVerticalStrut(10));

        btnNavDashboard  = buatNavButton("  \uD83D\uDCC8  Dashboard",   false);
        btnNavDataMaster = buatNavButton("  \uD83D\uDC64  Data Master", false);
        btnNavTransaksi  = buatNavButton("  \uD83D\uDED2  Transaksi",   true);  // aktif
        btnNavKeluar     = buatNavButton("  \u2192  Keluar",            false);

        sidebar.add(btnNavDashboard);
        sidebar.add(btnNavDataMaster);
        sidebar.add(btnNavTransaksi);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnNavKeluar);
        sidebar.add(Box.createVerticalStrut(20));

        // Event navigasi
        btnNavDashboard.addActionListener(e -> {
            new FormDashboard().setVisible(true);
            dispose();
        });
        btnNavDataMaster.addActionListener(e -> {
            FormData fd = new FormData();
            fd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fd.setVisible(true);
        });
        btnNavTransaksi.addActionListener(e -> { /* sudah di halaman ini */ });
        btnNavKeluar.addActionListener(e -> System.exit(0));

        return sidebar;
    }

    private JButton buatNavButton(String text, boolean aktif) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(SIDEBAR_TEXT);
        btn.setBackground(aktif ? SIDEBAR_ACTIVE : SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!aktif) btn.setBackground(SIDEBAR_HOVER);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                if (!aktif) btn.setBackground(SIDEBAR_BG);
            }
        });
        return btn;
    }

    // ══════════════════════════════════════════════════════
    //  Panel Detail Transaksi (kiri atas)
    // ══════════════════════════════════════════════════════
    private JPanel buatPanelDetailTransaksi() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Detail Transaksi",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);

        // ── Pilih Pelanggan ──
        comboPelanggan = new JComboBox<>();
        comboPelanggan.setFont(labelFont);
        addFormRow(panel, gbc, 0, "Pilih Pelanggan", comboPelanggan);

        // ── Pilih Kendaraan ──
        comboKendaraan = new JComboBox<>();
        comboKendaraan.setFont(labelFont);
        comboKendaraan.addActionListener(e -> {
            if (controller != null) controller.onKendaraanDipilih();
        });
        addFormRow(panel, gbc, 1, "Pilih Kendaraan", comboKendaraan);

        // ── Tanggal Sewa ──
        spinnerTglSewa = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorSewa = new JSpinner.DateEditor(spinnerTglSewa, "dd/MM/yyyy");
        spinnerTglSewa.setEditor(editorSewa);
        spinnerTglSewa.setValue(new Date());
        spinnerTglSewa.addChangeListener(e -> {
            if (controller != null) controller.hitungLamaSewa();
        });
        addFormRow(panel, gbc, 2, "Tanggal Sewa", spinnerTglSewa);

        // ── Tanggal Kembali ──
        spinnerTglKembali = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorKembali = new JSpinner.DateEditor(spinnerTglKembali, "dd/MM/yyyy");
        spinnerTglKembali.setEditor(editorKembali);
        spinnerTglKembali.setValue(new Date());
        spinnerTglKembali.addChangeListener(e -> {
            if (controller != null) controller.hitungLamaSewa();
        });
        addFormRow(panel, gbc, 3, "Tanggal Kembali", spinnerTglKembali);

        // ── Lama Sewa (read-only) ──
        txtLamaSewa = new JTextField("0");
        txtLamaSewa.setEditable(false);
        txtLamaSewa.setBackground(new Color(235, 235, 235));
        txtLamaSewa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addFormRow(panel, gbc, 4, "Lama Sewa (hari)", txtLamaSewa);

        // ── Denda ──
        txtDenda = new JTextField();
        txtDenda.setFont(labelFont);
        txtDenda.setToolTipText("Isi denda jika ada (Rp). Kosongkan jika tidak ada denda.");
        txtDenda.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) {
                if (controller != null) controller.hitungTotalBiaya();
            }
        });
        addFormRow(panel, gbc, 5, "Denda (Rp)", txtDenda);

        // ── Total Biaya ──
        lblTotalBiaya = new JLabel("Rp 0");
        lblTotalBiaya.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalBiaya.setForeground(new Color(41, 128, 185));
        addFormRow(panel, gbc, 6, "Total Biaya", lblTotalBiaya);

        // ── Tombol Simpan / Batal ──
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.setBackground(Color.WHITE);

        btnSimpan = buatTombol("Simpan Transaksi", BTN_SIMPAN);
        btnBatal  = buatTombol("Batal",            BTN_BATAL);
        btnSimpan.addActionListener(e -> controller.simpanTransaksi());
        btnBatal.addActionListener(e  -> controller.resetForm());

        btnPanel.add(btnSimpan);
        btnPanel.add(btnBatal);

        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 8, 8, 8);
        panel.add(btnPanel, gbc);

        return panel;
    }

    /** Helper: tambah baris label + komponen ke GridBag */
    private void addFormRow(JPanel panel, GridBagConstraints gbc,
                            int row, String labelText, JComponent comp) {
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lbl = new JLabel(labelText + " :");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(comp, gbc);
    }

    // ══════════════════════════════════════════════════════
    //  Panel Detail Kendaraan Dipilih (kanan atas)
    // ══════════════════════════════════════════════════════
    private JPanel buatPanelDetailKendaraan() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Detail Kendaraan Dipilih",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 12, 8, 12);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);
        Font valueFont = new Font("Segoe UI", Font.BOLD, 13);

        lblDetailPlat  = new JLabel("-"); lblDetailPlat.setFont(valueFont);
        lblDetailMerk  = new JLabel("-"); lblDetailMerk.setFont(valueFont);
        lblDetailJenis = new JLabel("-"); lblDetailJenis.setFont(valueFont);
        lblDetailHarga = new JLabel("-"); lblDetailHarga.setFont(valueFont);
        lblDetailHarga.setForeground(new Color(39, 174, 96));

        String[][] rows = {
            {"Plat Nomor",     "lblDetailPlat"},
            {"Merk / Tipe",    "lblDetailMerk"},
            {"Jenis",          "lblDetailJenis"},
            {"Harga Sewa/hari","lblDetailHarga"},
        };
        JLabel[] valueLabels = {lblDetailPlat, lblDetailMerk, lblDetailJenis, lblDetailHarga};

        for (int i = 0; i < rows.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.4;
            JLabel lbl = new JLabel(rows[i][0] + " :");
            lbl.setFont(labelFont);
            panel.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0.6;
            panel.add(valueLabels[i], gbc);
        }

        // Spacer bawah
        gbc.gridx = 0; gbc.gridy = rows.length;
        gbc.gridwidth = 2; gbc.weighty = 1;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    // ══════════════════════════════════════════════════════
    //  Panel Tabel Daftar Sewa
    // ══════════════════════════════════════════════════════
    private JPanel buatPanelTabel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(CONTENT_BG);

        // Header tabel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CONTENT_BG);

        JLabel lblJudul = new JLabel("Daftar Sewa Aktif");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 13));
        headerPanel.add(lblJudul, BorderLayout.WEST);

        btnSelesaikan = buatTombol("✔ Selesaikan Sewa", BTN_SELESAI);
        btnSelesaikan.addActionListener(e -> controller.selesaikanSewa());
        headerPanel.add(btnSelesaikan, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Tabel
        tabelTransaksi = new JTable();
        tabelTransaksi.setRowHeight(26);
        tabelTransaksi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelTransaksi.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelTransaksi.getTableHeader().setBackground(new Color(52, 73, 94));
        tabelTransaksi.getTableHeader().setForeground(Color.WHITE);
        tabelTransaksi.setSelectionBackground(new Color(174, 214, 241));
        tabelTransaksi.setGridColor(new Color(220, 220, 220));
        tabelTransaksi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Warnai baris berdasarkan status
        tabelTransaksi.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String status = "";
                    int statusCol = table.getColumnCount() - 1;
                    if (statusCol >= 0 && table.getValueAt(row, statusCol) != null) {
                        status = table.getValueAt(row, statusCol).toString();
                    }
                    if ("Selesai".equals(status)) {
                        c.setBackground(new Color(235, 245, 235)); // hijau muda
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                    }
                }
                return c;
            }
        });

        scrollTabel = new JScrollPane(tabelTransaksi);
        scrollTabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollTabel.setPreferredSize(new Dimension(0, 220));
        panel.add(scrollTabel, BorderLayout.CENTER);

        return panel;
    }

    // ══════════════════════════════════════════════════════
    //  Helper: Buat Tombol
    // ══════════════════════════════════════════════════════
    private JButton buatTombol(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 32));
        return btn;
    }

    // ══════════════════════════════════════════════════════
    //  Main (untuk test standalone)
    // ══════════════════════════════════════════════════════
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new FormTransaksi().setVisible(true));
    }

    // ══════════════════════════════════════════════════════
    //  Getter (digunakan Controller)
    // ══════════════════════════════════════════════════════
    public JComboBox<String> getComboPelanggan()    { return comboPelanggan; }
    public JComboBox<String> getComboKendaraan()    { return comboKendaraan; }
    public JSpinner getSpinnerTglSewa()             { return spinnerTglSewa; }
    public JSpinner getSpinnerTglKembali()          { return spinnerTglKembali; }
    public JTextField getTxtLamaSewa()              { return txtLamaSewa; }
    public JTextField getTxtDenda()                 { return txtDenda; }
    public JLabel getLblTotalBiaya()                { return lblTotalBiaya; }
    public JTable getTabelTransaksi()               { return tabelTransaksi; }
    public JLabel getLblDetailPlat()                { return lblDetailPlat; }
    public JLabel getLblDetailMerk()                { return lblDetailMerk; }
    public JLabel getLblDetailJenis()               { return lblDetailJenis; }
    public JLabel getLblDetailHarga()               { return lblDetailHarga; }
}
