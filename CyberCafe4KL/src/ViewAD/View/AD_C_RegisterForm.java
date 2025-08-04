package ViewAD.View;

import ViewAD.Code.RG_QR;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;

public class AD_C_RegisterForm extends javax.swing.JFrame {

    public AD_C_RegisterForm() {
        initComponents();
//        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null); // Set hiển giữa màn hình
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        pnlMain2 = new javax.swing.JPanel();
        pnlRegister = new javax.swing.JPanel();
        NameAccount = new javax.swing.JLabel();
        PhoneNumber = new javax.swing.JLabel();
        Gender = new javax.swing.JLabel();
        CCCD = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtNameAccount = new javax.swing.JTextField();
        txtPhoneNumber = new javax.swing.JTextField();
        txtCCCD = new javax.swing.JTextField();
        btnRegister = new javax.swing.JButton();
        cbxGender = new javax.swing.JComboBox<>();
        cbxBalance = new javax.swing.JComboBox<>();
        Balance = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlMain.setBackground(new java.awt.Color(51, 51, 255));

        pnlMain2.setBackground(new java.awt.Color(153, 255, 153));

        pnlRegister.setBackground(new java.awt.Color(30, 30, 47));

        NameAccount.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        NameAccount.setForeground(new java.awt.Color(255, 255, 255));
        NameAccount.setText("NameAccount:");

        PhoneNumber.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        PhoneNumber.setForeground(new java.awt.Color(255, 255, 255));
        PhoneNumber.setText("PhoneNumber:");

        Gender.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        Gender.setForeground(new java.awt.Color(255, 255, 255));
        Gender.setText("Gender:");

        CCCD.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        CCCD.setForeground(new java.awt.Color(255, 255, 255));
        CCCD.setText("CCCD:");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("REGISTER");

        txtNameAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameAccountActionPerformed(evt);
            }
        });

        txtPhoneNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhoneNumberActionPerformed(evt);
            }
        });

        txtCCCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCCCDActionPerformed(evt);
            }
        });

        btnRegister.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnRegister.setText("Register");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        cbxGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));

        cbxBalance.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "20.000", "50.000", "100.000", "200.000", "500.000" }));

        Balance.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        Balance.setForeground(new java.awt.Color(255, 255, 255));
        Balance.setText("Nạp tiền:");

        javax.swing.GroupLayout pnlRegisterLayout = new javax.swing.GroupLayout(pnlRegister);
        pnlRegister.setLayout(pnlRegisterLayout);
        pnlRegisterLayout.setHorizontalGroup(
            pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRegisterLayout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cbxGender, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(CCCD)
                        .addComponent(NameAccount)
                        .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCCCD)
                            .addComponent(txtPhoneNumber)
                            .addComponent(txtNameAccount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                            .addComponent(Gender)
                            .addComponent(btnRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Balance))
                        .addComponent(PhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbxBalance, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(73, 73, 73))
        );
        pnlRegisterLayout.setVerticalGroup(
            pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegisterLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(NameAccount)
                .addGap(15, 15, 15)
                .addComponent(txtNameAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(PhoneNumber)
                .addGap(15, 15, 15)
                .addComponent(txtPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(CCCD)
                .addGap(15, 15, 15)
                .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Gender)
                .addGap(15, 15, 15)
                .addComponent(cbxGender, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Balance)
                .addGap(15, 15, 15)
                .addComponent(cbxBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlMain2Layout = new javax.swing.GroupLayout(pnlMain2);
        pnlMain2.setLayout(pnlMain2Layout);
        pnlMain2Layout.setHorizontalGroup(
            pnlMain2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMain2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        pnlMain2Layout.setVerticalGroup(
            pnlMain2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMain2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlMain2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlMain2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNameAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameAccountActionPerformed

    }//GEN-LAST:event_txtNameAccountActionPerformed

    private void txtPhoneNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneNumberActionPerformed

    private void txtCCCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCCCDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCCCDActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        String name = txtNameAccount.getText().trim();
        String phone = txtPhoneNumber.getText().trim();
        String cccd = txtCCCD.getText().trim();
        String gender = cbxGender.getSelectedItem().toString();

        String balanceStr = cbxBalance.getSelectedItem().toString().replace(".", "").replace(",", "");
        double balance = Double.parseDouble(balanceStr);

        if (name.isEmpty() || phone.isEmpty() || cccd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        if (!cccd.matches("\\d{12}")) {
            JOptionPane.showMessageDialog(this, "CCCD phải gồm đúng 12 chữ số.");
            return;
        }

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải gồm đúng 10 chữ số.");
            return;
        }

        // Lấy tên admin đang đăng nhập từ CN_TaiKhoanDangNhap
        String adminName = ViewAD.Code.CN_TaiKhoanDangNhap.getTenTaiKhoan();

        int idAccountAdmin = -1;  // Khai báo biến ở đây!

        try (Connection conn = Controller.DBConnection.getConnection()) {
            String sql = "SELECT IDAccount FROM Account WHERE NameAccount = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, adminName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idAccountAdmin = rs.getInt("IDAccount");
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi lấy ID Admin: " + e.getMessage());
            return;
        }

        if (idAccountAdmin == -1) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy ID của tài khoản Admin đang đăng nhập.");
            return;
        }

        RG_QR qrForm = new RG_QR(
                null,
                name,
                (int) balance,
                0,
                (int) balance,
                phone,
                gender,
                cccd,
                idAccountAdmin,
                this
        );
        qrForm.setVisible(true);


    }//GEN-LAST:event_btnRegisterActionPerformed
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AD_C_RegisterForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Balance;
    private javax.swing.JLabel CCCD;
    private javax.swing.JLabel Gender;
    private javax.swing.JLabel NameAccount;
    private javax.swing.JLabel PhoneNumber;
    private javax.swing.JButton btnRegister;
    private javax.swing.JComboBox<String> cbxBalance;
    private javax.swing.JComboBox<String> cbxGender;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMain2;
    private javax.swing.JPanel pnlRegister;
    private javax.swing.JTextField txtCCCD;
    private javax.swing.JTextField txtNameAccount;
    private javax.swing.JTextField txtPhoneNumber;
    // End of variables declaration//GEN-END:variables
}
