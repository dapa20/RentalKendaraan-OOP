package mvc.Koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    private static Connection koneksi;
    
    public static Connection connection() {
        if (koneksi == null) {
            try {
                // nama database kita: db_rental
                String url = "jdbc:mysql://localhost:3306/db_rental";
                String user = "root";
                String pass = ""; // isi kalo xampp pake pw
                
                // Load driver secara dinamis
                Class.forName("com.mysql.cj.jdbc.Driver");
                koneksi = DriverManager.getConnection(url, user, pass);
                System.out.println("Koneksi ke Database Rental Berhasil!");
                
            } catch (ClassNotFoundException e) {
                System.out.println("Driver tidak ditemukan: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Koneksi Gagal: " + e.getMessage());
            }
        }
        return koneksi;
    }
}