package ViewAD.Code;

import Controller.DBConnection;
import ViewAD.View.AD_TAB2_Order;
import ViewC.Code.CN_BienToanCuc;
import java.awt.BorderLayout;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.sql.ResultSet;

public class TAB2_QR extends javax.swing.JFrame {

    private String maHD;
    private String nguoiTao;
    private int tongTienSP;
    private int giamGia;
    private int thanhToan;
    private JTable tblChiTiet;
    private AD_TAB2_Order formOrder;

    private String tenTaiKhoanNap;
    private int soTienNap;

    // Constructor dành cho Admin (k có Gói nạp)
    public TAB2_QR(String maHD, String nguoiTao, int tongTienSP, int giamGia, int thanhToan,
            JTable tblChiTiet, AD_TAB2_Order formOrder) {
        initComponents();
        this.maHD = maHD;
        this.nguoiTao = nguoiTao;
        this.tongTienSP = tongTienSP;
        this.giamGia = giamGia;
        this.thanhToan = thanhToan;
        this.tblChiTiet = tblChiTiet;
        this.formOrder = formOrder;

        this.setResizable(false);
        AnhQR();
    }

    // Constructor dành cho Admin (có Gói nạp)
    public TAB2_QR(String maHD, String nguoiTao, int tongTienSP, int giamGia, int thanhToan,
            JTable tblChiTiet, AD_TAB2_Order formOrder,
            String tenTaiKhoanNap, int soTienNap) {

        this(maHD, nguoiTao, tongTienSP, giamGia, thanhToan, tblChiTiet, formOrder);

        // Gán thêm thông tin Gói nạp
        this.tenTaiKhoanNap = tenTaiKhoanNap;
        this.soTienNap = soTienNap;
    }

    // Constructor dành cho Client (không có Gói nạp)
    public TAB2_QR(String maHD, String nguoiTao, int tongTienSP, int giamGia, int thanhToan,
            JTable tblChiTiet, ViewC.View.C2_Order formClient) {

        this(maHD, nguoiTao, tongTienSP, giamGia, thanhToan, tblChiTiet,
                new AD_TAB2_Order() {
            @Override
            public void resetSauKhiThanhToan() {
                formClient.resetSauKhiThanhToan(); // Gọi lại form client
            }
        });
    }

    // Constructor dành cho Client (có Gói nạp)
    public TAB2_QR(String maHD, String nguoiTao, int tongTienSP, int giamGia, int thanhToan,
            JTable tblChiTiet, ViewC.View.C2_Order formClient,
            String tenTaiKhoanNap, int soTienNap) {

        this(maHD, nguoiTao, tongTienSP, giamGia, thanhToan, tblChiTiet,
                new AD_TAB2_Order() {
            @Override
            public void resetSauKhiThanhToan() {
                formClient.resetSauKhiThanhToan(); // Gọi lại form client
            }
        });

        this.tenTaiKhoanNap = tenTaiKhoanNap;
        this.soTienNap = soTienNap;
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

        // Lấy IDAccount từ tên tài khoản
        int idAccount;

        // Nếu gọi từ Client (nguoiTao rỗng), lấy Admin đang online
        if (nguoiTao == null || nguoiTao.trim().isEmpty()) {
            idAccount = layAdminDangTruc();
            if (idAccount == -1) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy admin đang trực.");
                return false;
            }
        } else {
            idAccount = layIDAccount(nguoiTao);
            if (idAccount == -1) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản admin: " + nguoiTao);
                return false;
            }
        }

        // Lấy tên máy nếu là phía Client
        String tenMay = "";
        if (nguoiTao == null || nguoiTao.trim().isEmpty()) {
            try {
                tenMay = ViewC.Code.CN_BienToanCuc.getTenMayClient();
                if (tenMay == null || tenMay.isEmpty()) {
                    ViewC.Code.CN_LayTenMayTheoIP.ganThongTinMay();
                    tenMay = ViewC.Code.CN_BienToanCuc.getTenMayClient();
                }
            } catch (Exception ex) {
                tenMay = "Máy khách";
            }
        }

        // 1. Lưu OrderFood (nếu có tên máy thì thêm vào Note)
        String insertOrder;
        PreparedStatement ps1;
        if (!tenMay.isEmpty()) {
            insertOrder = "INSERT INTO OrderFood (IDOrder, IDAccount, OrderTime, Note) VALUES (?, ?, ?, ?)";
            ps1 = conn.prepareStatement(insertOrder);
            ps1.setInt(1, Integer.parseInt(maHD.replace("HĐ", "")));
            ps1.setInt(2, idAccount);
            ps1.setString(3, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps1.setString(4, tenMay);
        } else {
            insertOrder = "INSERT INTO OrderFood (IDOrder, IDAccount, OrderTime) VALUES (?, ?, ?)";
            ps1 = conn.prepareStatement(insertOrder);
            ps1.setInt(1, Integer.parseInt(maHD.replace("HĐ", "")));
            ps1.setInt(2, idAccount);
            ps1.setString(3, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        ps1.executeUpdate();

        // 2. Lưu OrderDetail
        String insertDetail = "INSERT INTO OrderDetail (IDOrder, IDFood, Quantity, TotalPrice) VALUES (?, ?, ?, ?)";
        PreparedStatement ps2 = conn.prepareStatement(insertDetail);

        for (int i = 0; i < tblChiTiet.getRowCount(); i++) {
            int idFood = Integer.parseInt(tblChiTiet.getValueAt(i, 0).toString()); // cột 0: ID sản phẩm
            int quantity = Integer.parseInt(tblChiTiet.getValueAt(i, 3).toString().replace(".", "")); // cột 3: số lượng
            int total = Integer.parseInt(tblChiTiet.getValueAt(i, 4).toString().replace(".", "")); // cột 4: thành tiền

            ps2.setInt(1, Integer.parseInt(maHD.replace("HĐ", "")));
            ps2.setInt(2, idFood);
            ps2.setInt(3, quantity);
            ps2.setInt(4, total);
            ps2.addBatch();
        }

        ps2.executeBatch();
        return true;
    } catch (Exception e) {
        System.out.println("Lỗi lưu hóa đơn: " + e.getMessage());
        return false;
    }
}

    private int layIDAccount(String tenTK) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT IDAccount FROM Account WHERE NameAccount = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tenTK);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("IDAccount");
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy IDAccount: " + e.getMessage());
        }
        return -1;
    }

    private int layAdminDangTruc() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT TOP 1 IDAccount FROM Account "
                    + "WHERE (RoleAccount = 'ADMIN' OR RoleAccount = 'BOSS') "
                    + "AND OnlineStatus = 1 AND AccountStatus = 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("IDAccount");
                System.out.println("[Lấy admin trực] IDAccount: " + id);
                return id;
            } else {
                System.out.println("[Lấy admin trực] Không tìm thấy ai đang trực.");
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy admin đang trực: " + e.getMessage());
        }
        return -1;
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
            // 🔔 In ra console để theo dõi máy vừa gửi Order (giúp Admin đọc được từ DB)
            try {
                String tenMay = ViewC.Code.CN_BienToanCuc.getTenMayClient();
                if (tenMay == null || tenMay.isEmpty()) {
                    // Nếu chưa gán biến toàn cục thì lấy lại từ IP
                    ViewC.Code.CN_LayTenMayTheoIP.ganThongTinMay();
                    tenMay = ViewC.Code.CN_BienToanCuc.getTenMayClient();
                }

                String gio = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
                System.out.println("[Thông báo] Đã gửi thông báo order từ " + tenMay + " lúc " + gio);
            } catch (Exception ex) {
                System.out.println("[Thông báo] Không thể xác định tên máy client.");
            }

            // Nếu có yêu cầu nạp tiền thì thực hiện sau khi lưu thành công
            if (tenTaiKhoanNap != null && !tenTaiKhoanNap.trim().isEmpty() && soTienNap > 0) {
                try (Connection conn = DBConnection.getConnection()) {
                    String sqlUpdate = "UPDATE Account SET Balance = Balance + ? WHERE NameAccount = ?";
                    PreparedStatement ps = conn.prepareStatement(sqlUpdate);
                    ps.setInt(1, soTienNap);
                    ps.setString(2, tenTaiKhoanNap.trim());
                    int rows = ps.executeUpdate();
                    ps.close();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Đã nạp " + soTienNap + " VNĐ vào tài khoản " + tenTaiKhoanNap);
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi khi nạp tiền!");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi nạp tiền: " + e.getMessage());
                }
            }

            JOptionPane.showMessageDialog(this, "Đã lưu hóa đơn thành công.");
            if (formOrder != null) {
                formOrder.resetSauKhiThanhToan();
            }
            this.dispose();
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
