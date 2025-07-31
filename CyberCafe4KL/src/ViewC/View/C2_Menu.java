package ViewC.View;

import Controller.DBConnection;
import ViewAD.View.AD_ChangePW;
import ViewC.Code.C2_ChiPhiGio;
import ViewC.Code.CN_BienToanCuc;
import ViewC.Code.C2_SetImage;
import ViewC.Code.C2_ClientIcons;
import ViewC.Code.C2_ThoiGianConLai;
import ViewC.Code.C2_ThoiGianSuDung;
import ViewC.Code.CN_LogoutMay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;

public class C2_Menu extends JFrame {

    public static C2_Menu instance;
    public Timer timer;

    public C2_Menu() {
        initComponents();
        instance = this;
        LoadThongTinMay();
        C2_ClientIcons.LoadIcons(pnlCN);
        C2_SetImage.SetPanelBackgroundTLQL(pnlTLQL);
        addEventHandlers();

        this.setResizable(false); // Không cho phóng to
        chanDongX(); // Không cho tắt giao diện khi đang đăng nhập

        // 60 giây load lại tt 1 lần
        Timer timer = new Timer(60_000, e -> CapNhatThongTinDongBo());
        timer.start();
    }

    private void CapNhatThongTinDongBo() {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                // 1. Trạng thái và giờ bắt đầu
                String sqlUsage = "SELECT TOP 1 StartTime FROM ComputerUsage "
                        + "WHERE IDComputer = ? AND IDAccount = ? AND EndTime IS NULL "
                        + "ORDER BY StartTime DESC";
                PreparedStatement psUsage = conn.prepareStatement(sqlUsage);
                psUsage.setInt(1, CN_BienToanCuc.IDComputer);
                psUsage.setInt(2, CN_BienToanCuc.IDAccount);
                ResultSet rsUsage = psUsage.executeQuery();

                if (rsUsage.next()) {
                    Timestamp startTime = rsUsage.getTimestamp("StartTime");
                    lblTrangThai.setText("Đang sử dụng");
                    lblGioBatDau.setText(new SimpleDateFormat("HH:mm:ss").format(startTime));

                    long usedMin = Duration.between(startTime.toInstant(), Instant.now()).toMinutes();
                    lblThoiGianSD.setText(usedMin + " phút");

                    double chiPhiGio = C2_ChiPhiGio.getChiPhiGio(CN_BienToanCuc.IDComputer, usedMin);
                    lblChiPhiGio.setText(formatTien(chiPhiGio) + " đ");
                } else {
                    lblTrangThai.setText("Chưa sử dụng");
                    lblGioBatDau.setText("--:--");
                }

                rsUsage.close();
                psUsage.close();

                // 2. Số dư khả dụng
                String sqlBal = "SELECT Balance FROM Account WHERE IDAccount = ?";
                PreparedStatement psBal = conn.prepareStatement(sqlBal);
                psBal.setInt(1, CN_BienToanCuc.IDAccount);
                ResultSet rsBal = psBal.executeQuery();
                if (rsBal.next()) {
                    lblSoDu.setText(formatTien(rsBal.getDouble("Balance")) + " đ");
                }
                rsBal.close();
                psBal.close();

                // 3. Thời gian sử dụng
                long usedMin = C2_ThoiGianSuDung.getThoiGianSuDungPhut(CN_BienToanCuc.IDComputer, CN_BienToanCuc.IDAccount);
                lblThoiGianSD.setText(usedMin + " phút");

                // 4. Thời gian còn lại
                long conLaiMin = C2_ThoiGianConLai.getThoiGianConLaiPhut(CN_BienToanCuc.IDAccount, CN_BienToanCuc.IDComputer);
                lblThoiGianConLai.setText(conLaiMin + " phút");
                System.out.println("[AutoUpdate] Đã cập nhật thông tin máy");
                conn.close();
            } else {
                System.out.println("Không thể kết nối DB khi CapNhatThongTinDongBo");
            }
        } catch (Exception e) {
            System.out.println("Lỗi CapNhatThongTinDongBo: " + e.getMessage());
        }
    }
    //    private void CapNhatThongTinDongBo() {
    //        long usedMin = C2_ThoiGianSuDung.getThoiGianSuDungPhut(CN_BienToanCuc.IDComputer, CN_BienToanCuc.IDAccount);
    //        lblThoiGianSD.setText(usedMin + " phút");
    //
    //        long conLaiMin = C2_ThoiGianConLai.getThoiGianConLaiPhut(CN_BienToanCuc.IDAccount, CN_BienToanCuc.IDComputer);
    //        lblThoiGianConLai.setText(conLaiMin + " phút");
    //
    //        double chiPhiGio = C2_ChiPhiGio.getChiPhiGio(CN_BienToanCuc.IDComputer, usedMin);
    //        lblChiPhiGio.setText(formatTien(chiPhiGio) + " đ");
    //
    //        System.out.println("[AutoUpdate] Đã cập nhật thông tin máy");
    //    }

    private void LoadThongTinMay() {
        lblTaiKhoan.setText(
                CN_BienToanCuc.TenTaiKhoan.equals("") ? "--" : CN_BienToanCuc.TenTaiKhoan
        );

        String tenMay = CN_BienToanCuc.TenMay.equals("") ? "MÁY " : CN_BienToanCuc.TenMay;
        lblTenMay.setText(tenMay);

        lblTrangThai.setText("--");
        lblGioBatDau.setText("--:--");
        lblThoiGianSD.setText("-- phút");
        lblThoiGianConLai.setText("-- phút");
        lblChiPhiGio.setText("0 đ");
        lblChiPhiDV.setText("0 đ");
        lblSoDu.setText("-- đ"); // Mặc định

        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                String sql = "SELECT TOP 1 StartTime FROM ComputerUsage "
                        + "WHERE IDComputer = ? AND IDAccount = ? AND EndTime IS NULL "
                        + "ORDER BY StartTime DESC";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, CN_BienToanCuc.IDComputer);
                ps.setInt(2, CN_BienToanCuc.IDAccount);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Timestamp startTime = rs.getTimestamp("StartTime");

                    lblTrangThai.setText("Đang sử dụng");
                    lblGioBatDau.setText(new SimpleDateFormat("HH:mm:ss").format(startTime));

                    long usedMin = Duration.between(startTime.toInstant(), Instant.now()).toMinutes();
                    lblThoiGianSD.setText(usedMin + " phút");

                    int cost = (int) usedMin * 200;
                    lblChiPhiGio.setText(formatTien(cost) + " đ");
                }

                rs.close();
                ps.close();

                // Lấy số dư
                String sqlBal = "SELECT Balance FROM Account WHERE IDAccount = ?";
                PreparedStatement psBal = conn.prepareStatement(sqlBal);
                psBal.setInt(1, CN_BienToanCuc.IDAccount);
                ResultSet rsBal = psBal.executeQuery();
                if (rsBal.next()) {
                    lblSoDu.setText(formatTien(rsBal.getDouble("Balance")) + " đ");
                }

                // Thời gian sử dụng
                long usedMin = C2_ThoiGianSuDung.getThoiGianSuDungPhut(CN_BienToanCuc.IDComputer, CN_BienToanCuc.IDAccount);
                lblThoiGianSD.setText(usedMin + " phút");

                // Thời gian còn lại
                long conLaiMin = C2_ThoiGianConLai.getThoiGianConLaiPhut(CN_BienToanCuc.IDAccount, CN_BienToanCuc.IDComputer);
                lblThoiGianConLai.setText(conLaiMin + " phút");

                // Chi phí giờ chơi
                double chiPhiGio = C2_ChiPhiGio.getChiPhiGio(CN_BienToanCuc.IDComputer, usedMin);
                lblChiPhiGio.setText(formatTien(chiPhiGio) + " đ");
                rsBal.close();
                psBal.close();

                conn.close();
            } else {
                System.out.println("Không thể kết nối DB khi LoadThongTinMay");
            }
        } catch (Exception e) {
            System.out.println("Lỗi LoadThongTinMay: " + e.getMessage());
        }
    }

    // Hàm xử lý sự kiện click từ tất cả JLabel trong pnlCN
    private void addEventHandlers() {
        addLabelMouseListeners(pnlCN);
    }

    private void addLabelMouseListeners(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel lbl && lbl.getName() != null) {
                lbl.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        xuLyChucNang(lbl.getName());
                    }
                });
            } else if (comp instanceof Container inner) {
                addLabelMouseListeners(inner);
            }
        }
    }

    private void xuLyChucNang(String tenNut) {
        switch (tenNut) {
            case "chat":
                C2_Chat.showChat();
                break;
            case "order":
                JOptionPane.showMessageDialog(this, "Chức năng Order đang phát triển");
                break;
            case "dangxuat":
                CN_LogoutMay.logoutMay();
                this.dispose();
                break;
            case "matkhau":
                new AD_ChangePW().setVisible(true);
                break;
            case "khoamay":
                new C1_GiaoDienTreo().setVisible(true); // Mở form khóa máy
                this.dispose(); // Đóng form menu
                break;
            default:
                JOptionPane.showMessageDialog(this, "Không xác định chức năng: " + tenNut);
        }
    }

    private String formatTien(double soTien) {
        return String.format("%,.0f", soTien).replace(',', ' ');
    }

    private void chanDongX() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (CN_BienToanCuc.IDAccount != -1) {
                    JOptionPane.showMessageDialog(null, "Không thể tắt khi đang sử dụng");
                } else {
                    dispose(); // nếu chưa login thì vẫn cho tắt
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlSDM1 = new javax.swing.JPanel();
        pnlMainTTM = new javax.swing.JPanel();
        pnlTTM = new javax.swing.JPanel();
        lblTenMay = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblSoDu = new javax.swing.JLabel();
        lblTrangThai = new javax.swing.JLabel();
        lblGioBatDau = new javax.swing.JLabel();
        lblThoiGianSD = new javax.swing.JLabel();
        lblThoiGianConLai = new javax.swing.JLabel();
        lblChiPhiGio = new javax.swing.JLabel();
        lblChiPhiDV = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblTaiKhoan = new javax.swing.JLabel();
        pnlMainCN = new javax.swing.JPanel();
        pnlCN = new javax.swing.JPanel();
        pnlTLQL = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(30, 30, 47));

        pnlSDM1.setBackground(new java.awt.Color(51, 51, 255));
        pnlSDM1.setForeground(new java.awt.Color(255, 255, 255));
        pnlSDM1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        pnlMainTTM.setBackground(new java.awt.Color(153, 255, 153));

        pnlTTM.setBackground(new java.awt.Color(30, 30, 47));

        lblTenMay.setBackground(new java.awt.Color(255, 255, 255));
        lblTenMay.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblTenMay.setForeground(new java.awt.Color(255, 255, 255));
        lblTenMay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTenMay.setText("[MÁY 01]");

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Thời gian sử dụng:");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Thời gian bắt đầu:");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Chi phí giờ chơi:");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Trạng thái:");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Thời gian còn lại:");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Chi phí dịch vụ:");

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Số dư khả dụng:");

        lblSoDu.setBackground(new java.awt.Color(255, 255, 255));
        lblSoDu.setForeground(new java.awt.Color(255, 255, 255));
        lblSoDu.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSoDu.setText("jLabel7");

        lblTrangThai.setForeground(new java.awt.Color(255, 255, 255));
        lblTrangThai.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTrangThai.setText("jLabel8");

        lblGioBatDau.setForeground(new java.awt.Color(255, 255, 255));
        lblGioBatDau.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGioBatDau.setText("jLabel9");

        lblThoiGianSD.setForeground(new java.awt.Color(255, 255, 255));
        lblThoiGianSD.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblThoiGianSD.setText("jLabel10");

        lblThoiGianConLai.setForeground(new java.awt.Color(255, 255, 255));
        lblThoiGianConLai.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblThoiGianConLai.setText("jLabel11");

        lblChiPhiGio.setForeground(new java.awt.Color(255, 255, 255));
        lblChiPhiGio.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChiPhiGio.setText("jLabel12");

        lblChiPhiDV.setBackground(new java.awt.Color(255, 255, 255));
        lblChiPhiDV.setForeground(new java.awt.Color(255, 255, 255));
        lblChiPhiDV.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChiPhiDV.setText("jLabel13");

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Tài khoản:");

        lblTaiKhoan.setBackground(new java.awt.Color(255, 255, 255));
        lblTaiKhoan.setForeground(new java.awt.Color(255, 255, 255));
        lblTaiKhoan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTaiKhoan.setText("jLabel7");

        javax.swing.GroupLayout pnlTTMLayout = new javax.swing.GroupLayout(pnlTTM);
        pnlTTM.setLayout(pnlTTMLayout);
        pnlTTMLayout.setHorizontalGroup(
            pnlTTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTTMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTenMay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pnlTTMLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(pnlTTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlTTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSoDu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTrangThai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblGioBatDau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblThoiGianSD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblThoiGianConLai, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblChiPhiDV, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblChiPhiGio, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTaiKhoan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        pnlTTMLayout.setVerticalGroup(
            pnlTTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTTMLayout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addComponent(lblTenMay)
                .addGap(26, 26, 26)
                .addGroup(pnlTTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lblTaiKhoan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlTTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTTMLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlTTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(lblChiPhiDV)))
                    .addGroup(pnlTTMLayout.createSequentialGroup()
                        .addComponent(lblSoDu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTrangThai)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblGioBatDau)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblThoiGianSD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblThoiGianConLai)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblChiPhiGio)))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlMainTTMLayout = new javax.swing.GroupLayout(pnlMainTTM);
        pnlMainTTM.setLayout(pnlMainTTMLayout);
        pnlMainTTMLayout.setHorizontalGroup(
            pnlMainTTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainTTMLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlTTM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        pnlMainTTMLayout.setVerticalGroup(
            pnlMainTTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainTTMLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlTTM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        pnlMainCN.setBackground(new java.awt.Color(153, 255, 153));

        pnlCN.setBackground(new java.awt.Color(30, 30, 47));

        javax.swing.GroupLayout pnlCNLayout = new javax.swing.GroupLayout(pnlCN);
        pnlCN.setLayout(pnlCNLayout);
        pnlCNLayout.setHorizontalGroup(
            pnlCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 322, Short.MAX_VALUE)
        );
        pnlCNLayout.setVerticalGroup(
            pnlCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlMainCNLayout = new javax.swing.GroupLayout(pnlMainCN);
        pnlMainCN.setLayout(pnlMainCNLayout);
        pnlMainCNLayout.setHorizontalGroup(
            pnlMainCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainCNLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlCN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        pnlMainCNLayout.setVerticalGroup(
            pnlMainCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainCNLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlCN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout pnlSDM1Layout = new javax.swing.GroupLayout(pnlSDM1);
        pnlSDM1.setLayout(pnlSDM1Layout);
        pnlSDM1Layout.setHorizontalGroup(
            pnlSDM1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSDM1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(pnlSDM1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlMainCN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlMainTTM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );
        pnlSDM1Layout.setVerticalGroup(
            pnlSDM1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSDM1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlMainTTM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2)
                .addComponent(pnlMainCN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        pnlTLQL.setBackground(new java.awt.Color(204, 255, 204));
        pnlTLQL.setMaximumSize(new java.awt.Dimension(330, 150));
        pnlTLQL.setMinimumSize(new java.awt.Dimension(330, 150));
        pnlTLQL.setPreferredSize(new java.awt.Dimension(330, 150));

        javax.swing.GroupLayout pnlTLQLLayout = new javax.swing.GroupLayout(pnlTLQL);
        pnlTLQL.setLayout(pnlTLQLLayout);
        pnlTLQLLayout.setHorizontalGroup(
            pnlTLQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlTLQLLayout.setVerticalGroup(
            pnlTLQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlSDM1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(pnlTLQL, javax.swing.GroupLayout.PREFERRED_SIZE, 328, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlSDM1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2)
                .addComponent(pnlTLQL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new C2_Menu().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lblChiPhiDV;
    private javax.swing.JLabel lblChiPhiGio;
    private javax.swing.JLabel lblGioBatDau;
    private javax.swing.JLabel lblSoDu;
    private javax.swing.JLabel lblTaiKhoan;
    private javax.swing.JLabel lblTenMay;
    private javax.swing.JLabel lblThoiGianConLai;
    private javax.swing.JLabel lblThoiGianSD;
    private javax.swing.JLabel lblTrangThai;
    private javax.swing.JPanel pnlCN;
    private javax.swing.JPanel pnlMainCN;
    private javax.swing.JPanel pnlMainTTM;
    private javax.swing.JPanel pnlSDM1;
    private javax.swing.JPanel pnlTLQL;
    private javax.swing.JPanel pnlTTM;
    // End of variables declaration//GEN-END:variables
}
