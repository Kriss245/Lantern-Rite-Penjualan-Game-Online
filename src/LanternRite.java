import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class LanternRite extends javax.swing.JFrame {

    /**
     * Creates new form UAS_Java
     */
    public LanternRite() {
        try {
            initComponents();  
            load_table();
            kosong();
            
            // Membuat field ini tidak bisa di edit saat akan menginput
            txtdiskontambahan.setEditable(false);
            txtppn.setEditable(false);
            txtdiskonbelanja.setEditable(false);
            txttotalpembayaran.setEditable(false);
            txtcashbackpembelian.setEditable(false);
            txtuangkembalian.setEditable(false);
            
            txthargagame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        hitung();
                    }
                }
            });
            
            txtjumlahbayar.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        hitungKembalian();
                    }
                }
            });
        } catch (SQLException e) {
            Logger.getLogger(LanternRite.class.getName()).log(Level.SEVERE, null, e);
        }
    }

     void cari() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("NO");
        model.addColumn("ID PEMBELI");
        model.addColumn("NAMA PEMBELI");
        model.addColumn("STATUS PEMBELI");
        model.addColumn("JUDUL GAME");
        model.addColumn("GENRE GAME");
        model.addColumn("PLATFORM");
        model.addColumn("HARGA GAME");
        model.addColumn("DISKON BELANJA");
        model.addColumn("CASHBACK PEMBELIAN");
        model.addColumn("DISKON TAMBAHAN");
        model.addColumn("PPN");
        model.addColumn("TOTAL PEMBAYARAN");
        model.addColumn("JUMLAH BAYAR");
        model.addColumn("UANG KEMBALIAN");
        try {
            String sql = "SELECT * FROM pembeli WHERE id_pembeli LIKE '%" + txtcari.getText() + "%'";
            Connection conn = config.configDB();
            java.sql.Statement stm = conn.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            
            int no = 1;
            boolean dataFound = false;

            while (res.next()) {
                model.addRow(new Object[]{
                    no++,
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    res.getString(4),
                    res.getString(5),
                    res.getString(6),
                    res.getString(7),
                    res.getString(8),
                    res.getString(9),
                    res.getString(10),
                    res.getString(11),
                    res.getString(12),
                    res.getString(13),
                    res.getString(14),
                    res.getString(15)
                });
                dataFound = true;
            }

            jTable1.setModel(model);

            if (!dataFound) {
                JOptionPane.showMessageDialog(null, "Data tidak ditemukan");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    private void kosong() {
        txtidpembeli.setText(null);
        txtnamapembeli.setText(null);
        cb1.setSelectedIndex(0);
        txtjudulgame.setText(null);
        cb2.setSelectedIndex(0);
        cb3.setSelectedIndex(0);
        txthargagame.setText(null);
        txtdiskonbelanja.setText(null);
        txtcashbackpembelian.setText(null);
        txtdiskontambahan.setText(null);
        txtppn.setText(null);
        txttotalpembayaran.setText(null);
        txtjumlahbayar.setText(null);
        txtuangkembalian.setText(null);
    }
    
    private void load_table() throws SQLException {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("NO");
        model.addColumn("ID PEMBELI");
        model.addColumn("NAMA PEMBELI");
        model.addColumn("STATUS PEMBELI");
        model.addColumn("JUDUL GAME");
        model.addColumn("GENRE GAME");
        model.addColumn("PLATFORM");
        model.addColumn("HARGA GAME");
        model.addColumn("DISKON BELANJA");
        model.addColumn("CASHBACK PEMBELIAN");
        model.addColumn("DISKON TAMBAHAN");
        model.addColumn("PPN");
        model.addColumn("TOTAL PEMBAYARAN");
        model.addColumn("JUMLAH BAYAR");
        model.addColumn("UANG KEMBALIAN");
        model.addColumn("TANGGAL TRANSAKSI");

        int no = 1;
        String sql = "select * from pembeli";
        Connection conn = config.configDB();
        java.sql.Statement stm = conn.createStatement();
        java.sql.ResultSet res = stm.executeQuery(sql);
        while (res.next()) {
           model.addRow(new Object[]{
                    no++,
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    res.getString(4),
                    res.getString(5),
                    res.getString(6),
                    res.getString(7),
                    res.getString(8),
                    res.getString(9),
                    res.getString(10),
                    res.getString(11),
                    res.getString(12),
                    res.getString(13),
                    res.getString(14),
                    res.getString(15)
                });
        }
        jTable1.setModel(model);
    }

    private void hitung() {
        try {
            DecimalFormat df = new DecimalFormat("#.##");

            double hargaGame = Double.parseDouble(txthargagame.getText());
 
            double diskonBelanja = 0;
            double cashbackPembelian = 0;

            String statusPembeli = cb1.getSelectedItem().toString();
            if (statusPembeli.equals("Pelanggan")) {
                diskonBelanja = hargaGame * 0.2; // Diskon 20% untuk Pelanggan dari harga game
            } else if (statusPembeli.equals("Umum")) {
                cashbackPembelian = hargaGame * 0.05; // Cashback 5% untuk Umum dari harga game
            }

            // Diskon tambahan 10% jika pembelian konsumen lebih dari 450,000
            double diskonTambahan = 0;
            if (hargaGame > 450000) {
                diskonTambahan += hargaGame * 0.1; 
            }
            
            // Menghitung ppn, total diskon, dan total oembayaran
            double ppn = hargaGame * 0.02; // Pajak pembelian game 2% dari harga game
            double totalDiskon = diskonBelanja + diskonTambahan;
            double totalPembayaran = hargaGame - totalDiskon - cashbackPembelian + ppn;
            
            //Menampilkan output pada textfield
            txtppn.setText(df.format(ppn));
            txtdiskontambahan.setText(df.format(diskonTambahan));
            txtdiskonbelanja.setText(df.format(diskonBelanja));
            txtcashbackpembelian.setText(df.format(cashbackPembelian));
            txttotalpembayaran.setText(df.format(totalPembayaran));
            
           // Membuat field ini tidak bisa di edit
            txtdiskontambahan.setEditable(false);
            txtppn.setEditable(false);
            txtdiskontambahan.setEditable(false);
            txtdiskonbelanja.setEditable(false);
            txttotalpembayaran.setEditable(false);
            txtcashbackpembelian.setEditable(false);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Masukkan data yang valid!");
        }
    }

    private void hitungKembalian() {
        try {
            DecimalFormat df = new DecimalFormat("#.##");

            double totalPembayaran = Double.parseDouble(txttotalpembayaran.getText());
            double jumlahBayar = Double.parseDouble(txtjumlahbayar.getText());
            double uangKembalian = jumlahBayar - totalPembayaran;

            txtuangkembalian.setText(df.format(uangKembalian));
            
            // Membuat field ini tidak bisa di edit 
            txtuangkembalian.setEditable(false);
        
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Pembayaran ini tidak valid. Pastikan jumlah pembayaran telah diisi dengan benar.");
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtidpembeli = new javax.swing.JTextField();
        txtnamapembeli = new javax.swing.JTextField();
        cb1 = new javax.swing.JComboBox<>();
        cb2 = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        txthargagame = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtjudulgame = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cb3 = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtdiskonbelanja = new javax.swing.JTextField();
        txtcashbackpembelian = new javax.swing.JTextField();
        txttotalpembayaran = new javax.swing.JTextField();
        txtjumlahbayar = new javax.swing.JTextField();
        txtuangkembalian = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtdiskontambahan = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtppn = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        bcari = new javax.swing.JButton();
        txtcari = new javax.swing.JTextField();
        bcetaknota = new javax.swing.JButton();
        txtcetaknota = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        bhapus = new javax.swing.JButton();
        bkeluar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        bbersih = new javax.swing.JButton();
        btambah = new javax.swing.JButton();
        bperbarui = new javax.swing.JButton();
        bcetaklaporan = new javax.swing.JButton();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel2.setOpaque(false);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("ID Pembeli");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Nama Pembeli");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Status Pembeli");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Genre Game");

        cb1.setBackground(new java.awt.Color(204, 204, 204));
        cb1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pelanggan", "Umum" }));

        cb2.setBackground(new java.awt.Color(204, 204, 204));
        cb2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Adventure", "Action ", "Strategy", "Puzzle", "MMORPG", "RPG" }));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Harga Game");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Judul Game");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Platform");

        cb3.setBackground(new java.awt.Color(204, 204, 204));
        cb3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Playstation 5", "Playstation 4", "Playstation 3", "Xbox 360", "Nintendo Switch" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(cb2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 83, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cb3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cb1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtnamapembeli, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                        .addComponent(txtidpembeli)
                                        .addComponent(txtjudulgame)
                                        .addComponent(txthargagame)))))
                        .addGap(18, 18, 18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtidpembeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtnamapembeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtjudulgame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txthargagame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel3.setOpaque(false);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Diskon Belanja (20%)");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Cashback Pembelian (5%)");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Total Pembayaran");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("Jumlah Bayar");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("Uang Kembalian");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("Diskon Tambahan (10%)");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setText("PPN (2%)");

        txtppn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtppnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txttotalpembayaran, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(txtppn, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtdiskontambahan, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtcashbackpembelian, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtdiskonbelanja, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtjumlahbayar)
                    .addComponent(txtuangkembalian))
                .addGap(25, 25, 25))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtdiskonbelanja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcashbackpembelian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtdiskontambahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtppn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txttotalpembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtjumlahbayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtuangkembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel1.setText("PENJUALAN GAME ONLINE");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel2.setText("TOKO \"LANTERN RITE\"");

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setBackground(new java.awt.Color(153, 204, 255));
        jTable1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11", "Title 12", "Title 13", "Title 14"
            }
        ));
        jTable1.setGridColor(new java.awt.Color(0, 0, 0));
        jTable1.setSelectionBackground(new java.awt.Color(255, 102, 102));
        jTable1.setShowGrid(true);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel5.setOpaque(false);

        bcari.setBackground(new java.awt.Color(51, 153, 255));
        bcari.setText("Cari");
        bcari.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bcariMouseClicked(evt);
            }
        });
        bcari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcariActionPerformed(evt);
            }
        });

        txtcari.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        bcetaknota.setBackground(new java.awt.Color(51, 153, 255));
        bcetaknota.setText("Cetak Nota");
        bcetaknota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcetaknotaActionPerformed(evt);
            }
        });

        txtcetaknota.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bcetaknota, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bcari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtcari, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                    .addComponent(txtcetaknota))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bcari)
                    .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bcetaknota)
                    .addComponent(txtcetaknota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setOpaque(false);

        bhapus.setBackground(new java.awt.Color(255, 51, 51));
        bhapus.setText("Hapus Data");
        bhapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bhapusActionPerformed(evt);
            }
        });

        bkeluar.setBackground(new java.awt.Color(255, 51, 51));
        bkeluar.setText("Akhiri Program");
        bkeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bkeluarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bhapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bkeluar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bhapus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bkeluar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setOpaque(false);

        bbersih.setBackground(new java.awt.Color(255, 255, 102));
        bbersih.setText("Bersihkan Form");
        bbersih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bbersihActionPerformed(evt);
            }
        });

        btambah.setBackground(new java.awt.Color(255, 255, 102));
        btambah.setText("Tambah Data");
        btambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btambahActionPerformed(evt);
            }
        });

        bperbarui.setBackground(new java.awt.Color(255, 255, 102));
        bperbarui.setText("Perbarui Data");
        bperbarui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bperbaruiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(bbersih, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btambah, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bperbarui, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bbersih, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btambah)
                    .addComponent(bperbarui))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        bcetaklaporan.setBackground(new java.awt.Color(0, 255, 204));
        bcetaklaporan.setText("Cetak Laporan");
        bcetaklaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcetaklaporanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(23, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(132, 132, 132)
                                .addComponent(bcetaklaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(17, 17, 17))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(185, 185, 185))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(302, 302, 302)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(328, 328, 328)
                        .addComponent(jLabel2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(bcetaklaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void bbersihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bbersihActionPerformed
        // TODO add your handling code here:
        kosong();
    }//GEN-LAST:event_bbersihActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("#");
        
        int row = jTable1.rowAtPoint(evt.getPoint());
        String id_pembeli = jTable1.getValueAt(row, 1).toString();
        txtidpembeli.setText(id_pembeli);

        String nama_pembeli = jTable1.getValueAt(row, 2).toString();
        txtnamapembeli.setText(nama_pembeli);

        String status_pembeli = jTable1.getValueAt(row, 3).toString();
        cb1.setSelectedItem(status_pembeli);

        String judul_game = jTable1.getValueAt(row, 4).toString();
        txtjudulgame.setText(judul_game);

        String genre_game = jTable1.getValueAt(row, 5).toString();
        cb2.setSelectedItem(genre_game);

        String platform = jTable1.getValueAt(row, 6).toString();
        cb3.setSelectedItem(platform);

        String harga_game = jTable1.getValueAt(row, 7).toString();
        txthargagame.setText(harga_game);

        String diskon_belanja = jTable1.getValueAt(row, 8).toString();
        txtdiskonbelanja.setText(diskon_belanja);

        String cashback_pembelian = jTable1.getValueAt(row, 9).toString();
        txtcashbackpembelian.setText(cashback_pembelian);
        
        String diskon_tambahan = jTable1.getValueAt(row, 10).toString();
        txtdiskontambahan.setText(diskon_tambahan);
        
        String ppn = jTable1.getValueAt(row, 11).toString();
        txtppn.setText(ppn);
        
        String total_pembayaran = jTable1.getValueAt(row, 12).toString();
        txttotalpembayaran.setText(total_pembayaran);

        String jumlah_bayar = jTable1.getValueAt(row, 13).toString();
        txtjumlahbayar.setText(jumlah_bayar);

        String uang_kembalian = jTable1.getValueAt(row, 14).toString();
        txtuangkembalian.setText(uang_kembalian);
    }//GEN-LAST:event_jTable1MouseClicked

    private void bkeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bkeluarActionPerformed
        // TODO add your handling code here:
        int response = JOptionPane.showConfirmDialog(null, "Apakah anda ingin keluar dari program?", "Konfirmasi Keluar",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_bkeluarActionPerformed

    private void bcetaklaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcetaklaporanActionPerformed
        // TODO add your handling code here:
        try {
            HashMap<String, Object> parameter = new HashMap<>();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/dbgameonline", "root", "");

            File file = new File("D:\\Netbeans Pemrograman Visual\\PenjualanGameOnline\\src\\Laporan_Data_Konsumen\\Laporan_Transaksi.jasper");
            JasperReport jp = (JasperReport) JRLoader.loadObject(file);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jp, parameter, cn);
            JasperViewer.viewReport(jasperPrint, false);
            JasperViewer.setDefaultLookAndFeelDecorated(true);

        } catch (ClassNotFoundException | SQLException | JRException e) {
            JOptionPane.showMessageDialog(null, "Data Tidak Dapat Ditampilkan: " + e.getMessage());
        }
    }//GEN-LAST:event_bcetaklaporanActionPerformed

    private void btambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btambahActionPerformed
        // TODO add your handling code here:
         try {
            String sql = "INSERT INTO pembeli (id_pembeli, nama_pembeli, status_pembeli, judul_game, genre_game, platform, harga_game, diskon_belanja, cashback_pembelian, diskon_tambahan, ppn, total_pembayaran, jumlah_bayar, uang_kembalian, tanggal_transaksi) VALUES ('"
                + txtidpembeli.getText() + "','"
                + txtnamapembeli.getText() + "','"
                + cb1.getSelectedItem() + "','"
                + txtjudulgame.getText() + "','"
                + cb2.getSelectedItem() + "','"
                + cb3.getSelectedItem() + "','"
                + txthargagame.getText() + "','"
                + txtdiskonbelanja.getText() + "','"
                + txtcashbackpembelian.getText() + "','"
                + txtdiskontambahan.getText() + "','"
                + txtppn.getText() + "','"
                + txttotalpembayaran.getText() + "','"
                + txtjumlahbayar.getText() + "','"
                + txtuangkembalian.getText() + "', CURDATE())";
            Connection conn = config.configDB();
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Penyimpanan Data Telah Berhasil");
            load_table();
            kosong();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btambahActionPerformed

    private void bperbaruiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bperbaruiActionPerformed
        // TODO add your handling code here:
        int response = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Merubah Data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
    
        if (response == JOptionPane.YES_OPTION) {
            try {
                String sql = "update pembeli set " +
                             "id_pembeli='" + txtidpembeli.getText() + "'," +
                             "nama_pembeli='" + txtnamapembeli.getText() + "'," +
                             "status_pembeli='" + cb1.getSelectedItem() + "'," +
                             "judul_game='" + txtjudulgame.getText() + "'," +
                             "genre_game='" + cb2.getSelectedItem() + "'," +
                             "platform='" + cb3.getSelectedItem() + "'," +
                             "harga_game='" + txthargagame.getText() + "'," +
                             "diskon_belanja='" + txtdiskonbelanja.getText() + "'," +                             
                             "cashback_pembelian='" + txtcashbackpembelian.getText() + "'," +
                             "diskon_tambahan='" + txtdiskontambahan.getText() + "'," +
                             "ppn='" + txtppn.getText() + "'," +
                             "total_pembayaran='" + txttotalpembayaran.getText() + "'," +
                             "jumlah_bayar='" + txtjumlahbayar.getText() + "'," +
                             "uang_kembalian='" + txtuangkembalian.getText() + "'" +
                             "where id_pembeli='" + txtidpembeli.getText() + "'";
                java.sql.Connection conn = (Connection) config.configDB();
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.execute();
                JOptionPane.showMessageDialog(null, "Data Telah Berhasil dirubah");
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, "Merubah Data Gagal: " + e.getMessage());
            }
            try {
                load_table();
                kosong();
            } catch (SQLException ex) {
                Logger.getLogger(LanternRite.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Perubahan data dibatalkan");
        }
    }//GEN-LAST:event_bperbaruiActionPerformed

    private void bhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bhapusActionPerformed
        // TODO add your handling code here:
         int confirmed = JOptionPane.showConfirmDialog(null, 
        "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi Hapus",
        JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM pembeli WHERE id_pembeli=?";
                java.sql.Connection conn = (Connection) config.configDB();
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);

                pst.setString(1, txtidpembeli.getText());

                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data telah berhasil dihapus");

                load_table();
                kosong();
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Menghapus data telah dibatalkan");
        }                                     
    }//GEN-LAST:event_bhapusActionPerformed

    private void bcetaknotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcetaknotaActionPerformed
        // TODO add your handling code here:
        try {
            String idPembeli = txtcetaknota.getText();
            HashMap<String, Object> parameter = new HashMap<>();
            parameter.put("id_pembeli", idPembeli);
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/dbgameonline", "root", "");

            File file = new File("D:\\Netbeans Pemrograman Visual\\PenjualanGameOnline\\src\\Laporan_Data_Konsumen\\Nota_Transaksi.jasper");
            JasperReport jp = (JasperReport) JRLoader.loadObject(file);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jp, parameter, cn);
            JasperViewer.viewReport(jasperPrint, false);
            JasperViewer.setDefaultLookAndFeelDecorated(true);

        } catch (ClassNotFoundException | SQLException | JRException e) {
            JOptionPane.showMessageDialog(null, "Dokumen Tidak Ada " + e.getMessage());
        }
    }//GEN-LAST:event_bcetaknotaActionPerformed

    private void bcariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bcariActionPerformed

    private void bcariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bcariMouseClicked
        // TODO add your handling code here:
        cari();
    }//GEN-LAST:event_bcariMouseClicked

    private void txtppnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtppnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtppnActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LanternRite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LanternRite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LanternRite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LanternRite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LanternRite().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bbersih;
    private javax.swing.JButton bcari;
    private javax.swing.JButton bcetaklaporan;
    private javax.swing.JButton bcetaknota;
    private javax.swing.JButton bhapus;
    private javax.swing.JButton bkeluar;
    private javax.swing.JButton bperbarui;
    private javax.swing.JButton btambah;
    private javax.swing.JComboBox<String> cb1;
    private javax.swing.JComboBox<String> cb2;
    private javax.swing.JComboBox<String> cb3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField txtcari;
    private javax.swing.JTextField txtcashbackpembelian;
    private javax.swing.JTextField txtcetaknota;
    private javax.swing.JTextField txtdiskonbelanja;
    private javax.swing.JTextField txtdiskontambahan;
    private javax.swing.JTextField txthargagame;
    private javax.swing.JTextField txtidpembeli;
    private javax.swing.JTextField txtjudulgame;
    private javax.swing.JTextField txtjumlahbayar;
    private javax.swing.JTextField txtnamapembeli;
    private javax.swing.JTextField txtppn;
    private javax.swing.JTextField txttotalpembayaran;
    private javax.swing.JTextField txtuangkembalian;
    // End of variables declaration//GEN-END:variables
}
