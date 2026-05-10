/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package mvc.View;

import java.awt.*;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.*;
import mvc.DAO.DAOKendaraan;
import mvc.DAO.DAOTransaksi;
import mvc.Model.Kendaraan;

/**
 * Dashboard utama aplikasi Rental Kendaraan.
 *
 * Menampilkan:
 *  - Sidebar navigasi (Dashboard, Data Master, Transaksi, Keluar)
 *  - Statistik: Total Kendaraan, Sedang Disewa, Pelanggan Aktif
 *  - Bar chart Ringkasan Pendapatan per bulan
 *
 * @author RentalKendaraan
 */
public class FormDashboard extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(FormDashboard.class.getName());

    // ─── Warna tema ───────────────────────────────────────
    private static final Color SIDEBAR_BG     = new Color(30, 39, 46);
    private static final Color SIDEBAR_ACTIVE = new Color(41, 128, 185);
    private static final Color SIDEBAR_HOVER  = new Color(44, 62, 80);
    private static final Color SIDEBAR_TEXT   = Color.WHITE;
    private static final Color CONTENT_BG     = new Color(245, 246, 250);
    private static final Color CARD_BG        = Color.WHITE;

    // ─── Komponen ─────────────────────────────────────────
    private JPanel sidebarPanel;
    private JButton btnNavDashboard, btnNavDataMaster, btnNavTransaksi, btnNavKeluar;
    private JLabel lblTotalKendaraan, lblMotor, lblMobil;
    private JLabel lblDisewa, lblPelanggan;
    private GrafikBulanan grafikPanel;

    // ─── DAO ──────────────────────────────────────────────
    private final DAOKendaraan daoKendaraan;
    private final DAOTransaksi daoTransaksi;

    public FormDashboard() {
        daoKendaraan = new DAOKendaraan();
        daoTransaksi = new DAOTransaksi();
        initComponents();
        this.setLocationRelativeTo(null);
        muatStatistik();
    }

    // ──────────────────────────────────────────────────────
    //  initComponents
    // ──────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private void initComponents() {
        setTitle("RentalKendaraan-OOP v1.0 – Dashboard");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(820, 600);
        setLayout(new BorderLayout());

        // ── Sidebar ──
        sidebarPanel = buatSidebar();
        add(sidebarPanel, BorderLayout.WEST);

        // ── Content ──
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(CONTENT_BG);

        // Judul
        JPanel judulPanel = new JPanel(new BorderLayout());
        judulPanel.setBackground(CONTENT_BG);
        judulPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 10, 25));

        JLabel lblJudul = new JLabel("Dashboard Utama");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblJudul.setForeground(new Color(30, 39, 46));

        JLabel lblSub = new JLabel("Selamat Datang, Admin!");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSub.setForeground(new Color(100, 100, 100));

        judulPanel.add(lblJudul, BorderLayout.NORTH);
        judulPanel.add(lblSub, BorderLayout.CENTER);
        contentPanel.add(judulPanel, BorderLayout.NORTH);

        // Statistik + Grafik
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(CONTENT_BG);
        mainContent.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        mainContent.add(buatStatistikPanel());
        mainContent.add(Box.createVerticalStrut(20));
        mainContent.add(buatGrafikPanel());

        contentPanel.add(mainContent, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        pack();
        setSize(820, 600);
    }

    // ══════════════════════════════════════════════════════
    //  Sidebar
    // ══════════════════════════════════════════════════════
    private JPanel buatSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(180, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel lblLogo = new JLabel("  RentalKendaraan", SwingConstants.LEFT);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 0));
        sidebar.add(lblLogo);

        sidebar.add(Box.createVerticalStrut(10));

        btnNavDashboard  = buatNavButton("  \uD83D\uDCC8  Dashboard",   true);  // aktif
        btnNavDataMaster = buatNavButton("  \uD83D\uDC64  Data Master", false);
        btnNavTransaksi  = buatNavButton("  \uD83D\uDED2  Transaksi",   false);
        btnNavKeluar     = buatNavButton("  \u2192  Keluar",            false);

        sidebar.add(btnNavDashboard);
        sidebar.add(btnNavDataMaster);
        sidebar.add(btnNavTransaksi);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnNavKeluar);
        sidebar.add(Box.createVerticalStrut(20));

        // Event
        btnNavDashboard.addActionListener(e -> muatStatistik()); // refresh
        btnNavDataMaster.addActionListener(e -> {
            FormData fd = new FormData();
            fd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fd.setVisible(true);
        });
        btnNavTransaksi.addActionListener(e -> {
            new FormTransaksi().setVisible(true);
        });
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
        if (!aktif) {
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                    btn.setBackground(SIDEBAR_HOVER);
                }
                @Override public void mouseExited(java.awt.event.MouseEvent e) {
                    btn.setBackground(SIDEBAR_BG);
                }
            });
        }
        return btn;
    }

    // ══════════════════════════════════════════════════════
    //  Panel Statistik (3 kartu)
    // ══════════════════════════════════════════════════════
    private JPanel buatStatistikPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setBackground(CONTENT_BG);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        // Kartu 1: Total Kendaraan
        JPanel card1 = buatKartu(new Color(41, 128, 185));
        JLabel iconK  = buatLabelIcon("\uD83D\uDE97 \uD83C\uDFCD");
        lblTotalKendaraan = new JLabel("TOTAL KENDARAAN: 0");
        lblTotalKendaraan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalKendaraan.setForeground(Color.WHITE);
        lblMotor = new JLabel("Motor: 0");
        lblMotor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMotor.setForeground(new Color(200, 230, 255));
        lblMobil = new JLabel("Mobil: 0");
        lblMobil.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMobil.setForeground(new Color(200, 230, 255));
        card1.add(iconK);
        card1.add(lblTotalKendaraan);
        card1.add(lblMotor);
        card1.add(lblMobil);

        // Kartu 2: Sedang Disewa
        JPanel card2 = buatKartu(new Color(39, 174, 96));
        JLabel iconD = buatLabelIcon("\uD83D\uDD11");
        lblDisewa = new JLabel("SEDANG DISEWA: 0");
        lblDisewa.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDisewa.setForeground(Color.WHITE);
        JLabel lblDisewaInfo = new JLabel("Unit sedang di luar");
        lblDisewaInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDisewaInfo.setForeground(new Color(200, 255, 220));
        card2.add(iconD);
        card2.add(lblDisewa);
        card2.add(lblDisewaInfo);

        // Kartu 3: Pelanggan Aktif
        JPanel card3 = buatKartu(new Color(142, 68, 173));
        JLabel iconP = buatLabelIcon("\uD83D\uDC65");
        lblPelanggan = new JLabel("PELANGGAN: 0");
        lblPelanggan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPelanggan.setForeground(Color.WHITE);
        JLabel lblPlgInfo = new JLabel("Terdaftar di sistem");
        lblPlgInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPlgInfo.setForeground(new Color(220, 200, 255));
        card3.add(iconP);
        card3.add(lblPelanggan);
        card3.add(lblPlgInfo);

        panel.add(card1);
        panel.add(card2);
        panel.add(card3);

        return panel;
    }

    private JPanel buatKartu(Color bgColor) {
        JPanel card = new JPanel();
        card.setBackground(bgColor);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        return card;
    }

    private JLabel buatLabelIcon(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    // ══════════════════════════════════════════════════════
    //  Panel Grafik Pendapatan
    // ══════════════════════════════════════════════════════
    private JPanel buatGrafikPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(CONTENT_BG);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        JLabel lblGrafik = new JLabel("Ringkasan Pendapatan");
        lblGrafik.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblGrafik.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        wrapper.add(lblGrafik, BorderLayout.NORTH);

        grafikPanel = new GrafikBulanan();
        grafikPanel.setPreferredSize(new Dimension(580, 200));
        wrapper.add(grafikPanel, BorderLayout.CENTER);

        return wrapper;
    }

    // ══════════════════════════════════════════════════════
    //  Muat Statistik dari DB
    // ══════════════════════════════════════════════════════
    private void muatStatistik() {
        // Hitung kendaraan
        java.util.List<Kendaraan> semuaKendaraan = daoKendaraan.getAll();
        int totalMotor = 0, totalMobil = 0;
        for (Kendaraan k : semuaKendaraan) {
            if ("Motor".equalsIgnoreCase(k.getJenis()))  totalMotor++;
            else if ("Mobil".equalsIgnoreCase(k.getJenis())) totalMobil++;
        }
        int total = totalMotor + totalMobil;

        lblTotalKendaraan.setText("TOTAL KENDARAAN: " + total);
        lblMotor.setText("Motor: " + totalMotor);
        lblMobil.setText("Mobil: " + totalMobil);

        int disewa = daoTransaksi.countDisewa();
        lblDisewa.setText("SEDANG DISEWA: " + disewa);

        int pelanggan = daoTransaksi.countPelangganAktif();
        lblPelanggan.setText("PELANGGAN AKTIF: " + pelanggan);

        // Grafik tahun berjalan
        int tahun = Calendar.getInstance().get(Calendar.YEAR);
        Map<Integer, Long> pendapatan = daoTransaksi.getPendapatanBulanan(tahun);
        grafikPanel.setData(pendapatan, tahun);
        grafikPanel.repaint();
    }

    // ══════════════════════════════════════════════════════
    //  Inner class: Panel Bar Chart
    // ══════════════════════════════════════════════════════
    /**
     * Panel kustom yang menggambar bar chart pendapatan per bulan.
     * Dicat ulang setiap kali data diperbarui via setData().
     */
    private static class GrafikBulanan extends JPanel {

        private Map<Integer, Long> data;
        private int tahun;

        private static final String[] LABEL_BULAN = {
            "Jan","Feb","Mar","Apr","Mei","Jun",
            "Jul","Agu","Sep","Okt","Nov","Des"
        };

        private static final Color BAR_COLOR    = new Color(52, 152, 219);
        private static final Color BAR_HOVER    = new Color(41, 128, 185);
        private static final Color CHART_BG     = new Color(36, 47, 61);
        private static final Color GRID_COLOR   = new Color(60, 75, 90);
        private static final Color TEXT_COLOR   = new Color(200, 210, 220);

        public GrafikBulanan() {
            setBackground(CHART_BG);
            setBorder(BorderFactory.createLineBorder(new Color(50, 65, 80), 1));
        }

        public void setData(Map<Integer, Long> data, int tahun) {
            this.data  = data;
            this.tahun = tahun;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data == null) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int paddingL = 55, paddingR = 15, paddingT = 20, paddingB = 35;
            int chartW = w - paddingL - paddingR;
            int chartH = h - paddingT - paddingB;

            // Nilai maks untuk skala
            long maxVal = data.values().stream().mapToLong(Long::longValue).max().orElse(1L);
            if (maxVal == 0) maxVal = 1;

            int barCount = 12;
            int barWidth = (int) (chartW / (barCount * 1.6));
            int gap = (chartW - barWidth * barCount) / (barCount + 1);

            // ── Garis grid horizontal ──
            g2.setColor(GRID_COLOR);
            g2.setStroke(new BasicStroke(0.8f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1f, new float[]{4, 4}, 0));
            int gridCount = 4;
            for (int i = 0; i <= gridCount; i++) {
                int y = paddingT + chartH - (i * chartH / gridCount);
                g2.drawLine(paddingL, y, paddingL + chartW, y);
                // Label nilai
                long labelVal = maxVal * i / gridCount;
                String valStr = labelVal >= 1_000_000
                    ? String.format("%.1fM", labelVal / 1_000_000.0)
                    : labelVal >= 1_000
                        ? String.format("%.0fK", labelVal / 1_000.0)
                        : String.valueOf(labelVal);
                g2.setColor(TEXT_COLOR);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.drawString(valStr, 2, y + 4);
                g2.setColor(GRID_COLOR);
            }

            // ── Batang ──
            g2.setStroke(new BasicStroke(1f));
            for (int bulan = 1; bulan <= 12; bulan++) {
                long val = data.getOrDefault(bulan, 0L);
                int barH = (int) ((double) val / maxVal * chartH);
                int x = paddingL + gap + (bulan - 1) * (barWidth + gap);
                int y = paddingT + chartH - barH;

                // Gradient bar
                GradientPaint gp = new GradientPaint(
                    x, y, BAR_HOVER,
                    x, y + barH, BAR_COLOR);
                g2.setPaint(gp);
                g2.fillRoundRect(x, y, barWidth, barH, 4, 4);

                // Nilai di atas bar
                if (val > 0) {
                    g2.setColor(TEXT_COLOR);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
                    String valShort = val >= 1_000_000
                        ? String.format("%.1fM", val / 1_000_000.0)
                        : String.format("%.0fK", val / 1_000.0);
                    FontMetrics fm = g2.getFontMetrics();
                    int tx = x + (barWidth - fm.stringWidth(valShort)) / 2;
                    if (y - 3 > paddingT) g2.drawString(valShort, tx, y - 3);
                }

                // Label bulan
                g2.setColor(TEXT_COLOR);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                FontMetrics fm = g2.getFontMetrics();
                String lbl = LABEL_BULAN[bulan - 1];
                int tx = x + (barWidth - fm.stringWidth(lbl)) / 2;
                g2.drawString(lbl, tx, h - paddingB + 14);
            }

            // ── Sumbu ──
            g2.setColor(TEXT_COLOR);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(paddingL, paddingT, paddingL, paddingT + chartH);
            g2.drawLine(paddingL, paddingT + chartH, paddingL + chartW, paddingT + chartH);

            // ── Judul tahun ──
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.setColor(new Color(150, 180, 210));
            g2.drawString("Tahun " + tahun, paddingL + chartW - 60, paddingT + 12);
        }
    }

    // ══════════════════════════════════════════════════════
    //  Main — Entry Point Aplikasi
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
        java.awt.EventQueue.invokeLater(() -> new FormDashboard().setVisible(true));
    }
}
