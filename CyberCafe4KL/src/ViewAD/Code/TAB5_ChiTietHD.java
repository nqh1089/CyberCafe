package ViewAD.Code;

import Controller.DBConnection;
import java.awt.Color;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.swing.table.DefaultTableCellRenderer;

public class TAB5_ChiTietHD extends javax.swing.JFrame {

    private String maHD;

    public TAB5_ChiTietHD() {
        initComponents();
    }

    public TAB5_ChiTietHD(String maHD) {
        this.maHD = maHD;
        initComponents();
        System.out.println("Chi tiết hóa đơn cho mã: " + maHD);

        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Chi tiết hóa đơn: " + maHD);

        SetTableChiTietHD();
        loadDataCTHD();

    }

    private void SetTableChiTietHD() {
        Color nenToi = new Color(30, 30, 47);
        Color chuTrang = Color.WHITE;
        Color titleDen = Color.BLACK;

        // Không cho chỉnh sửa bảng
        tblCTHD.setDefaultEditor(Object.class, null);
        tblCTHD.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"STT", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        // Màu nền và chữ
        tblCTHD.setBackground(nenToi);
        tblCTHD.setForeground(chuTrang);
        tblCTHD.setGridColor(new Color(70, 70, 90));

        // Màu khi chọn hàng
        tblCTHD.setSelectionBackground(new Color(100, 149, 237));
        tblCTHD.setSelectionForeground(Color.WHITE);

        // Chỉ cho chọn hàng, không chọn ô
        tblCTHD.setCellSelectionEnabled(false);
        tblCTHD.setColumnSelectionAllowed(false);
        tblCTHD.setRowSelectionAllowed(true);
        tblCTHD.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Căn giữa tiêu đề cột
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tblCTHD.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblCTHD.getTableHeader().setForeground(titleDen);

        // ScrollPane nền tối
        jScrollPane1.getViewport().setBackground(nenToi);
        jScrollPane1.setBackground(nenToi);
    }

    private void loadDataCTHD() {
        DefaultTableModel model = (DefaultTableModel) tblCTHD.getModel();
        model.setRowCount(0); // clear bảng

        // Nếu là hóa đơn máy (HDMxx) thì gọi riêng
        if (maHD.startsWith("HDM")) {
            loadHoaDonMay(maHD);
            return; // Không xử lý tiếp OrderFood
        }

        int tongTien = 0;

        try (Connection conn = DBConnection.getConnection()) {
            int idHD = Integer.parseInt(maHD.replaceAll("[^0-9]", ""));

            // Lấy thông tin đơn hàng
            PreparedStatement ps1 = conn.prepareStatement(
                    "SELECT ofd.OrderTime, A.NameAccount, ofd.Note FROM OrderFood ofd "
                    + "JOIN Account A ON ofd.IDAccount = A.IDAccount WHERE ofd.IDOrder = ?"
            );
            ps1.setInt(1, idHD);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                Timestamp orderTime = rs1.getTimestamp("OrderTime");
                String ngayTao = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(orderTime);

                String tenNV = rs1.getString("NameAccount");
                String tenMay = rs1.getString("Note");

                lblID1.setText("Mã hóa đơn: HD" + idHD);
                lblID.setText("Ngày tạo: " + ngayTao);
                lblID3.setText("NVBH: " + tenNV);

                if (tenMay != null && !tenMay.trim().isEmpty()) {
                    lblTitle.setText("THÔNG TIN HÓA ĐƠN " + tenMay.trim());
                }
            }

            PreparedStatement ps2 = conn.prepareStatement(
                    "SELECT FD.NameFood, OD.Quantity, FD.Price AS UnitPrice, OD.TotalPrice "
                    + "FROM OrderDetail OD JOIN FoodDrink FD ON OD.IDFood = FD.IDFood WHERE OD.IDOrder = ?"
            );
            ps2.setInt(1, idHD);
            ResultSet rs2 = ps2.executeQuery();

            int stt = 1;
            while (rs2.next()) {
                String tenSP = rs2.getString("NameFood");
                int soLuong = rs2.getInt("Quantity");
                int donGia = rs2.getInt("UnitPrice");
                int thanhTien = rs2.getInt("TotalPrice");

                tongTien += thanhTien;

                model.addRow(new Object[]{
                    stt++, tenSP, soLuong, DinhDangTien(donGia), DinhDangTien(thanhTien)
                });
            }

            lblID4.setText("Tổng tiền thanh toán: " + DinhDangTien(tongTien) + " đ");

            // Nếu không tìm thấy trong OrderFood, thử hiển thị hóa đơn máy
            if (tblCTHD.getRowCount() == 0) {
                loadHoaDonMay(maHD);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi load dữ liệu hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadHoaDonMay(String maHD) {
        DefaultTableModel model = (DefaultTableModel) tblCTHD.getModel();
        model.setRowCount(0); // Xóa bảng trước khi load mới

        try (Connection conn = DBConnection.getConnection()) {
            int idHD = Integer.parseInt(maHD.replaceAll("[^0-9]", ""));

            PreparedStatement ps = conn.prepareStatement("""
            SELECT TOP 1 
                I.CreateAt, 
                I.TotalAmount, 
                A.NameAccount AS AdminName, 
                CU.StartTime, CU.EndTime, CU.Cost,
                C.NameComputer, ACC.NameAccount AS UserName
            FROM Invoice I
            JOIN Account A ON I.IDAccount = A.IDAccount
            JOIN ComputerUsage CU ON CU.EndTime IS NOT NULL
                                  AND CU.EndTime = (
                                      SELECT MAX(EndTime)
                                      FROM ComputerUsage
                                      WHERE IDComputer = CU.IDComputer AND EndTime IS NOT NULL
                                  )
            JOIN Computer C ON CU.IDComputer = C.IDComputer
            JOIN Account ACC ON CU.IDAccount = ACC.IDAccount
            WHERE I.IDInvoice = ?
        """);

            ps.setInt(1, idHD);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String ngayTao = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm")
                        .format(rs.getTimestamp("CreateAt"));
                String tenAdmin = rs.getString("AdminName");
                String tenKH = rs.getString("UserName");
                String tenMay = rs.getString("NameComputer");

                Timestamp start = rs.getTimestamp("StartTime");
                Timestamp end = rs.getTimestamp("EndTime");

                double costGioChoi = rs.getDouble("Cost");
                double tongTien = rs.getDouble("TotalAmount");

                lblID1.setText("Mã hóa đơn: HD" + idHD);
                lblID.setText("Ngày tạo: " + ngayTao);
                lblID3.setText("Trực máy: " + tenAdmin);
                lblTitle.setText("HÓA ĐƠN MÁY: " + tenMay + " (KH: " + tenKH + ")");

                int stt = 1;
                model.addRow(new Object[]{
                    stt++, "Giờ chơi (" + formatTime(start) + " → " + formatTime(end) + ")", 1,
                    DinhDangTien((int) costGioChoi), DinhDangTien((int) costGioChoi)
                });

                // ✅ Bổ sung: lấy chi tiết dịch vụ từ OrderDetail
                PreparedStatement psDV = conn.prepareStatement("""
                SELECT FD.NameFood, OD.Quantity, FD.Price AS UnitPrice, OD.TotalPrice
                FROM OrderDetail OD
                JOIN FoodDrink FD ON OD.IDFood = FD.IDFood
                WHERE OD.IDOrder = ?
            """);
                psDV.setInt(1, idHD);
                ResultSet rsDV = psDV.executeQuery();

                while (rsDV.next()) {
                    String tenSP = rsDV.getString("NameFood");
                    int soLuong = rsDV.getInt("Quantity");
                    int donGia = rsDV.getInt("UnitPrice");
                    int thanhTien = rsDV.getInt("TotalPrice");

                    model.addRow(new Object[]{
                        stt++, tenSP, soLuong,
                        DinhDangTien(donGia), DinhDangTien(thanhTien)
                    });
                }

                lblID4.setText("Tổng tiền thanh toán: " + DinhDangTien((int) tongTien) + " đ");

            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn máy phù hợp!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải hóa đơn máy!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatTime(Timestamp ts) {
        return new java.text.SimpleDateFormat("HH:mm").format(ts);
    }

    private String DinhDangTien(int so) {
        return String.format("%,d", so).replace(",", " ");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblID = new javax.swing.JLabel();
        lblID1 = new javax.swing.JLabel();
        lblID3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCTHD = new javax.swing.JTable();
        lblID4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 255));

        jPanel2.setBackground(new java.awt.Color(153, 255, 153));

        jPanel3.setBackground(new java.awt.Color(30, 30, 47));

        lblTitle.setBackground(new java.awt.Color(255, 255, 255));
        lblTitle.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("THÔNG TIN HÓA ĐƠN");

        lblID.setBackground(new java.awt.Color(255, 255, 255));
        lblID.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lblID.setForeground(new java.awt.Color(204, 255, 255));
        lblID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblID.setText("[NGÀY TẠO ĐƠN]");

        lblID1.setBackground(new java.awt.Color(255, 255, 255));
        lblID1.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblID1.setForeground(new java.awt.Color(204, 255, 255));
        lblID1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblID1.setText("[MÃ HOÁ ĐƠN]");

        lblID3.setBackground(new java.awt.Color(255, 255, 255));
        lblID3.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lblID3.setForeground(new java.awt.Color(204, 255, 255));
        lblID3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblID3.setText("NVBH:");

        jScrollPane1.setBackground(new java.awt.Color(30, 30, 47));

        tblCTHD.setBackground(new java.awt.Color(30, 30, 47));
        tblCTHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "STT", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"
            }
        ));
        jScrollPane1.setViewportView(tblCTHD);

        lblID4.setBackground(new java.awt.Color(255, 255, 255));
        lblID4.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lblID4.setForeground(new java.awt.Color(204, 255, 255));
        lblID4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblID4.setText("Tổng tiền thanh toán:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
                    .addComponent(lblID1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblID3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblID4, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lblTitle)
                .addGap(18, 18, 18)
                .addComponent(lblID1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblID)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblID3)
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblID4)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblID1;
    private javax.swing.JLabel lblID3;
    private javax.swing.JLabel lblID4;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblCTHD;
    // End of variables declaration//GEN-END:variables
}
