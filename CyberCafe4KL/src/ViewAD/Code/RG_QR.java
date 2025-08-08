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
        this.maHD = (maHD != null && !maHD.isEmpty()) ? maHD : TaoMaHoaDonMoi();

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
                JOptionPane.showMessageDialog(this, "Không thể kết nối cơ sở dữ liệu.");
                return false;
            }

            if (maHD == null || maHD.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã hóa đơn chưa được khởi tạo.");
                return false;
            }

            int idOrder = Integer.parseInt(maHD.replace("HĐ", ""));
            int idAccount = layAdminDangTruc();
            if (idAccount == -1) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy admin đang trực.");
                return false;
            }

            // Kiểm tra hóa đơn đã tồn tại chưa
            String checkSQL = "SELECT COUNT(*) FROM OrderFood WHERE IDOrder = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
            checkStmt.setInt(1, idOrder);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Hóa đơn đã tồn tại, không lưu lại.");
                return true;
            }

            // Thêm vào OrderFood
            String insertOrder = "INSERT INTO OrderFood (IDOrder, IDAccount, OrderTime, NguoiTao, GiamGia, ThanhToan) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertOrder);
            ps.setInt(1, idOrder);
            ps.setInt(2, idAccount);
            ps.setString(3, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.setString(4, nguoiTao);
            ps.setInt(5, giamGia);
            ps.setInt(6, thanhToan);
            ps.executeUpdate();

            // Thêm chi tiết hóa đơn (gói nạp)
            String insertDetail = "INSERT INTO OrderFoodDetail (IDOrder, TenSP, SoLuong, DonGia, ThanhTien) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psDetail = conn.prepareStatement(insertDetail);
            psDetail.setInt(1, idOrder);
            psDetail.setString(2, "Gói nạp");
            psDetail.setInt(3, 1);
            psDetail.setInt(4, tongTienSP);
            psDetail.setInt(5, thanhToan);
            psDetail.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("Lỗi lưu hóa đơn: " + e.getMessage());
            return false;
        }
    }
    

    private int layAdminDangTruc() {
        return CN_TaiKhoanDangNhap.getIDTaiKhoan();
    }

    private String TaoMaHoaDonMoi() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ISNULL(MAX(IDOrder), 0) + 1 AS NewID FROM OrderFood";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int newID = rs.getInt("NewID");
                return "HĐ" + newID;
            }
        } catch (Exception e) {
            System.out.println("Lỗi tạo mã hóa đơn: " + e.getMessage());
        }
        return "HĐ10001"; // mặc định nếu có lỗi
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblCTK;
    private javax.swing.JLabel lblND;
    private javax.swing.JLabel lblST;
    private javax.swing.JLabel lblSTK;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlQR;
    // End of variables declaration//GEN-END:variables
}
