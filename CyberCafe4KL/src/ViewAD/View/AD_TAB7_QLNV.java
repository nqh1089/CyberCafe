package ViewAD.View;

import Controller.DBConnection;
import ViewAD.Code.CN_SetupTable;
import ViewAD.Code.CN_TaiKhoanDangNhap;
import ViewAD.Code.CN_btnSlideBar;
import ViewAD.Code.TAB1_Slidebar;
import ViewAD.Code.TAB7_ChiTietNV;
import ViewAD.Code.TAB7_NhanVien;
import ViewAD.Code.TAB7_cbxTT;
import java.sql.Connection;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class AD_TAB7_QLNV extends javax.swing.JFrame {

    public AD_TAB7_QLNV() {
        initComponents();
        SetIconSlidebar();

        String[] columns = {"ID", "NameAccount", "Role", "CCCD", "PhoneNumber", "Email", "Gender", "Status"};
        CN_SetupTable.SetTable(tblQLNV, jScrollPane2, columns);

//        SetTableOrder();
        CapNhatTable();
        TAB7_cbxTT.CapNhatComboBox(cbxTT);
        LoadTable();

        lblID.setText("Xin chào, " + CN_TaiKhoanDangNhap.getTenTaiKhoan());
        setTitle("CyberCafe4KL_Quản lý Nhân viên");
        CN_btnSlideBar.ganSuKienSlideBar(
                lblDM, lblOrder, lblSP, lblMT, lblHD, lblTKe, lblTKhoan, lblDX,
                this
        );

        cbxTT.addActionListener(e -> {
            TAB7_cbxTT.LocTheoTrangThai(tblQLNV, txtTimKiem, cbxTT);
        });
    }

    private void SetIconSlidebar() {
//        A1_Slidebar.SetLabelIcon(lblDX, "icID.png", ); //Khi dnhap thì hiện lên tên tkhoan
        TAB1_Slidebar.SetLabelIcon(lblDM, "icDM.png", " ĐẶT MÁY");
        TAB1_Slidebar.SetLabelIcon(lblOrder, "icOrder.png", " ORDER");
        TAB1_Slidebar.SetLabelIcon(lblSP, "icSP.png", " SẢN PHẨM");
        TAB1_Slidebar.SetLabelIcon(lblHD, "icHD1.png", " HÓA ĐƠN");
        TAB1_Slidebar.SetLabelIcon(lblMT, "icMT.png", " MÁY TÍNH");
        TAB1_Slidebar.SetLabelIcon(lblTKe, "icTKe.png", " THỐNG KÊ");
        TAB1_Slidebar.SetLabelIcon(lblTKhoan, "icTKhoan.png", " TÀI KHOẢN");
        TAB1_Slidebar.SetLabelIcon(lblDX, "icDX.png", " Đăng xuất");

        TAB1_Slidebar.SetLabelIcon(lblChat, "icChat.png", "");
        TAB1_Slidebar.SetLabelIcon(lblTB, "icTB.png", "");

    }

//    private void SetTableOrder() {
//        Color nenToi = new Color(30, 30, 47);
//        Color chuTrang = Color.WHITE;
//        Color titleDen = Color.BLACK;
//
//        tblQLNV.setBackground(nenToi);
//        tblQLNV.setForeground(chuTrang); // Màu chữ
//        tblQLNV.setSelectionBackground(new Color(100, 149, 237)); // Màu nền khi chọn
//        tblQLNV.setSelectionForeground(Color.WHITE); // Màu chữ khi chọn
//
//        tblQLNV.setGridColor(new Color(70, 70, 90));
//        tblQLNV.setRowSelectionAllowed(true);
//        tblQLNV.setColumnSelectionAllowed(false);
//        tblQLNV.setCellSelectionEnabled(false);
//        tblQLNV.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//
//        // Căn giữa tiêu đề bảng
//        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) tblQLNV.getTableHeader().getDefaultRenderer();
//        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
//        tblQLNV.getTableHeader().setForeground(titleDen);
//
//        jScrollPane2.getViewport().setBackground(nenToi);
//        jScrollPane2.setBackground(nenToi);
//    }
    public void LocTrangThai() {
        String keyword = txtTimKiem.getText().trim();
        String selected = cbxTT.getSelectedItem().toString();

        int status = -1; // -1 = tất cả
        if (selected.equals("Đang hoạt động")) {
            status = 1;
        } else if (selected.equals("Ngừng hoạt động")) {
            status = 0;
        }

        // Gọi DAO đã sửa ở TAB7_NhanVien
        ViewAD.Code.TAB7_NhanVien.LoadTable(tblQLNV, keyword, status);
    }

    private void LoadTable() {
        String keyword = txtTimKiem.getText().trim();
        TAB7_NhanVien.LoadTable(tblQLNV, keyword, -1);  // -1 = tất cả
    }

    public void CapNhatTable() {
        LoadTable();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        pnlMain = new javax.swing.JPanel();
        pnlCN = new javax.swing.JPanel();
        lblDX = new javax.swing.JLabel();
        lblTKhoan = new javax.swing.JLabel();
        lblTKe = new javax.swing.JLabel();
        lblHD = new javax.swing.JLabel();
        lblMT = new javax.swing.JLabel();
        lblSP = new javax.swing.JLabel();
        lblOrder = new javax.swing.JLabel();
        lblDM = new javax.swing.JLabel();
        lblID = new javax.swing.JLabel();
        pnlCNNgang = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblTB = new javax.swing.JLabel();
        lblChat = new javax.swing.JLabel();
        pnlMainSDM = new javax.swing.JPanel();
        pnlSDM = new javax.swing.JPanel();
        txtSDM = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblQLNV = new javax.swing.JTable();
        txtTimKiem = new javax.swing.JTextField();
        btnTim = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        cbxTT = new javax.swing.JComboBox<>();

        jScrollPane1.setViewportView(jTree1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1500, 780));
        setResizable(false);

        pnlMain.setBackground(new java.awt.Color(51, 51, 255));
        pnlMain.setMaximumSize(new java.awt.Dimension(1500, 780));
        pnlMain.setMinimumSize(new java.awt.Dimension(1500, 780));
        pnlMain.setRequestFocusEnabled(false);
        pnlMain.setVerifyInputWhenFocusTarget(false);

        pnlCN.setBackground(new java.awt.Color(44, 44, 62));
        pnlCN.setMaximumSize(new java.awt.Dimension(219, 780));
        pnlCN.setMinimumSize(new java.awt.Dimension(219, 780));
        pnlCN.setPreferredSize(new java.awt.Dimension(219, 780));
        pnlCN.setRequestFocusEnabled(false);

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

        lblID.setBackground(new java.awt.Color(255, 255, 255));
        lblID.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblID.setForeground(new java.awt.Color(204, 255, 255));
        lblID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblID.setText("[ID Admin]");

        javax.swing.GroupLayout pnlCNLayout = new javax.swing.GroupLayout(pnlCN);
        pnlCN.setLayout(pnlCNLayout);
        pnlCNLayout.setHorizontalGroup(
            pnlCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCNLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(pnlCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDX)
                    .addComponent(lblOrder)
                    .addComponent(lblDM)
                    .addComponent(lblSP)
                    .addComponent(lblHD)
                    .addComponent(lblTKe)
                    .addComponent(lblTKhoan)
                    .addComponent(lblMT))
                .addGap(82, 82, 82))
            .addComponent(lblID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlCNLayout.setVerticalGroup(
            pnlCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCNLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(lblID)
                .addGap(85, 85, 85)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 211, Short.MAX_VALUE)
                .addComponent(lblDX)
                .addGap(32, 32, 32))
        );

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
                .addGap(28, 28, 28)
                .addComponent(lblTB, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(8, 8, 8)
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
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1273, Short.MAX_VALUE)
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
        txtSDM.setText("QUẢN LÝ NHÂN VIÊN");

        jScrollPane2.setBackground(new java.awt.Color(30, 30, 47));
        jScrollPane2.setForeground(new java.awt.Color(30, 30, 47));
        jScrollPane2.setMaximumSize(new java.awt.Dimension(100, 100));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(100, 100));

        tblQLNV.setBackground(new java.awt.Color(30, 30, 47));
        tblQLNV.setForeground(new java.awt.Color(30, 30, 47));
        tblQLNV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Role", "CCCD", "PhoneNumber", "Email", "Gender", "Status"
            }
        ));
        tblQLNV.setGridColor(new java.awt.Color(30, 30, 47));
        tblQLNV.setSelectionBackground(new java.awt.Color(30, 30, 47));
        jScrollPane2.setViewportView(tblQLNV);

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        btnTim.setText("Tìm");
        btnTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimActionPerformed(evt);
            }
        });

        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        cbxTT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Đang hoạt động", "Ngừng hoạt động'" }));

        javax.swing.GroupLayout pnlSDMLayout = new javax.swing.GroupLayout(pnlSDM);
        pnlSDM.setLayout(pnlSDMLayout);
        pnlSDMLayout.setHorizontalGroup(
            pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSDMLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSDM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlSDMLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1259, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 2, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSDMLayout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTim, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbxTT, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
        );
        pnlSDMLayout.setVerticalGroup(
            pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSDMLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(txtSDM)
                .addGap(37, 37, 37)
                .addGroup(pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTim)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(cbxTT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                .addContainerGap())
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
            .addComponent(pnlCN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addComponent(pnlCNNgang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(pnlMainSDM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        String role = CN_TaiKhoanDangNhap.getTenTaiKhoan().toUpperCase(); // Lấy tên tài khoản đang đăng nhập
        String sql = "SELECT RoleAccount FROM Account WHERE NameAccount = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, CN_TaiKhoanDangNhap.getTenTaiKhoan());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String roleAcc = rs.getString("RoleAccount");
                if (!roleAcc.equalsIgnoreCase("BOSS")) {
                    JOptionPane.showMessageDialog(this, "Ối zồi ôiii!!! Trình nà zì????\nTài khoản hiện tại không có quyền thêm nhân viên");
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản đang đăng nhập.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kiểm tra quyền tài khoản.");
            return;
        }

        TAB7_ChiTietNV formThem = new TAB7_ChiTietNV(this);
        formThem.setVisible(true);
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimActionPerformed
        LoadTable();
    }//GEN-LAST:event_btnTimActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        int row = tblQLNV.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa");
            return;
        }

        String[] duLieu = new String[8];
        for (int i = 0; i < 8; i++) {
            duLieu[i] = tblQLNV.getValueAt(row, i).toString();
        }

        TAB7_ChiTietNV formSua = new TAB7_ChiTietNV(duLieu, this);
        formSua.setVisible(true);
    }//GEN-LAST:event_btnSuaActionPerformed
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AD_TAB7_QLNV().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTim;
    private javax.swing.JComboBox<String> cbxLoaiSP;
    private javax.swing.JComboBox<String> cbxLoaiSP1;
    private javax.swing.JComboBox<String> cbxTT;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTree jTree1;
    private javax.swing.JLabel lblChat;
    private javax.swing.JLabel lblDM;
    private javax.swing.JLabel lblDX;
    private javax.swing.JLabel lblHD;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblMT;
    private javax.swing.JLabel lblOrder;
    private javax.swing.JLabel lblSP;
    private javax.swing.JLabel lblTB;
    private javax.swing.JLabel lblTKe;
    private javax.swing.JLabel lblTKhoan;
    private javax.swing.JPanel pnlCN;
    private javax.swing.JPanel pnlCNNgang;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMainSDM;
    private javax.swing.JPanel pnlSDM;
    private javax.swing.JTable tblQLNV;
    private javax.swing.JLabel txtSDM;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
