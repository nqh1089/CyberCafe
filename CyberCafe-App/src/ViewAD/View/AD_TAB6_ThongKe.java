package ViewAD.View;

import ViewAD.Code.CN_ChanDongX;
import ViewAD.Code.CN_Slidebar;
import ViewAD.Code.CN_TaiKhoanDangNhap;
import ViewAD.Code.TAB6_TongLuotTruyCap;
import ViewAD.Code.TAB6_cbxThang;
import ViewAD.Code.TAB6_tbl;
import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;

public class AD_TAB6_ThongKe extends javax.swing.JFrame {

    public AD_TAB6_ThongKe() {
        initComponents();
        
         CN_ChanDongX.ChanDongX(this);

        CN_Slidebar.SetSlidebar(
                lblDM, lblOrder, lblSP, lblMT, lblHD, lblTKe, lblTKhoan,
                lblDMK, lblDX, lblChat, lblTB, this
        );

        lblID.setText("Xin chào, " + CN_TaiKhoanDangNhap.getTenTaiKhoan());
        setTitle("CyberCafe4KL_Thống kê");

        // Load tháng và set bảng
        TAB6_cbxThang.LoadThangPhatSinh(cbxThangTK);
        TAB6_tbl.SetupAllTables(jTable1, jScrollPane2, jTable3, jScrollPane4, jTable2, jScrollPane3);

        int thangHienTai = LocalDate.now().getMonthValue();
        loadDuLieuBangTheoThang(thangHienTai);
        loadThongKePhu(thangHienTai);

        // Khi chọn tháng mới
        cbxThangTK.addActionListener(e -> {
            int thang = TAB6_cbxThang.getThangFromComboBox(cbxThangTK);
            loadDuLieuBangTheoThang(thang);
            loadThongKePhu(thang);
        });
    }
    
    // === Load dữ liệu chính (3 bảng) ===
    private void loadDuLieuBangTheoThang(int thang) {
        TAB6_tbl.LoadTopSanPham(jTable3, thang);
        TAB6_tbl.LoadTopNhanVien(jTable1, thang);
        TAB6_tbl.LoadTopMay(jTable2, thang);
    }

    // === Load thông kê phụ (ví dụ lượt truy cập) ===
    private void loadThongKePhu(int thang) {
        TAB6_TongLuotTruyCap.Load(jLabel3, thang);
    }

    // === Nếu bạn có hàm tạo danh sách tháng thủ công ===
    public void LoadComboBoxThang() {
        int thangHienTai = LocalDate.now().getMonthValue(); // Lấy tháng hiện tại (1-12)

        String[] danhSachThang = new String[thangHienTai];
        for (int i = 0; i < thangHienTai; i++) {
            danhSachThang[i] = "THÁNG " + (i + 1);
        }

        cbxThangTK = new JComboBox<>(danhSachThang);
        pnlSDM.add(cbxThangTK);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        pnlMain = new javax.swing.JPanel();
        pnlCNNgang = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblTB = new javax.swing.JLabel();
        lblChat = new javax.swing.JLabel();
        pnlMainSDM = new javax.swing.JPanel();
        pnlSDM = new javax.swing.JPanel();
        txtSDM = new javax.swing.JLabel();
        cbxThangTK = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        pnlTSHD3 = new javax.swing.JPanel();
        lblTSHD3 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        pnlTSHD = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblTSHD = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        pnlTSHD1 = new javax.swing.JPanel();
        lblTSHD1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        pnlTSHD2 = new javax.swing.JPanel();
        lblTSHD2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        pnlCN = new javax.swing.JPanel();
        lblID = new javax.swing.JLabel();
        lblDX = new javax.swing.JLabel();
        lblTKhoan = new javax.swing.JLabel();
        lblTKe = new javax.swing.JLabel();
        lblHD = new javax.swing.JLabel();
        lblMT = new javax.swing.JLabel();
        lblSP = new javax.swing.JLabel();
        lblOrder = new javax.swing.JLabel();
        lblDM = new javax.swing.JLabel();
        lblDMK = new javax.swing.JLabel();

        jScrollPane1.setViewportView(jTree1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1500, 780));
        setResizable(false);

        pnlMain.setBackground(new java.awt.Color(51, 51, 255));
        pnlMain.setMaximumSize(new java.awt.Dimension(1500, 780));
        pnlMain.setMinimumSize(new java.awt.Dimension(1500, 780));
        pnlMain.setRequestFocusEnabled(false);
        pnlMain.setVerifyInputWhenFocusTarget(false);

        pnlCNNgang.setBackground(new java.awt.Color(153, 255, 153));

        jPanel3.setBackground(new java.awt.Color(44, 44, 62));
        jPanel3.setMaximumSize(new java.awt.Dimension(1275, 52));
        jPanel3.setMinimumSize(new java.awt.Dimension(1275, 52));
        jPanel3.setPreferredSize(new java.awt.Dimension(1275, 52));
        jPanel3.setRequestFocusEnabled(false);
        jPanel3.setVerifyInputWhenFocusTarget(false);

        lblTB.setForeground(new java.awt.Color(204, 255, 255));
        lblTB.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTB.setText("TB");

        lblChat.setForeground(new java.awt.Color(204, 255, 255));
        lblChat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblChat.setText("Chat");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblChat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTB, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblChat)
                    .addComponent(lblTB))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlCNNgangLayout = new javax.swing.GroupLayout(pnlCNNgang);
        pnlCNNgang.setLayout(pnlCNNgangLayout);
        pnlCNNgangLayout.setHorizontalGroup(
            pnlCNNgangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCNNgangLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1279, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        pnlCNNgangLayout.setVerticalGroup(
            pnlCNNgangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCNNgangLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        pnlMainSDM.setBackground(new java.awt.Color(153, 255, 153));

        pnlSDM.setBackground(new java.awt.Color(30, 30, 47));

        txtSDM.setBackground(new java.awt.Color(255, 255, 255));
        txtSDM.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        txtSDM.setForeground(new java.awt.Color(255, 255, 255));
        txtSDM.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtSDM.setText("THỐNG KÊ");

        cbxThangTK.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tháng 6" }));
        cbxThangTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxThangTKActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(102, 255, 255));

        pnlTSHD3.setBackground(new java.awt.Color(255, 255, 255));

        lblTSHD3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTSHD3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTSHD3.setText("TOP SẢN PHẨM BÁN CHẠY:");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Tên SP", "Tổng số lượng", "Doanh thu"
            }
        ));
        jScrollPane4.setViewportView(jTable3);

        javax.swing.GroupLayout pnlTSHD3Layout = new javax.swing.GroupLayout(pnlTSHD3);
        pnlTSHD3.setLayout(pnlTSHD3Layout);
        pnlTSHD3Layout.setHorizontalGroup(
            pnlTSHD3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 793, Short.MAX_VALUE)
            .addGroup(pnlTSHD3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTSHD3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlTSHD3Layout.setVerticalGroup(
            pnlTSHD3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTSHD3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblTSHD3)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlTSHD3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlTSHD3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        jPanel2.setBackground(new java.awt.Color(102, 255, 255));

        pnlTSHD.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("100");

        lblTSHD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTSHD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTSHD.setText("TỔNG SỐ LƯỢT TRUY CẬP:");

        javax.swing.GroupLayout pnlTSHDLayout = new javax.swing.GroupLayout(pnlTSHD);
        pnlTSHD.setLayout(pnlTSHDLayout);
        pnlTSHDLayout.setHorizontalGroup(
            pnlTSHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTSHDLayout.createSequentialGroup()
                .addComponent(lblTSHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlTSHDLayout.setVerticalGroup(
            pnlTSHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTSHDLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblTSHD)
                .addGap(27, 27, 27)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(160, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlTSHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlTSHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        jPanel4.setBackground(new java.awt.Color(102, 255, 255));

        pnlTSHD1.setBackground(new java.awt.Color(255, 255, 255));

        lblTSHD1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTSHD1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTSHD1.setText("TOP NHÂN VIÊN CÓ DOANH THU CAO NHẤT:");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Tên", "Tổng số HD", "Doanh thu"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout pnlTSHD1Layout = new javax.swing.GroupLayout(pnlTSHD1);
        pnlTSHD1.setLayout(pnlTSHD1Layout);
        pnlTSHD1Layout.setHorizontalGroup(
            pnlTSHD1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
            .addGroup(pnlTSHD1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTSHD1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlTSHD1Layout.setVerticalGroup(
            pnlTSHD1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTSHD1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblTSHD1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlTSHD1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlTSHD1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        jPanel5.setBackground(new java.awt.Color(102, 255, 255));

        pnlTSHD2.setBackground(new java.awt.Color(255, 255, 255));

        lblTSHD2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTSHD2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTSHD2.setText("TOP MÁY ĐƯỢC CHỌN NHIỀU NHẤT:");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Số lượt bật", "Giờ hoạt động", "Doanh thu"
            }
        ));
        jScrollPane3.setViewportView(jTable2);

        javax.swing.GroupLayout pnlTSHD2Layout = new javax.swing.GroupLayout(pnlTSHD2);
        pnlTSHD2.setLayout(pnlTSHD2Layout);
        pnlTSHD2Layout.setHorizontalGroup(
            pnlTSHD2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
            .addGroup(pnlTSHD2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTSHD2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlTSHD2Layout.setVerticalGroup(
            pnlTSHD2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTSHD2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblTSHD2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlTSHD2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlTSHD2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout pnlSDMLayout = new javax.swing.GroupLayout(pnlSDM);
        pnlSDM.setLayout(pnlSDMLayout);
        pnlSDMLayout.setHorizontalGroup(
            pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSDMLayout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addGroup(pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlSDMLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlSDMLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(96, 96, 96))
            .addGroup(pnlSDMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSDM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSDMLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbxThangTK, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(577, 577, 577))
        );
        pnlSDMLayout.setVerticalGroup(
            pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSDMLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(txtSDM)
                .addGap(18, 18, 18)
                .addComponent(cbxThangTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(85, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlMainSDMLayout = new javax.swing.GroupLayout(pnlMainSDM);
        pnlMainSDM.setLayout(pnlMainSDMLayout);
        pnlMainSDMLayout.setHorizontalGroup(
            pnlMainSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainSDMLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlSDM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        pnlMainSDMLayout.setVerticalGroup(
            pnlMainSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainSDMLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlSDM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        pnlCN.setBackground(new java.awt.Color(44, 44, 62));
        pnlCN.setMaximumSize(new java.awt.Dimension(219, 780));
        pnlCN.setMinimumSize(new java.awt.Dimension(219, 780));
        pnlCN.setRequestFocusEnabled(false);

        lblID.setBackground(new java.awt.Color(255, 255, 255));
        lblID.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblID.setForeground(new java.awt.Color(204, 255, 255));
        lblID.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblID.setText("Xin chào, nqh1089");

        lblDX.setBackground(new java.awt.Color(204, 255, 255));
        lblDX.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lblDX.setForeground(new java.awt.Color(204, 255, 255));
        lblDX.setText("Đăng xuất");

        lblTKhoan.setBackground(new java.awt.Color(255, 255, 255));
        lblTKhoan.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblTKhoan.setForeground(new java.awt.Color(204, 255, 255));
        lblTKhoan.setText("Tài khoản");

        lblTKe.setBackground(new java.awt.Color(255, 255, 255));
        lblTKe.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblTKe.setForeground(new java.awt.Color(204, 255, 255));
        lblTKe.setText("Thống kê");

        lblHD.setBackground(new java.awt.Color(255, 255, 255));
        lblHD.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblHD.setForeground(new java.awt.Color(204, 255, 255));
        lblHD.setText("Hóa đơn");

        lblMT.setBackground(new java.awt.Color(255, 255, 255));
        lblMT.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblMT.setForeground(new java.awt.Color(204, 255, 255));
        lblMT.setText("Máy tính");

        lblSP.setBackground(new java.awt.Color(255, 255, 255));
        lblSP.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblSP.setForeground(new java.awt.Color(204, 255, 255));
        lblSP.setText("Sản phẩm");

        lblOrder.setBackground(new java.awt.Color(255, 255, 255));
        lblOrder.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblOrder.setForeground(new java.awt.Color(204, 255, 255));
        lblOrder.setText("Order");

        lblDM.setBackground(new java.awt.Color(255, 255, 255));
        lblDM.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblDM.setForeground(new java.awt.Color(204, 255, 255));
        lblDM.setText("Đặt máy");

        lblDMK.setBackground(new java.awt.Color(255, 255, 255));
        lblDMK.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lblDMK.setForeground(new java.awt.Color(204, 255, 255));
        lblDMK.setText("Đổi MK");

        javax.swing.GroupLayout pnlCNLayout = new javax.swing.GroupLayout(pnlCN);
        pnlCN.setLayout(pnlCNLayout);
        pnlCNLayout.setHorizontalGroup(
            pnlCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCNLayout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addComponent(lblID, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(pnlCNLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(pnlCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDMK)
                    .addComponent(lblDX)
                    .addComponent(lblOrder)
                    .addComponent(lblDM)
                    .addComponent(lblSP)
                    .addComponent(lblHD)
                    .addComponent(lblTKe)
                    .addComponent(lblMT)
                    .addComponent(lblTKhoan))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCNLayout.setVerticalGroup(
            pnlCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCNLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(lblID)
                .addGap(84, 84, 84)
                .addComponent(lblDM)
                .addGap(40, 40, 40)
                .addComponent(lblOrder)
                .addGap(40, 40, 40)
                .addComponent(lblSP)
                .addGap(40, 40, 40)
                .addComponent(lblMT)
                .addGap(40, 40, 40)
                .addComponent(lblHD)
                .addGap(40, 40, 40)
                .addComponent(lblTKe)
                .addGap(40, 40, 40)
                .addComponent(lblTKhoan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDMK)
                .addGap(18, 18, 18)
                .addComponent(lblDX)
                .addGap(43, 43, 43))
        );

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                .addComponent(pnlCN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlCNNgang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlMainSDM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addComponent(pnlCNNgang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(pnlMainSDM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
            .addComponent(pnlCN, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxThangTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxThangTKActionPerformed
        LoadComboBoxThang();
    }//GEN-LAST:event_cbxThangTKActionPerformed
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AD_TAB6_ThongKe().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbxThangTK;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTree jTree1;
    private javax.swing.JLabel lblChat;
    private javax.swing.JLabel lblDM;
    private javax.swing.JLabel lblDMK;
    private javax.swing.JLabel lblDX;
    private javax.swing.JLabel lblHD;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblMT;
    private javax.swing.JLabel lblOrder;
    private javax.swing.JLabel lblSP;
    private javax.swing.JLabel lblTB;
    private javax.swing.JLabel lblTKe;
    private javax.swing.JLabel lblTKhoan;
    private javax.swing.JLabel lblTSHD;
    private javax.swing.JLabel lblTSHD1;
    private javax.swing.JLabel lblTSHD2;
    private javax.swing.JLabel lblTSHD3;
    private javax.swing.JPanel pnlCN;
    private javax.swing.JPanel pnlCNNgang;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMainSDM;
    private javax.swing.JPanel pnlSDM;
    private javax.swing.JPanel pnlTSHD;
    private javax.swing.JPanel pnlTSHD1;
    private javax.swing.JPanel pnlTSHD2;
    private javax.swing.JPanel pnlTSHD3;
    private javax.swing.JLabel txtSDM;
    // End of variables declaration//GEN-END:variables
}
