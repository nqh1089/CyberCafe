package ViewAD.View;

import Controller.DBConnection;
import ViewAD.Code.CN_SetupTable;
import ViewAD.Code.CN_Slidebar;
import ViewAD.Code.CN_TaiKhoanDangNhap;
import ViewAD.Code.TAB3_ThemSP;
import ViewAD.Code.TAB3_LoadTT;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class AD_TAB3_QLSP extends javax.swing.JFrame {

    private AD_TAB3_QLSP LoadSP;

    public AD_TAB3_QLSP() {
        initComponents();

        CN_Slidebar.SetSlidebar(
                lblDM, lblOrder, lblSP, lblMT, lblHD, lblTKe, lblTKhoan,
                lblDMK, lblDX, lblChat, lblTB, this
        );

        String[] columns = {"Mã SP", "Ảnh SP", "Tên SP", "Loại SP", "Giá SP", "Trạng Thái"};
        CN_SetupTable.SetTable(tblSanPham, jScrollPane2, columns);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        CapNhatBangSanPham();
    }

    public void CapNhatBangSanPham() {
        String loaiSP = cbxLocSP.getSelectedItem().toString();
        String trangThai = cbxLocTT.getSelectedItem().toString();
        TAB3_LoadTT.LoadData(tblSanPham, loaiSP, trangThai);
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
        tblSanPham = new javax.swing.JTable();
        txtFind = new javax.swing.JTextField();
        btnTim = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnAn = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        cbxLocSP = new javax.swing.JComboBox<>();
        cbxLocTT = new javax.swing.JComboBox<>();
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
        txtSDM.setText("QUẢN LÝ SẢN PHẨM");

        jScrollPane2.setBackground(new java.awt.Color(30, 30, 47));
        jScrollPane2.setForeground(new java.awt.Color(30, 30, 47));
        jScrollPane2.setMaximumSize(new java.awt.Dimension(100, 100));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(100, 100));

        tblSanPham.setBackground(new java.awt.Color(30, 30, 47));
        tblSanPham.setForeground(new java.awt.Color(30, 30, 47));
        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã SP", "Ảnh SP", "Tên SP", "Loại SP", "Giá SP", "Trạng thái"
            }
        ));
        tblSanPham.setGridColor(new java.awt.Color(30, 30, 47));
        tblSanPham.setSelectionBackground(new java.awt.Color(30, 30, 47));
        jScrollPane2.setViewportView(tblSanPham);

        txtFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFindActionPerformed(evt);
            }
        });

        btnTim.setText("Tìm");
        btnTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnAn.setText("Ẩn");
        btnAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnActionPerformed(evt);
            }
        });

        btnXoa.setText("Xoá");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        cbxLocSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Đồ ăn", "Đồ uống", "Gói nạp" }));
        cbxLocSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxLocSPActionPerformed(evt);
            }
        });

        cbxLocTT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Đang bán", "Ngừng bán" }));
        cbxLocTT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxLocTTActionPerformed(evt);
            }
        });

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
                .addGap(38, 38, 38)
                .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTim, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(cbxLocSP, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxLocTT, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAn, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
        );
        pnlSDMLayout.setVerticalGroup(
            pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSDMLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(txtSDM)
                .addGap(37, 37, 37)
                .addGroup(pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbxLocSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbxLocTT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTim)
                        .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnThem)
                        .addComponent(btnSua)
                        .addComponent(btnAn)
                        .addComponent(btnXoa)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
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

    private void txtFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFindActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFindActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        int row = tblSanPham.getSelectedRow();
        if (row >= 0) {
            String maSP = tblSanPham.getValueAt(row, 0).toString();
            new TAB3_ThemSP(maSP, this).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để sửa");
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        new TAB3_ThemSP(this).setVisible(true);
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnActionPerformed
        int row = tblSanPham.getSelectedRow();
        if (row >= 0) {
            String maSP = tblSanPham.getValueAt(row, 0).toString();
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE FoodDrink SET Available = 0 WHERE IDFood = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, maSP);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Đã ẩn sản phẩm (chuyển sang NGỪNG BÁN)");
                    CapNhatBangSanPham();
                } else {
                    JOptionPane.showMessageDialog(this, "Ẩn sản phẩm thất bại");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để ẩn");
        }
    }//GEN-LAST:event_btnAnActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int row = tblSanPham.getSelectedRow();
        if (row >= 0) {
            String maSP = tblSanPham.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xoá sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "DELETE FROM FoodDrink WHERE IDFood = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, maSP);
                    int rows = stmt.executeUpdate();

                    if (rows > 0) {
                        //
                        int maxID = 0;
                        String maxSql = "SELECT ISNULL(MAX(IDFood), 0) AS MaxID FROM FoodDrink";
                        PreparedStatement maxStmt = conn.prepareStatement(maxSql);
                        ResultSet rs = maxStmt.executeQuery();
                        if (rs.next()) {
                            maxID = rs.getInt("MaxID");
                        }

                        // Reset IDENTITY về maxID -> lấy id sp cao nhất trong bảng
                        String reseedSql = "DBCC CHECKIDENT ('FoodDrink', RESEED, ?)";
                        PreparedStatement resetStmt = conn.prepareStatement(reseedSql);
                        resetStmt.setInt(1, maxID);
                        resetStmt.execute();

                        JOptionPane.showMessageDialog(this, "Đã xoá sản phẩm");
                        CapNhatBangSanPham();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xoá sản phẩm thất bại");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xoá");
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void cbxLocSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxLocSPActionPerformed
        CapNhatBangSanPham();
    }//GEN-LAST:event_cbxLocSPActionPerformed

    private void cbxLocTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxLocTTActionPerformed
        CapNhatBangSanPham();
    }//GEN-LAST:event_cbxLocTTActionPerformed

    private void btnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimActionPerformed
        String tenSP = txtFind.getText();
        String loaiSP = cbxLocSP.getSelectedItem().toString();
        String trangThai = cbxLocTT.getSelectedItem().toString();
        TAB3_LoadTT.TimKiemTheoTen(tblSanPham, tenSP, loaiSP, trangThai);

    }//GEN-LAST:event_btnTimActionPerformed
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AD_TAB3_QLSP().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAn;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTim;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cbxLocSP;
    private javax.swing.JComboBox<String> cbxLocTT;
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
    private javax.swing.JPanel pnlCN;
    private javax.swing.JPanel pnlCNNgang;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMainSDM;
    private javax.swing.JPanel pnlSDM;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTextField txtFind;
    private javax.swing.JLabel txtSDM;
    // End of variables declaration//GEN-END:variables
}
