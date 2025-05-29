# 🎮 Lantern Rite - Penjualan Game Online

**Lantern Rite** adalah aplikasi kasir penjualan game online berbasis desktop yang dikembangkan menggunakan Java (NetBeans IDE) dan MySQL. Aplikasi ini dirancang untuk menyederhanakan proses transaksi dengan berbagai fitur otomatis, seperti diskon belanja, cashback pembelian, diskon tambahan, perhitungan PPN, pencetakan nota, dan laporan penjualan.

---

## 🧾 Deskripsi

Aplikasi ini cocok digunakan oleh toko game online atau digital store untuk mengelola penjualan dengan efisien. Seluruh perhitungan dan pencatatan transaksi dilakukan secara otomatis, dan laporan penjualan dapat dicetak untuk keperluan dokumentasi atau evaluasi bisnis.

---

## ⚙️ Fitur Utama

- Input data pelanggan dan game beserta harga game dan sebagainya  
- Perhitungan Diskon belanja otomatis berdasarkan jumlah pembelian  
- Perhitungan Cashback Pembelian otomatis untuk pembelian tertentu  
- Perhitungan Diskon tambahan otomatis (misal: event atau promo)  
- Perhitungan otomatis PPN (Pajak Pertambahan Nilai)
- Perhitungan otomatis uang kembalian  
- Pencetakan nota transaksi setiap pembelian berdasarkan ID yang dipilih 
- Pencetakan laporan seluruh transaksi yang ada
- Koneksi ke database MySQL untuk penyimpanan data  

---

## 🛠 Teknologi yang Digunakan

- Java (NetBeans IDE)  
- Java Swing (GUI Desktop)  
- MySQL (sebagai database utama)  
- JasperReports (untuk cetak nota dan laporan)  
- JDBC (Java Database Connectivity)

---

## 📌 Cara Menggunakan Aplikasi

1. **Install XAMPP atau software MySQL Server lainnya.**  
2. **Buat database baru di MySQL** dengan nama `dblanternrite` atau sesuai nama yang digunakan dalam aplikasi.  
3. **Import file SQL database** (`dblanternrite.sql`).  
4. **Buka proyek ini di NetBeans IDE.**
7. **Cek konfigurasi koneksi database** di file koneksi (biasanya `config.java`), dan sesuaikan:  
   - Host: "jdbc:mysql://localhost:(port kamu)/`dblanternrite`" 
   - Username: `root`  
   - Password: *(kosong atau sesuai pengaturan XAMPP/MySQL kamu)*  
   - Nama database: `dblanternrite`  
8. **Pastikan driver JDBC MySQL (`mysql-connector-java-x.x.x.jar`) dan beberapa file `.jar` jasper report sudah ditambahkan ke Library Project.**  
9. Jalankan program dari NetBeans (tekan tombol **Run**).  
10. Setelah aplikasi berjalan, kamu bisa mulai:  
   - Menambahkan data pelanggan dan game beserta harga game dan sebagainya ke dalam database  
   - Melakukan transaksi penjualan  
   - Setelah menambahkan data pelanggan dan game serta harga game dan sebagainya ke dalam database, lalu tekan enter
   - Aplikasi akan otomatis menghitung diskon belanja, cashback pembelian, diskon belanja, dan PPN, lalu tekan enter 
   - Aplikasi akan otomatis menghitung uang kembalian
   - Nota transaksi setiap pembelian berdasarkan ID yang dipilih dapat dicetak  
   - Laporan penjualan seluruh transaksi yang ada dapat dicetak  

---

## 📂 Struktur Proyek (Umum)

- `src/` – Berisi kode program Java dan tampilan aplikasi
- - `src/laporan data konsumen` – Berisi file jasper report dari tampilan desain Cetak Nota dan Cetak Laporan
- `dblanternrite` – Berisi contoh database untuk import ke MySQL  
- `lib/` – File library `.jar` seperti `mysql-connector` dan jasper report 
- `assets/` – (opsional) Gambar atau ikon aplikasi  

---

## 📄 Lisensi

Proyek ini menggunakan lisensi **MIT**. Bebas digunakan dan dimodifikasi untuk kebutuhan pribadi, edukasi, atau bisnis dengan mencantumkan atribusi.
