-- =====================================================
-- UPDATE SCHEMA: db_rental
-- Tambah kolom tanggal_kembali & denda ke tbl_transaksi_rental
-- Jalankan script ini di phpMyAdmin atau MySQL CLI
-- =====================================================

USE db_rental;

ALTER TABLE `tbl_transaksi_rental`
    ADD COLUMN `tanggal_kembali` DATE NULL AFTER `tanggal_sewa`,
    ADD COLUMN `denda` INT(11) NOT NULL DEFAULT 0 AFTER `lama_hari`;

-- Pastikan kolom status_rental memiliki default 'Berjalan'
ALTER TABLE `tbl_transaksi_rental`
    MODIFY COLUMN `status_rental` VARCHAR(15) DEFAULT 'Berjalan';

-- Verifikasi struktur tabel
DESCRIBE tbl_transaksi_rental;
