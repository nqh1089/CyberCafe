package ViewAD.Code;

import Controller.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RG_QR extends javax.swing.JFrame {

    private String maHD;
    private String nguoiTao;  // Tài khoản KHÁCH vừa đăng ký
    private int tongTienSP;
    private int giamGia;
    private int thanhToan;

    private String phone;
    private String gender;
    private String cccd;

    private int idAccountAdmin;  // ID tài khoản admin đang đăng nhập

    private javax.swing.JFrame parentForm; // Form cha (Register)

    public RG_QR(String maHD, String nguoiTao, int tongTienSP, int giamGia, int thanhToan,
            String phone, String gender, String cccd,
            int idAccountAdmin,
            javax.swing.JFrame parentForm) {

        initComponents();
        this.maHD = maHD;
        this.nguoiTao = nguoiTao;
        this.tongTienSP = tongTienSP;
        this.giamGia = giamGia;
        this.thanhToan = thanhToan;

        this.phone = phone;
        this.gender = gender;
        this.cccd = cccd;

        this.idAccountAdmin = idAccountAdmin;
        this.parentForm = parentForm;

        this.setResizable(false);
        AnhQR();
    }

    private void AnhQR() {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Assets/Products/QR.jpg"));
            Image img = icon.getImage().getScaledInstance(pnlQR.getWidth(), pnlQR.getHeight(), Image.SCALE_SMOOTH);
            JLabel lblQR = new JLabel(new ImageIcon(img));
            pnlQR.removeAll();
            pnlQR.setLayout(new BorderLayout());
            pnlQR.add(lblQR, BorderLayout.CENTER);
            pnlQR.revalidate();
            pnlQR.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể hiển thị mã QR.");
        }
    }

    private boolean LuuHoaDonVaoDB() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                return false;
            }

            int idAccount = idAccountAdmin;

            int newIDOrder = 1;
            String sqlMax = "SELECT ISNULL(MAX(IDOrder), 0) + 1 FROM OrderFood";
            PreparedStatement psMax = conn.prepareStatement(sqlMax);
            ResultSet rsMax = psMax.executeQuery();
            if (rsMax.next()) {
                newIDOrder = rsMax.getInt(1);
            }
            rsMax.close();
            psMax.close();

            String insertOrder = "INSERT INTO OrderFood (IDOrder, IDAccount, OrderTime) VALUES (?, ?, ?)";
            PreparedStatement ps1 = conn.prepareStatement(insertOrder);
            ps1.setInt(1, newIDOrder);
            ps1.setInt(2, idAccount);
            ps1.setString(3, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps1.executeUpdate();
            ps1.close();

            return true;

        } catch (Exception e) {
            System.out.println("Lỗi lưu hóa đơn: " + e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        btnDone = new javax.swing.JButton();
        pnlQR = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlMain.setBackground(new java.awt.Color(30, 30, 47));
        pnlMain.setMaximumSize(new java.awt.Dimension(400, 400));
        pnlMain.setMinimumSize(new java.awt.Dimension(400, 400));
        pnlMain.setPreferredSize(new java.awt.Dimension(400, 400));
        pnlMain.setRequestFocusEnabled(false);
        pnlMain.setVerifyInputWhenFocusTarget(false);
        pnlMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnDone.setText("Hoàn tất");
        btnDone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoneActionPerformed(evt);
            }
        });
        pnlMain.add(btnDone, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 340, 110, -1));

        pnlQR.setMaximumSize(new java.awt.Dimension(340, 340));
        pnlQR.setMinimumSize(new java.awt.Dimension(340, 340));

        javax.swing.GroupLayout pnlQRLayout = new javax.swing.GroupLayout(pnlQR);
        pnlQR.setLayout(pnlQRLayout);
        pnlQRLayout.setHorizontalGroup(
            pnlQRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );
        pnlQRLayout.setVerticalGroup(
            pnlQRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );

        pnlMain.add(pnlQR, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, 290));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoneActionPerformed
        if (LuuHoaDonVaoDB()) {
            try (Connection conn = DBConnection.getConnection()) {
                if (conn == null) {
                    JOptionPane.showMessageDialog(this, "Không thể kết nối DB.");
                    return;
                }

                CallableStatement cs = conn.prepareCall("{? = call SP_DangKyNhanhClient(?, ?, ?, ?)}");
                cs.registerOutParameter(1, java.sql.Types.INTEGER);
                cs.setString(2, nguoiTao);
                cs.setString(3, phone);
                cs.setString(4, gender);
                cs.setString(5, cccd);
                cs.execute();
                int result = cs.getInt(1);

                if (result != 0) {
                    JOptionPane.showMessageDialog(this, "Tạo tài khoản thất bại. Mã lỗi: " + result);
                    return;
                }

                String sqlUpdate = "UPDATE Account SET Balance = ? WHERE NameAccount = ?";
                PreparedStatement ps = conn.prepareStatement(sqlUpdate);
                ps.setDouble(1, thanhToan);
                ps.setString(2, nguoiTao);
                ps.executeUpdate();
                ps.close();

                String password = "";
                String sqlPW = "SELECT PWAccount FROM Account WHERE NameAccount = ?";
                PreparedStatement psPW = conn.prepareStatement(sqlPW);
                psPW.setString(1, nguoiTao);
                ResultSet rsPW = psPW.executeQuery();
                if (rsPW.next()) {
                    password = rsPW.getString("PWAccount");
                }
                rsPW.close();
                psPW.close();

                JOptionPane.showMessageDialog(this,
                        "Đăng ký & nạp tiền thành công!\nMật khẩu: " + password);

                this.dispose();            // đóng form QR
                if (parentForm != null) {  // đóng luôn form Register cha
                    parentForm.dispose();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi xử lý: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Lưu hóa đơn thất bại.");
        }
    }//GEN-LAST:event_btnDoneActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDone;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlQR;
    // End of variables declaration//GEN-END:variables
}
