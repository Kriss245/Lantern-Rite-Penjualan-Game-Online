<p align="center"><img src="https://imgur.com/pPLgP7U.png" width="350"></p>

# 🎮 Lantern Rite - Penjualan Game Online

> **Lantern Rite** adalah aplikasi kasir penjualan game online berbasis desktop yang dikembangkan menggunakan Java (NetBeans IDE) dan MySQL. Aplikasi ini dirancang untuk menyederhanakan proses transaksi dengan berbagai fitur otomatis, seperti diskon belanja, cashback pembelian, diskon tambahan, perhitungan PPN, pencetakan nota, dan laporan penjualan. Aplikasi ini cocok digunakan oleh toko game online atau digital store untuk mengelola penjualan dengan efisien. Seluruh perhitungan dan pencatatan transaksi dilakukan secara otomatis, dan laporan penjualan dapat dicetak untuk keperluan dokumentasi atau evaluasi bisnis.

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


## 📲 Contoh Tampilan Antarmuka

<p align="center"><img src="https://imgur.com/v87p5Ks.png" width="500"></p>
<p align="center" style="font-size:10px; color:gray;">
<em> Gambar 1. Tampilan Awal Program </em>
</p>

<br><br>

<p align="center"><img src="https://imgur.com/8aLRecl.png" width="400"></p>
<p align="center" style="font-size:10px; color:gray;">
<em> Gambar 2. Tampilan isi data pelanggan, game, dan harga game </em>
</p>

<br><br>

<p align="center"><img src="https://imgur.com/o3y1jJU.png" width="400"></p>
<p align="center" style="font-size:10px; color:gray;">
<em> Gambar 3. Tampilan setelah perhitungan otomatis </em>
</p>

<br><br>

<p align="center"><img src="https://imgur.com/t1SRXCf.png" width="400"></p>
<p align="center" style="font-size:10px; color:gray;">
<em> Gambar 4. Tampilan setelah tambah data ke dalam database </em>
</p>

<br><br>

<p align="center"><img src="https://imgur.com/CQfeNYb.png" width="400"></p>
<p align="center" style="font-size:10px; color:gray;">
<em> Gambar 5. Tampilan cetak nota setiap pembelian berdasarkan ID yang dipilih </em>
</p>

<br><br>

<p align="center"><img src="https://imgur.com/3GORueK.png" width="400"></p>
<p align="center" style="font-size:10px; color:gray;">
<em> Gambar 6. Tampilan cetak laporan seluruh transaksi yang ada </em>
</p>

---

## 📂 Struktur Proyek (Umum)

- `src/` – Berisi kode program Java dan tampilan aplikasi
- `src/laporan data konsumen` – Berisi file jasper report dari tampilan desain Cetak Nota dan Cetak Laporan
- `dblanternrite` – Berisi contoh database untuk import ke MySQL  
- `lib/` – File library `.jar` seperti `mysql-connector` dan `jasper report` 

---

## 📄 Lisensi

Proyek ini menggunakan lisensi **MIT**. Bebas digunakan dan dimodifikasi untuk kebutuhan pribadi, edukasi, atau bisnis dengan mencantumkan atribusi.
