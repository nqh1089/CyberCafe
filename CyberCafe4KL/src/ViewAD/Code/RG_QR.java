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
            String noiDung = "THANHTOAN " + maHD;
            String qrURL = CN_GenQR.TaoLinkQR(thanhToan, noiDung);

            if (qrURL == null || qrURL.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không thể tạo link QR.");
                return;
            }

            ImageIcon icon = new ImageIcon(new java.net.URL(qrURL));
            Image img = icon.getImage().getScaledInstance(pnlQR.getWidth(), pnlQR.getHeight(), Image.SCALE_SMOOTH);

            JLabel lblQR = new JLabel(new ImageIcon(img));
            pnlQR.removeAll();
            pnlQR.setLayout(new BorderLayout());
            pnlQR.add(lblQR, BorderLayout.CENTER);
            pnlQR.revalidate();
            pnlQR.repaint();

            // Hiển thị thông tin dưới QR
            lblSTK.setText("STK: 0503081089");
            lblCTK.setText("CHỦ TÀI KHOẢN: NGUYEN QUANG HUY");
            lblST.setText("SỐ TIỀN: " + (int) thanhToan + " VND");
            lblND.setText("NỘI DUNG: " + noiDung);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hiển thị mã QR: " + e.getMessage());
        }
    }

    private boolean LuuHoaDonVaoDB() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                return false;
            }

            // Lấy ID hóa đơn mới
            int newIDOrder = 1;
            String sqlMax = "SELECT ISNULL(MAX(IDOrder), 0) + 1 FROM OrderFood";
            try (PreparedStatement psMax = conn.prepareStatement(sqlMax); ResultSet rsMax = psMax.executeQuery()) {
                if (rsMax.next()) {
                    newIDOrder = rsMax.getInt(1);
                    this.maHD = String.valueOf(newIDOrder); // gán lại để dùng tiếp
                }
            }

            // Thêm vào OrderFood
            String insertOrder = "INSERT INTO OrderFood (IDOrder, IDAccount, OrderTime) VALUES (?, ?, ?)";
            try (PreparedStatement ps1 = conn.prepareStatement(insertOrder)) {
                ps1.setInt(1, newIDOrder);
                ps1.setInt(2, idAccountAdmin);
                ps1.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                ps1.executeUpdate();
            }

            // Tìm IDFood của gói nạp (dựa vào số tiền thanh toán)
            int idFoodNap = -1;
            String sqlFood = "SELECT TOP 1 IDFood FROM FoodDrink WHERE Category = N'Gói nạp' AND Price = ?";
            try (PreparedStatement psFood = conn.prepareStatement(sqlFood)) {
                psFood.setDouble(1, thanhToan);
                try (ResultSet rsFood = psFood.executeQuery()) {
                    if (rsFood.next()) {
                        idFoodNap = rsFood.getInt("IDFood");
                    }
                }
            }

            // Nếu tìm thấy gói nạp -> thêm vào OrderDetail
            if (idFoodNap != -1) {
                String insertDetail = "INSERT INTO OrderDetail (IDOrder, IDFood, Quantity, TotalPrice) VALUES (?, ?, ?, ?)";
                try (PreparedStatement psDetail = conn.prepareStatement(insertDetail)) {
                    psDetail.setInt(1, newIDOrder);
                    psDetail.setInt(2, idFoodNap);
                    psDetail.setInt(3, 1);
                    psDetail.setDouble(4, thanhToan);
                    psDetail.executeUpdate();
                }
            } else {
                System.out.println("Không tìm thấy gói nạp tương ứng với số tiền: " + thanhToan);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Lỗi lưu hóa đơn: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        btnDone = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        pnlQR = new javax.swing.JPanel();
        lblSTK = new javax.swing.JLabel();
        lblCTK = new javax.swing.JLabel();
        lblST = new javax.swing.JLabel();
        lblND = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(380, 571));

        pnlMain.setBackground(new java.awt.Color(30, 30, 47));
        pnlMain.setMaximumSize(new java.awt.Dimension(380, 571));
        pnlMain.setMinimumSize(new java.awt.Dimension(380, 571));
        pnlMain.setPreferredSize(new java.awt.Dimension(380, 571));
        pnlMain.setRequestFocusEnabled(false);
        pnlMain.setVerifyInputWhenFocusTarget(false);
        pnlMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnDone.setText("Hoàn tất");
        btnDone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoneActionPerformed(evt);
            }
        });
        pnlMain.add(btnDone, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 480, 110, -1));

        jLabel1.setBackground(new java.awt.Color(153, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUÉT MÃ ĐỂ THANH TOÁN");
        pnlMain.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 380, -1));

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

        pnlMain.add(pnlQR, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 300, 270));

        lblSTK.setBackground(new java.awt.Color(153, 255, 255));
        lblSTK.setForeground(new java.awt.Color(102, 255, 255));
        lblSTK.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSTK.setText("STK: ");
        pnlMain.add(lblSTK, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 300, 20));

        lblCTK.setBackground(new java.awt.Color(153, 255, 255));
        lblCTK.setForeground(new java.awt.Color(102, 255, 255));
        lblCTK.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCTK.setText("CHỦ TÀI KHOẢN");
        pnlMain.add(lblCTK, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, 300, 20));

        lblST.setBackground(new java.awt.Color(153, 255, 255));
        lblST.setForeground(new java.awt.Color(102, 255, 255));
        lblST.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblST.setText("SỐ TIỀN:");
        pnlMain.add(lblST, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 300, 20));

        lblND.setBackground(new java.awt.Color(153, 255, 255));
        lblND.setForeground(new java.awt.Color(102, 255, 255));
        lblND.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblND.setText("NỘI DUNG:");
        pnlMain.add(lblND, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 420, 300, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoneActionPerformed
        // 1. Lưu hóa đơn trước
    if (!LuuHoaDonVaoDB()) {
        JOptionPane.showMessageDialog(this, "Lưu hóa đơn thất bại.");
        return;
    }

    // 2. Tạo tài khoản khách hàng mới
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
        cs.close();

        if (result != 0) {
            JOptionPane.showMessageDialog(this, "Tạo tài khoản thất bại. Mã lỗi: " + result);
            return;
        }

        // 3. Cập nhật số dư cho tài khoản vừa tạo
        String sqlUpdate = "UPDATE Account SET Balance = ? WHERE NameAccount = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
            ps.setDouble(1, thanhToan);
            ps.setString(2, nguoiTao);
            ps.executeUpdate();
        }

        // 4. Lấy mật khẩu để hiển thị
        String password = "";
        String sqlPW = "SELECT PWAccount FROM Account WHERE NameAccount = ?";
        try (PreparedStatement psPW = conn.prepareStatement(sqlPW)) {
            psPW.setString(1, nguoiTao);
            try (ResultSet rsPW = psPW.executeQuery()) {
                if (rsPW.next()) {
                    password = rsPW.getString("PWAccount");
                }
            }
        }

        // 5. Thông báo thành công
        JOptionPane.showMessageDialog(this,
                "Đăng ký & nạp tiền thành công!\nMật khẩu: " + password);

        // 6. Đóng form
        this.dispose();
        if (parentForm != null) {
            parentForm.dispose();
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi xử lý: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnDoneActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDone;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblCTK;
    private javax.swing.JLabel lblND;
    private javax.swing.JLabel lblST;
    private javax.swing.JLabel lblSTK;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlQR;
    // End of variables declaration//GEN-END:variables
}
