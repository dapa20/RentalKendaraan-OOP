/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mvc;

import mvc.View.FormDashboard;

/**
 * Entry point utama aplikasi Rental Kendaraan OOP.
 * Jalankan class ini untuk memulai aplikasi.
 *
 * @author RentalKendaraan
 */
public class Main {

    public static void main(String[] args) {
        // Set Look & Feel Nimbus agar tampilan lebih modern
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info
                    : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger
                .getLogger(Main.class.getName())
                .log(java.util.logging.Level.SEVERE, null, ex);
        }

        // Buka FormDashboard sebagai jendela utama
        java.awt.EventQueue.invokeLater(() -> new FormDashboard().setVisible(true));
    }
}
