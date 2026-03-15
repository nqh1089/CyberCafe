package ViewAD.Code;

import ViewAD.View.AD_TAB7_QLNV;
import Controller.DBConnection;
import java.sql.*;
import javax.swing.*;

public class TAB7_ChiTietNV extends javax.swing.JFrame {

    private boolean cheDoSua = false;
    private String[] duLieuNV;
    private AD_TAB7_QLNV formQLNV;

    public TAB7_ChiTietNV(AD_TAB7_QLNV formQLNV) {
        initComponents();
        this.formQLNV = formQLNV;

        lblTieuDe.setText("THÊM NHÂN VIÊN");
        btnThemSua.setText("Thêm nhân viên");

        // Chỉ cho phép chọn ADMIN
        cbxRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"ADMIN"}));
        cbxRole.setSelectedIndex(0);
        cbxRole.setEnabled(false);

        txtID.setEnabled(false); // Ẩn ID vì ở đây là id đã tự sinh
        this.setLocationRelativeTo(null); // Set hiển giữa màn hình
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public TAB7_ChiTietNV(String[] duLieu, AD_TAB7_QLNV formQLNV) {
        initComponents();
        cheDoSua = true;
        this.duLieuNV = duLieu;
        this.formQLNV = formQLNV;

        lblTieuDe.setText("SỬA THÔNG TIN");
        btnThemSua.setText("Cập nhật thông tin");

        // Gán dữ liệu vào các trường
        txtID.setText(duLieu[0]);
        txtName.setText(duLieu[1]);

        // Gán role: cho phép chọn lại nếu cần, còn không thì chỉ gán và disable
        cbxRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"BOSS", "ADMIN"}));
        cbxRole.setSelectedItem(duLieu[2]);

        txtCCCD.setText(duLieu[3]);
        txtPhone.setText(duLieu[4]);
        txtEmail.setText(duLieu[5]);
        cbxGender.setSelectedItem(duLieu[6]);
        cbxStatus.setSelectedItem(duLieu[7]);

        // Không cho sửa các trường cố định
        txtID.setEnabled(false);
        txtName.setEnabled(false);
        cbxRole.setEnabled(false);
        txtCCCD.setEnabled(false);
        cbxGender.setEnabled(false);

        // Cho phép sửa các trường cập nhật
        txtPhone.setEnabled(true);
        txtEmail.setEnabled(true);

        // Nếu là tài khoản BOSS thì không cho sửa trạng thái
        if (duLieu[2].equalsIgnoreCase("BOSS")) {
            cbxStatus.setEnabled(false);
        } else {
            cbxStatus.setEnabled(true);
        }

        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        PhoneNumber = new javax.swing.JLabel();
        CCCD1 = new javax.swing.JLabel();
        Gender = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        CCCD = new javax.swing.JLabel();
        NameAccount1 = new javax.swing.JLabel();
        lblTieuDe = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        NameAccount2 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        txtID = new javax.swing.JTextField();
        txtCCCD = new javax.swing.JTextField();
        btnThemSua = new javax.swing.JButton();
        cbxGender = new javax.swing.JComboBox<>();
        NameAccount = new javax.swing.JLabel();
        cbxRole = new javax.swing.JComboBox<>();
        cbxStatus = new javax.swing.JComboBox<>();
        Gender1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 255));

        jPanel2.setBackground(new java.awt.Color(153, 255, 153));

        jPanel3.setBackground(new java.awt.Color(30, 30, 47));

        PhoneNumber.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        PhoneNumber.setForeground(new java.awt.Color(255, 255, 255));
        PhoneNumber.setText("PhoneNumber:");

        CCCD1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        CCCD1.setForeground(new java.awt.Color(255, 255, 255));
        CCCD1.setText("Email");

        Gender.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        Gender.setForeground(new java.awt.Color(255, 255, 255));
        Gender.setText("Gender:");

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        CCCD.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        CCCD.setForeground(new java.awt.Color(255, 255, 255));
        CCCD.setText("CCCD:");

        NameAccount1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        NameAccount1.setForeground(new java.awt.Color(255, 255, 255));
        NameAccount1.setText("Role:");

        lblTieuDe.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblTieuDe.setForeground(new java.awt.Color(255, 255, 255));
        lblTieuDe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTieuDe.setText("THÔNG TIN NHÂN VIÊN");

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        NameAccount2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        NameAccount2.setForeground(new java.awt.Color(255, 255, 255));
        NameAccount2.setText("IDAccount:");

        txtPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhoneActionPerformed(evt);
            }
        });

        txtID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDActionPerformed(evt);
            }
        });

        txtCCCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCCCDActionPerformed(evt);
            }
        });

        btnThemSua.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnThemSua.setText("THEM/SUA");
        btnThemSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSuaActionPerformed(evt);
            }
        });

        cbxGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));

        NameAccount.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        NameAccount.setForeground(new java.awt.Color(255, 255, 255));
        NameAccount.setText("NameAccount:");

        cbxRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Boss", "Admin" }));

        cbxStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang hoạt động", "Ngừng hoạt động" }));

        Gender1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        Gender1.setForeground(new java.awt.Color(255, 255, 255));
        Gender1.setText("AccountStatus:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Gender1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(NameAccount2)
                            .addComponent(txtID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(NameAccount)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(NameAccount1)
                            .addComponent(cbxRole, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(79, 79, 79))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cbxStatus, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(CCCD)
                                    .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CCCD1)
                                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPhone)
                                    .addComponent(cbxGender, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(Gender)
                                            .addComponent(PhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addGap(80, 80, 80))))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(175, 175, 175)
                .addComponent(btnThemSua, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(lblTieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lblTieuDe, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(NameAccount2)
                        .addGap(15, 15, 15)
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(NameAccount)
                        .addGap(14, 14, 14)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxRole, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(NameAccount1)
                        .addGap(47, 47, 47)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CCCD)
                    .addComponent(PhoneNumber))
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(CCCD1)
                        .addGap(15, 15, 15)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(Gender)
                            .addGap(49, 49, 49))
                        .addComponent(cbxGender, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(Gender1)
                        .addGap(49, 49, 49))
                    .addComponent(cbxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(67, 67, 67)
                .addComponent(btnThemSua, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(79, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed

    }//GEN-LAST:event_txtNameActionPerformed

    private void txtPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneActionPerformed

    }//GEN-LAST:event_txtPhoneActionPerformed

    private void txtIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDActionPerformed

    private void txtCCCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCCCDActionPerformed

    }//GEN-LAST:event_txtCCCDActionPerformed

    private void btnThemSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSuaActionPerformed
        String id = txtID.getText().trim();
        String name = txtName.getText().trim();
        String role = cbxRole.getSelectedItem().toString().toUpperCase();
        String cccd = txtCCCD.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String gender = cbxGender.getSelectedItem().toString();
        String statusText = cbxStatus.getSelectedItem().toString();
        boolean status = statusText.equals("Đang hoạt động");

        if (name.isEmpty() || cccd.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            if (!cheDoSua) {
                // Tự sinh IDAccount từ DB
                String idSql = "SELECT ISNULL(MAX(IDAccount), 0) + 1 FROM Account";
                Statement idStmt = conn.createStatement();
                ResultSet rsID = idStmt.executeQuery(idSql);
                if (rsID.next()) {
                    id = String.valueOf(rsID.getInt(1));
                    txtID.setText(id); // Hiển thị ID mới lên form
                }

                // Kiểm tra trùng CCCD hoặc SĐT
                String checkSql = "SELECT COUNT(*) FROM Account WHERE CCCD = ? OR PhoneNumber = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, cccd);
                checkStmt.setString(2, phone);
                ResultSet rsCheck = checkStmt.executeQuery();
                rsCheck.next();
                if (rsCheck.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "CCCD hoặc SĐT đã tồn tại");
                    return;
                }

                // Thêm nhân viên mới với mật khẩu mặc định là "123"
                String sql = "INSERT INTO Account (IDAccount, NameAccount, PWAccount, RoleAccount, CCCD, PhoneNumber, Email, Gender, AccountStatus) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, id);
                ps.setString(2, name);
                ps.setString(3, "123"); // Mật khẩu mặc định
                ps.setString(4, role);
                ps.setString(5, cccd);
                ps.setString(6, phone);
                ps.setString(7, email);
                ps.setString(8, gender);
                ps.setBoolean(9, status);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công. Mật khẩu mặc định là: 123");

            } else {
                // Cập nhật nhân viên
                String sql = "UPDATE Account SET PhoneNumber = ?, Email = ?, AccountStatus = ? WHERE IDAccount = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, phone);
                ps.setString(2, email);
                ps.setBoolean(3, status);
                ps.setString(4, id);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công");
            }

            formQLNV.CapNhatTable();
            this.dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu thông tin nhân viên");
        }
    }//GEN-LAST:event_btnThemSuaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CCCD;
    private javax.swing.JLabel CCCD1;
    private javax.swing.JLabel Gender;
    private javax.swing.JLabel Gender1;
    private javax.swing.JLabel NameAccount;
    private javax.swing.JLabel NameAccount1;
    private javax.swing.JLabel NameAccount2;
    private javax.swing.JLabel PhoneNumber;
    private javax.swing.JButton btnThemSua;
    private javax.swing.JComboBox<String> cbxGender;
    private javax.swing.JComboBox<String> cbxRole;
    private javax.swing.JComboBox<String> cbxStatus;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblTieuDe;
    private javax.swing.JTextField txtCCCD;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPhone;
    // End of variables declaration//GEN-END:variables
}
