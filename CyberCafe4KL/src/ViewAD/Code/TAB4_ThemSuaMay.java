package ViewAD.Code;

import Controller.DBConnection;
import ViewAD.Code.TAB4_LoadMayTinh;
import ViewAD.View.AD_TAB4_QLMT;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class TAB4_ThemSuaMay extends javax.swing.JFrame {

    private boolean isEdit = false;
    private AD_TAB4_QLMT parentForm;

    public TAB4_ThemSuaMay(AD_TAB4_QLMT parent, String tenMay, String cpu, String ram, String gpu, String monitor, String gia, int trangThai) {
        initComponents();
        setResizable(false); // Không cho phóng to
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng form này, k đóng form chính
        this.setLocationRelativeTo(null); // Set hiển giữa màn hình


        this.parentForm = parent;
        if (tenMay != null && !tenMay.isEmpty()) {
            isEdit = true;
            lblTittle.setText("SỬA THÔNG TIN");
            btnThemSua.setText("CẬP NHẬT");

            txtCPU.setText(cpu);
            txtRAM.setText(ram);
            txtGPU.setText(gpu);
            txtMonitor.setText(monitor);
            txtGia.setText(gia.replace(" VND/phút", ""));
            cbxTrangThai.setSelectedIndex(trangThai);
            lblTenMay.setText(tenMay);
        } else {
            lblTittle.setText("THÊM MÁY");
            btnThemSua.setText("THÊM MÁY");

            try (Connection conn = DBConnection.getConnection()) {
                String tenMoi = taoTenMayMoi(conn);
                lblTenMay.setText(tenMoi);
            } catch (Exception ex) {
                lblTenMay.setText("Không thể lấy tên máy");
                ex.printStackTrace();
            }
        }
        btnThemSua.addActionListener(e -> xuLyThemHoacSua());
    }

    private void xuLyThemHoacSua() {
        String cpu = txtCPU.getText().trim();
        String ram = txtRAM.getText().trim();
        String gpu = txtGPU.getText().trim();
        String monitor = txtMonitor.getText().trim();
        String giaStr = txtGia.getText().trim();
        int trangThai = cbxTrangThai.getSelectedIndex();

        if (cpu.isEmpty() || ram.isEmpty() || gpu.isEmpty() || monitor.isEmpty() || giaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        int gia = 0;
        try {
            gia = Integer.parseInt(giaStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Giá tiền phải là số!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (isEdit) {
                String sql = "UPDATE Computer SET CPU=?, RAM=?, GPU=?, Monitor=?, PricePerMinute=?, ComputerStatus=? WHERE NameComputer=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, cpu);
                ps.setString(2, ram);
                ps.setString(3, gpu);
                ps.setString(4, monitor);
                ps.setInt(5, gia);
                ps.setInt(6, trangThai);
                ps.setString(7, lblTenMay.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cập nhật máy thành công!");
            } else {
                String tenMoi = taoTenMayMoi(conn);
                int idMoi = layIDMoi(conn);
                String sql = "INSERT INTO Computer (IDComputer, NameComputer, PricePerMinute, CPU, RAM, GPU, Monitor, ComputerStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, idMoi);
                ps.setString(2, tenMoi);
                ps.setInt(3, gia);
                ps.setString(4, cpu);
                ps.setString(5, ram);
                ps.setString(6, gpu);
                ps.setString(7, monitor);
                ps.setInt(8, trangThai);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thêm máy thành công!");
            }

            parentForm.CapNhatSoDoMay(); // gọi lại sau khi thêm/sửa
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private int layIDMoi(Connection conn) throws SQLException {
        String sql = "SELECT ISNULL(MAX(IDComputer), 0) + 1 FROM Computer";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 1;
    }

    private String taoTenMayMoi(Connection conn) throws SQLException {
        int i = 1;
        while (true) {
            String ten = String.format("MÁY %02d", i);
            String sql = "SELECT 1 FROM Computer WHERE NameComputer = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ten);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return ten;
            }
            i++;
        }
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
        lblTittle = new javax.swing.JLabel();
        txtCPU = new javax.swing.JTextField();
        txtRAM = new javax.swing.JTextField();
        txtGPU = new javax.swing.JTextField();
        btnThemSua = new javax.swing.JButton();
        cbxTrangThai = new javax.swing.JComboBox<>();
        CCCD1 = new javax.swing.JLabel();
        txtMonitor = new javax.swing.JTextField();
        CCCD2 = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        lblTenMay = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlMain.setBackground(new java.awt.Color(51, 51, 255));

        pnlMain2.setBackground(new java.awt.Color(153, 255, 153));

        pnlRegister.setBackground(new java.awt.Color(30, 30, 47));

        NameAccount.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        NameAccount.setForeground(new java.awt.Color(255, 255, 255));
        NameAccount.setText("CPU:");

        PhoneNumber.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        PhoneNumber.setForeground(new java.awt.Color(255, 255, 255));
        PhoneNumber.setText("RAM:");

        Gender.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        Gender.setForeground(new java.awt.Color(255, 255, 255));
        Gender.setText("Trạng thái:");

        CCCD.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        CCCD.setForeground(new java.awt.Color(255, 255, 255));
        CCCD.setText("GPU:");

        lblTittle.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblTittle.setForeground(new java.awt.Color(255, 255, 255));
        lblTittle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTittle.setText("THÊM SỬA MÁY");

        txtCPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCPUActionPerformed(evt);
            }
        });

        txtRAM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRAMActionPerformed(evt);
            }
        });

        txtGPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGPUActionPerformed(evt);
            }
        });

        btnThemSua.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnThemSua.setText("THÊM SỬA MÁY");
        btnThemSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSuaActionPerformed(evt);
            }
        });

        cbxTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Máy đang hoạt động", "Máy đang bảo trì" }));
        cbxTrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTrangThaiActionPerformed(evt);
            }
        });

        CCCD1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        CCCD1.setForeground(new java.awt.Color(255, 255, 255));
        CCCD1.setText("MONITOR:");

        txtMonitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMonitorActionPerformed(evt);
            }
        });

        CCCD2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        CCCD2.setForeground(new java.awt.Color(255, 255, 255));
        CCCD2.setText("GIÁ TIỀN:");

        txtGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGiaActionPerformed(evt);
            }
        });

        lblTenMay.setBackground(new java.awt.Color(255, 255, 255));
        lblTenMay.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblTenMay.setForeground(new java.awt.Color(204, 255, 255));
        lblTenMay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTenMay.setText("[Tên máy]");

        javax.swing.GroupLayout pnlRegisterLayout = new javax.swing.GroupLayout(pnlRegister);
        pnlRegister.setLayout(pnlRegisterLayout);
        pnlRegisterLayout.setHorizontalGroup(
            pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTittle, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
            .addGroup(pnlRegisterLayout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cbxTrangThai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtGPU)
                            .addComponent(txtRAM)
                            .addComponent(txtCPU, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnThemSua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Gender, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMonitor)
                            .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(CCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(PhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(NameAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(CCCD1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(CCCD2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlRegisterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTenMay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlRegisterLayout.setVerticalGroup(
            pnlRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegisterLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblTittle, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTenMay, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                .addGap(28, 28, 28)
                .addComponent(NameAccount)
                .addGap(15, 15, 15)
                .addComponent(txtCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(PhoneNumber)
                .addGap(15, 15, 15)
                .addComponent(txtRAM, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(CCCD)
                .addGap(15, 15, 15)
                .addComponent(txtGPU, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(CCCD1)
                .addGap(15, 15, 15)
                .addComponent(txtMonitor, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(CCCD2)
                .addGap(15, 15, 15)
                .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(Gender)
                .addGap(15, 15, 15)
                .addComponent(cbxTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(btnThemSua, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
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

    private void txtCPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCPUActionPerformed

    }//GEN-LAST:event_txtCPUActionPerformed

    private void txtRAMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRAMActionPerformed

    }//GEN-LAST:event_txtRAMActionPerformed

    private void txtGPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGPUActionPerformed

    }//GEN-LAST:event_txtGPUActionPerformed

    private void btnThemSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSuaActionPerformed

    }//GEN-LAST:event_btnThemSuaActionPerformed

    private void txtMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMonitorActionPerformed

    }//GEN-LAST:event_txtMonitorActionPerformed

    private void txtGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGiaActionPerformed

    }//GEN-LAST:event_txtGiaActionPerformed

    private void cbxTrangThaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTrangThaiActionPerformed

    }//GEN-LAST:event_cbxTrangThaiActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CCCD;
    private javax.swing.JLabel CCCD1;
    private javax.swing.JLabel CCCD2;
    private javax.swing.JLabel Gender;
    private javax.swing.JLabel NameAccount;
    private javax.swing.JLabel PhoneNumber;
    private javax.swing.JButton btnThemSua;
    private javax.swing.JComboBox<String> cbxTrangThai;
    private javax.swing.JLabel lblTenMay;
    private javax.swing.JLabel lblTittle;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMain2;
    private javax.swing.JPanel pnlRegister;
    private javax.swing.JTextField txtCPU;
    private javax.swing.JTextField txtGPU;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtMonitor;
    private javax.swing.JTextField txtRAM;
    // End of variables declaration//GEN-END:variables
}
