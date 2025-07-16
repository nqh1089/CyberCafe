package ViewAD.View;

import ViewAD.Code.CN_Slidebar;
import ViewAD.Code.CN_TaiKhoanDangNhap;
import ViewAD.Code.TAB5_ChiTietHD;
import ViewAD.Code.TAB5_LoadDuLieuHD;
import ViewAD.Code.TAB5_cbxThang;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class AD_TAB5_QLHD extends javax.swing.JFrame {

    public AD_TAB5_QLHD() {
        initComponents();
        CN_Slidebar.SetSlidebar(
                lblDM, lblOrder, lblSP, lblMT, lblHD, lblTKe, lblTKhoan,
                lblDMK, lblDX, lblChat, lblTB, this
        );
        SetTableHoaDon();

        lblID.setText("Xin chào, " + CN_TaiKhoanDangNhap.getTenTaiKhoan());
        setTitle("CyberCafe4KL_Quản lý Hóa đơn");

        TAB5_cbxThang.LoadThangCoHD(cbxThang);
        int thangHienTai = TAB5_cbxThang.LaySoThangDangChon(cbxThang);
        TAB5_LoadDuLieuHD.LoadHoaDonTheoThang(thangHienTai, TableHoaDon, lblTSHD, lblTTTD);
    }

    private void SetTableHoaDon() {
        Color nenToi = new Color(30, 30, 47);
        Color chuTrang = Color.WHITE;
        Color titleDen = Color.BLACK;

        // Bắt buộc phải set lại model trước
        TableHoaDon.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"Mã HD", "Tổng SL", "Tổng tiền", "Nhân viên bán", "Ngày tạo"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        // Màu bảng
        TableHoaDon.setBackground(nenToi);
        TableHoaDon.setForeground(chuTrang);
        TableHoaDon.setGridColor(new Color(70, 70, 90));

        // Màu khi chọn hàng
        TableHoaDon.setSelectionBackground(new Color(100, 149, 237)); // Xanh dương
        TableHoaDon.setSelectionForeground(Color.WHITE);              // Chữ trắng

        // Chỉ chọn hàng, không chọn từng ô
        TableHoaDon.setCellSelectionEnabled(false);
        TableHoaDon.setColumnSelectionAllowed(false);
        TableHoaDon.setRowSelectionAllowed(true);
        TableHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Chọn 1 hàng duy nhất

        // Căn giữa header
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) TableHoaDon.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        TableHoaDon.getTableHeader().setForeground(titleDen);

        // ScrollPane màu nền
        jScrollPane2.getViewport().setBackground(nenToi);
        jScrollPane2.setBackground(nenToi);

        // Double click => mở chi tiết hóa đơn
        TableHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && TableHoaDon.getSelectedRow() != -1) {
                    int row = TableHoaDon.getSelectedRow();
                    String maHD = TableHoaDon.getValueAt(row, 0).toString();
                    new TAB5_ChiTietHD(maHD).setVisible(true);
                }
            }
        });
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
        jScrollPane2 = new javax.swing.JScrollPane();
        TableHoaDon = new javax.swing.JTable();
        pnlTSHD = new javax.swing.JPanel();
        lblTSHD = new javax.swing.JLabel();
        txtTSHD = new javax.swing.JLabel();
        pnlTTTD = new javax.swing.JPanel();
        lblTTTD = new javax.swing.JLabel();
        txtTTTD = new javax.swing.JLabel();
        cbxThang = new javax.swing.JComboBox<>();
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
        txtSDM.setText("QUẢN LÝ HÓA ĐƠN");

        jScrollPane2.setBackground(new java.awt.Color(30, 30, 47));
        jScrollPane2.setForeground(new java.awt.Color(30, 30, 47));
        jScrollPane2.setMaximumSize(new java.awt.Dimension(100, 100));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(100, 100));

        TableHoaDon.setBackground(new java.awt.Color(30, 30, 47));
        TableHoaDon.setForeground(new java.awt.Color(30, 30, 47));
        TableHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã HD", "Tổng số lượng SP", "Tổng tiền", "Nhân viên bán", "Ngày tạo"
            }
        ));
        TableHoaDon.setGridColor(new java.awt.Color(30, 30, 47));
        TableHoaDon.setSelectionBackground(new java.awt.Color(30, 30, 47));
        jScrollPane2.setViewportView(TableHoaDon);

        pnlTSHD.setBackground(new java.awt.Color(255, 255, 255));

        lblTSHD.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lblTSHD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTSHD.setText("100");

        txtTSHD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTSHD.setText("[TỔNG SỐ HD]");

        javax.swing.GroupLayout pnlTSHDLayout = new javax.swing.GroupLayout(pnlTSHD);
        pnlTSHD.setLayout(pnlTSHDLayout);
        pnlTSHDLayout.setHorizontalGroup(
            pnlTSHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTSHDLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTSHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(txtTSHD, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
        );
        pnlTSHDLayout.setVerticalGroup(
            pnlTSHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTSHDLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(lblTSHD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTSHD)
                .addGap(12, 12, 12))
        );

        pnlTTTD.setBackground(new java.awt.Color(255, 255, 255));

        lblTTTD.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lblTTTD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTTTD.setText("100");

        txtTTTD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTTTD.setText("[TỔNG TIỀN THU ĐƯỢC]");

        javax.swing.GroupLayout pnlTTTDLayout = new javax.swing.GroupLayout(pnlTTTD);
        pnlTTTD.setLayout(pnlTTTDLayout);
        pnlTTTDLayout.setHorizontalGroup(
            pnlTTTDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTTTD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtTTTD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
        );
        pnlTTTDLayout.setVerticalGroup(
            pnlTTTDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTTTDLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(lblTTTD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTTTD)
                .addGap(12, 12, 12))
        );

        cbxThang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tháng 6" }));
        cbxThang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxThangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSDMLayout = new javax.swing.GroupLayout(pnlSDM);
        pnlSDM.setLayout(pnlSDMLayout);
        pnlSDMLayout.setHorizontalGroup(
            pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSDMLayout.createSequentialGroup()
                .addGroup(pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSDMLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSDM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(pnlSDMLayout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 2, Short.MAX_VALUE))))
                    .addGroup(pnlSDMLayout.createSequentialGroup()
                        .addGap(356, 356, 356)
                        .addComponent(pnlTSHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnlTTTD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(pnlSDMLayout.createSequentialGroup()
                .addGap(584, 584, 584)
                .addComponent(cbxThang, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSDMLayout.setVerticalGroup(
            pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSDMLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(txtSDM)
                .addGap(18, 18, 18)
                .addComponent(cbxThang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlTSHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlTTTD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxThangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxThangActionPerformed
        int thang = TAB5_cbxThang.LaySoThangDangChon(cbxThang);
        TAB5_LoadDuLieuHD.LoadHoaDonTheoThang(thang, TableHoaDon, lblTSHD, lblTTTD);
    }//GEN-LAST:event_cbxThangActionPerformed
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AD_TAB5_QLHD().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TableHoaDon;
    private javax.swing.JComboBox<String> cbxThang;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
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
    private javax.swing.JLabel lblTTTD;
    private javax.swing.JPanel pnlCN;
    private javax.swing.JPanel pnlCNNgang;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMainSDM;
    private javax.swing.JPanel pnlSDM;
    private javax.swing.JPanel pnlTSHD;
    private javax.swing.JPanel pnlTTTD;
    private javax.swing.JLabel txtSDM;
    private javax.swing.JLabel txtTSHD;
    private javax.swing.JLabel txtTTTD;
    // End of variables declaration//GEN-END:variables
}
