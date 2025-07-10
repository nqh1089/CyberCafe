package ViewAD.Code;

import Controller.DBConnection;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TAB3_ThemSP extends javax.swing.JFrame {

    private boolean cheDoSua = false;
    private String maSanPhamDangSua = null;
    private String tenAnhDaChon = "";
    private final String thuMucAnh = "E:/SU25/BL2/CyberCafe4KL/CyberCafe4KL/src/Assets/Products/";

    public TAB3_ThemSP() {
        initComponents();
        txtTenCN.setText("THÊM SẢN PHẨM");
        cbxTrangThai.setEnabled(true);
        this.setResizable(false); // Không cho phóng to
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); //Chỉ đóng form thêm hoặc sửa, k đông form chính

    }

    public TAB3_ThemSP(String maSP) {
        initComponents();
        cheDoSua = true;
        maSanPhamDangSua = maSP;

        txtTenCN.setText("SỬA SẢN PHẨM");
        txtTenSP.setEnabled(false);
        cbxLoaiSP.setEnabled(false);
        cbxTrangThai.setVisible(true);

        LoadThongTinSanPham(maSP);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    }

    private void LoadThongTinSanPham(String maSP) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM FoodDrink WHERE IDFood = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, maSP);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtTenSP.setText(rs.getString("NameFood"));
                cbxLoaiSP.setSelectedItem(rs.getString("Category"));
                txtGiaSP.setText(String.valueOf((int) rs.getDouble("Price")));
                cbxTrangThai.setSelectedItem(rs.getInt("Available") == 1 ? "Đang bán" : "Ngừng bán");
                tenAnhDaChon = rs.getString("ImageFood");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtTenSP = new javax.swing.JTextField();
        txtGiaSP = new javax.swing.JTextField();
        cbxLoaiSP = new javax.swing.JComboBox<>();
        cbxTrangThai = new javax.swing.JComboBox<>();
        txtTenCN = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnChonAnh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 255));

        jPanel2.setBackground(new java.awt.Color(153, 255, 153));

        jPanel3.setBackground(new java.awt.Color(30, 30, 47));

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Giá sản phẩm:");

        cbxLoaiSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Đồ ăn", "Đồ uống", "Gói nạp" }));
        cbxLoaiSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxLoaiSPActionPerformed(evt);
            }
        });

        cbxTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang bán", "Ngừng bán" }));
        cbxTrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTrangThaiActionPerformed(evt);
            }
        });

        txtTenCN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTenCN.setForeground(new java.awt.Color(255, 255, 255));
        txtTenCN.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTenCN.setText("THÊM SẢN PHẨM");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Tên sản phẩm:");

        btnThem.setText("Hoàn tất");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Ảnh sản phẩm:");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Loại sản phẩm:");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Trạng thái:");

        btnChonAnh.setText("Chọn ảnh SP");
        btnChonAnh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonAnhActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTenCN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 43, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(41, 41, 41)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtGiaSP)
                    .addComponent(txtTenSP)
                    .addComponent(cbxLoaiSP, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbxTrangThai, 0, 140, Short.MAX_VALUE)
                    .addComponent(btnChonAnh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(37, 37, 37))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(txtTenCN)
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbxLoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtGiaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cbxTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(btnChonAnh))
                .addGap(48, 48, 48)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(75, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxLoaiSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxLoaiSPActionPerformed

    }//GEN-LAST:event_cbxLoaiSPActionPerformed

    private void cbxTrangThaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTrangThaiActionPerformed

    }//GEN-LAST:event_cbxTrangThaiActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        String ten = txtTenSP.getText().trim();
        String loai = cbxLoaiSP.getSelectedItem().toString();
        String giaText = txtGiaSP.getText().trim().replaceAll("\\s+", ""); // Xóa khoảng trắng

        if (ten.isEmpty() || giaText.isEmpty() || tenAnhDaChon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin và chọn ảnh sản phẩm");
            return;
        }

        // Kiểm tra file ảnh có tồn tại không
        File anh = new File(thuMucAnh + tenAnhDaChon);
        if (!anh.exists()) {
            JOptionPane.showMessageDialog(this, "Ảnh sản phẩm đã chọn không còn tồn tại");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            double gia = Double.parseDouble(giaText);

            if (cheDoSua) {
                int trangThai = cbxTrangThai.getSelectedItem().equals("Đang bán") ? 1 : 0;
                String sql = "UPDATE FoodDrink SET ImageFood = ?, Price = ?, Available = ? WHERE IDFood = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, tenAnhDaChon);
                stmt.setDouble(2, gia);
                stmt.setInt(3, trangThai);
                stmt.setString(4, maSanPhamDangSua);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công");
            } else {
                String sql = "INSERT INTO FoodDrink (NameFood, ImageFood, Price, Category, Available) VALUES (?, ?, ?, ?, 1)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, ten);
                stmt.setString(2, tenAnhDaChon);
                stmt.setDouble(3, gia);
                stmt.setString(4, loai);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm mới");
            }

            this.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá sản phẩm không hợp lệ");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnChonAnhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonAnhActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn ảnh sản phẩm");
        chooser.setFileFilter(new FileNameExtensionFilter("Hình ảnh", "jpg", "jpeg", "png", "gif"));

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File fileDuocChon = chooser.getSelectedFile();

            // Kiểm tra ảnh tồn tại
            if (!fileDuocChon.exists()) {
                JOptionPane.showMessageDialog(this, "Ảnh không tồn tại hoặc đã bị xóa");
                return;
            }

            tenAnhDaChon = fileDuocChon.getName();
            File dich = new File(thuMucAnh + tenAnhDaChon);

            try {
                Files.copy(fileDuocChon.toPath(), dich.toPath(), StandardCopyOption.REPLACE_EXISTING);
                JOptionPane.showMessageDialog(this, "Đã chọn ảnh: " + tenAnhDaChon);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Không thể copy ảnh: " + e.getMessage());
                tenAnhDaChon = "";
            }
        }
    }//GEN-LAST:event_btnChonAnhActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChonAnh;
    private javax.swing.JButton btnThem;
    private javax.swing.JComboBox<String> cbxLoaiSP;
    private javax.swing.JComboBox<String> cbxTrangThai;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtGiaSP;
    private javax.swing.JLabel txtTenCN;
    private javax.swing.JTextField txtTenSP;
    // End of variables declaration//GEN-END:variables
}
