package ViewAD.View;

import Controller.DAO;
import Controller.DBConnection;
import Model.Products;
import ViewAD.Code.CN_TaiKhoanDangNhap;
import ViewAD.Code.CN_btnSlideBar;
import ViewAD.Code.TAB1_Slidebar;
import ViewAD.Code.TAB2_CardSP;
import ViewAD.Code.TAB2_QR;
import ViewC.Code.CN_BienToanCuc;
import java.sql.Connection;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;

public class AD_TAB2_Order extends javax.swing.JFrame {

    private List<Products> danhSachSanPham;

    public AD_TAB2_Order() {
        initComponents();
        SetIconSlidebar();
        SetTableOrder();
        danhSachSanPham = getAllProducts();
        hienThiSanPham(danhSachSanPham);
        HienThongTinHoaDon();

        ((DefaultTableModel) tableOrder.getModel()).setRowCount(0);

        lblID.setText("Xin chào, " + CN_TaiKhoanDangNhap.getTenTaiKhoan());
        setTitle("CyberCafe4KL_Order");
        CN_btnSlideBar.ganSuKienSlideBar(
                lblDM, lblOrder, lblSP, lblMT, lblHD, lblTKe, lblTKhoan, lblDX,
                this
        );

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID"); // Ẩn cột ID trên bảng
        model.addColumn("Tên sản phẩm");
        model.addColumn("Đơn giá");
        model.addColumn("Số lượng");
        model.addColumn("Thành tiền");

        tableOrder.setModel(model);
        tableOrder.getColumnModel().getColumn(0).setMinWidth(0);
        tableOrder.getColumnModel().getColumn(0).setMaxWidth(0);

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

    private void SetTableOrder() {

        Color nenToi = new Color(30, 30, 47);
        Color ChuTrang = Color.WHITE;
        Color TitleDen = Color.BLACK;

        tableOrder.setBackground(nenToi);
        tableOrder.setForeground(ChuTrang);  // Màu chữ trong bảng
        tableOrder.setSelectionBackground(new Color(60, 60, 90));
        tableOrder.setGridColor(new Color(70, 70, 90));

        //Căn giữa tiêu đề bảng
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) tableOrder.getTableHeader().getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        tableOrder.getTableHeader().setForeground(TitleDen);  // Màu chữ tiêu đề

        jScrollPane4.getViewport().setBackground(nenToi);
        jScrollPane4.setBackground(nenToi);
    }

    public List<Products> getAllProducts() {
        List<Products> danhSach = new ArrayList<>();
        String sql = "SELECT IDFood, NameFood, Price, Category, ImageFood, Available FROM FoodDrink WHERE Available = 1";

        try (
                Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Products sp = new Products();
                sp.setId(rs.getInt("IDFood"));
                sp.setTenSP(rs.getString("NameFood"));
                sp.setGia(rs.getInt("Price"));
                sp.setLoaiSP(rs.getString("Category"));
                sp.setHinhAnh(rs.getString("ImageFood"));
                sp.setTrangThai(rs.getInt("Available")); // 1: còn bán

                danhSach.add(sp);
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy sản phẩm: " + e.getMessage());
        }

        return danhSach;
    }

    private void hienThiSanPham(List<Products> ds) {
        pnlSP.removeAll();

        // Hiển thị dạng lưới 2 cột, khoảng cách 10px
        pnlSP.setLayout(new GridLayout(0, 2, 10, 10));

        for (Products sp : ds) {
            JPanel card = TAB2_CardSP.taoCard(sp, tableOrder, jTextField1, jTextField2, jTextField3);
            pnlSP.add(card);
        }

        pnlSP.revalidate();
        pnlSP.repaint();
    }

    private void HienThongTinHoaDon() {
        jTextField1.setEditable(false);
        jTextField2.setEditable(false);
        jTextField3.setEditable(false);

        // Hiển thị ngày giờ hiện tại
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        lblNgayGio.setText(now.format(fmt));

        // Lấy mã hóa đơn mới từ DB
        String maHD = LayMaHoaDonMoi();
        lblMaHD.setText(maHD);

        // Gọi tên tài khoản đăng nhập hiện tại
        String ten = CN_TaiKhoanDangNhap.getTenTaiKhoan();
        lblMaNV.setText("NV: " + ten);
    }

    private String LayMaHoaDonMoi() {
        String maMoi = "HĐ1"; // mặc định nếu DB rỗng
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                String sql = "SELECT MAX(IDOrder) AS MaxID FROM OrderFood";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int max = rs.getInt("MaxID");
                    if (!rs.wasNull()) {
                        maMoi = "HĐ" + (max + 1);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy mã hóa đơn mới: " + e.getMessage());
        }
        return maMoi;
    }

    public void resetSauKhiThanhToan() {
        // Reset bảng order
        ((DefaultTableModel) tableOrder.getModel()).setRowCount(0);

        // Reset các ô tổng tiền
        jTextField1.setText("0");
        jTextField2.setText("0");
        jTextField3.setText("0");

        // Reset card sản phẩm (lấy lại từ DB)
        danhSachSanPham = getAllProducts();
        hienThiSanPham(danhSachSanPham);

        // Reset thông tin hóa đơn: ngày giờ, mã HD mới, nhân viên
        HienThongTinHoaDon();
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
        cbxLoaiSP = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        pnlSP = new javax.swing.JPanel();
        pnlMainTTM = new javax.swing.JPanel();
        pnlSDM1 = new javax.swing.JPanel();
        pnlButton = new javax.swing.JPanel();
        btnThanhToan = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblNgayGio = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblMaHD = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableOrder = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        lblMaNV = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();

        jScrollPane1.setViewportView(jTree1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1500, 780));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
                .addGap(55, 55, 55)
                .addGroup(pnlCNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDX)
                    .addComponent(lblOrder)
                    .addComponent(lblDM)
                    .addComponent(lblSP)
                    .addComponent(lblHD)
                    .addComponent(lblTKe)
                    .addComponent(lblTKhoan)
                    .addComponent(lblMT))
                .addContainerGap(87, Short.MAX_VALUE))
            .addComponent(lblID, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
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

        txtSDM.setBackground(new java.awt.Color(204, 255, 255));
        txtSDM.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        txtSDM.setForeground(new java.awt.Color(204, 255, 255));
        txtSDM.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtSDM.setText("ORDER SẢN PHẨM");

        cbxLoaiSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Đồ ăn", "Đồ uống", "Gói nạp" }));
        cbxLoaiSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxLoaiSPActionPerformed(evt);
            }
        });

        pnlSP.setBackground(new java.awt.Color(30, 30, 47));

        javax.swing.GroupLayout pnlSPLayout = new javax.swing.GroupLayout(pnlSP);
        pnlSP.setLayout(pnlSPLayout);
        pnlSPLayout.setHorizontalGroup(
            pnlSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 809, Short.MAX_VALUE)
        );
        pnlSPLayout.setVerticalGroup(
            pnlSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 628, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(pnlSP);

        javax.swing.GroupLayout pnlSDMLayout = new javax.swing.GroupLayout(pnlSDM);
        pnlSDM.setLayout(pnlSDMLayout);
        pnlSDMLayout.setHorizontalGroup(
            pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSDMLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txtSDM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSDMLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbxLoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );
        pnlSDMLayout.setVerticalGroup(
            pnlSDMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSDMLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(txtSDM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbxLoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3)
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

        pnlMainTTM.setBackground(new java.awt.Color(153, 255, 153));

        pnlSDM1.setBackground(new java.awt.Color(30, 30, 47));
        pnlSDM1.setForeground(new java.awt.Color(255, 255, 255));
        pnlSDM1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        pnlButton.setBackground(new java.awt.Color(44, 44, 62));

        btnThanhToan.setBackground(new java.awt.Color(204, 255, 255));
        btnThanhToan.setText("Thanh toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlButtonLayout = new javax.swing.GroupLayout(pnlButton);
        pnlButton.setLayout(pnlButtonLayout);
        pnlButtonLayout.setHorizontalGroup(
            pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonLayout.createSequentialGroup()
                .addGap(171, 171, 171)
                .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlButtonLayout.setVerticalGroup(
            pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlButtonLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        jLabel17.setForeground(new java.awt.Color(204, 255, 255));
        jLabel17.setText("Tổng tiền thanh toán:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("THÔNG TIN HÓA ĐƠN");

        jLabel18.setForeground(new java.awt.Color(204, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("-----------------------------------------------------------");

        lblNgayGio.setForeground(new java.awt.Color(204, 255, 255));
        lblNgayGio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNgayGio.setText("[Ngày và giờ]");

        jLabel19.setForeground(new java.awt.Color(204, 255, 255));
        jLabel19.setText("Tổng tiền giảm:");

        lblMaHD.setForeground(new java.awt.Color(204, 255, 255));
        lblMaHD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMaHD.setText("[Mã Hóa Đơn]");

        jLabel20.setForeground(new java.awt.Color(204, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("-----------------------------------------------------------");

        tableOrder.setBackground(new java.awt.Color(30, 30, 47));
        tableOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, "", null},
                {null, null, "", null},
                {null, null, "", null},
                {null, null, null, null}
            },
            new String [] {
                "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"
            }
        ));
        jScrollPane4.setViewportView(tableOrder);

        jLabel22.setForeground(new java.awt.Color(204, 255, 255));
        jLabel22.setText("Tổng tiền sản phẩm:");

        jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField1.setText("jTextField1");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField2.setText("jTextField2");

        lblMaNV.setForeground(new java.awt.Color(204, 255, 255));
        lblMaNV.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMaNV.setText("[Mã Nhân Viên]");

        jTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField3.setText("jTextField3");

        javax.swing.GroupLayout pnlSDM1Layout = new javax.swing.GroupLayout(pnlSDM1);
        pnlSDM1.setLayout(pnlSDM1Layout);
        pnlSDM1Layout.setHorizontalGroup(
            pnlSDM1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSDM1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSDM1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSDM1Layout.createSequentialGroup()
                        .addComponent(pnlButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSDM1Layout.createSequentialGroup()
                        .addGroup(pnlSDM1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSDM1Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addGroup(pnlSDM1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlSDM1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlSDM1Layout.createSequentialGroup()
                                        .addGap(245, 245, 245)
                                        .addGroup(pnlSDM1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSDM1Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSDM1Layout.createSequentialGroup()
                        .addGroup(pnlSDM1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblMaNV, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblMaHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlSDM1Layout.createSequentialGroup()
                                .addComponent(lblNgayGio, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        pnlSDM1Layout.setVerticalGroup(
            pnlSDM1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSDM1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(lblNgayGio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMaHD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMaNV)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel20)
                .addGap(18, 18, 18)
                .addGroup(pnlSDM1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlSDM1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlSDM1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addComponent(pnlButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pnlMainTTMLayout = new javax.swing.GroupLayout(pnlMainTTM);
        pnlMainTTM.setLayout(pnlMainTTMLayout);
        pnlMainTTMLayout.setHorizontalGroup(
            pnlMainTTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainTTMLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlSDM1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        pnlMainTTMLayout.setVerticalGroup(
            pnlMainTTMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainTTMLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(pnlSDM1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addComponent(pnlMainSDM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2)
                        .addComponent(pnlMainTTM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlCNNgang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addComponent(pnlCNNgang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlMainSDM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlMainTTM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );

        getContentPane().add(pnlMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1500, 780));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        String maHD = lblMaHD.getText().trim();
        String nguoiTao = CN_TaiKhoanDangNhap.getTenTaiKhoan().trim();

        int tongTienSP = Integer.parseInt(jTextField1.getText().replace(".", "").trim());
        int giamGia = Integer.parseInt(jTextField2.getText().replace(".", "").trim());
        int thanhToan = Integer.parseInt(jTextField3.getText().replace(".", "").trim());

        new TAB2_QR(maHD, nguoiTao, tongTienSP, giamGia, thanhToan, tableOrder, this).setVisible(true);
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void cbxLoaiSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxLoaiSPActionPerformed
        String loai = cbxLoaiSP.getSelectedItem().toString();
        if (loai.equals("Tất cả")) {
            hienThiSanPham(danhSachSanPham);
            return;
        }

        List<Products> loc = new ArrayList<>();
        for (Products sp : danhSachSanPham) {
            if (sp.getLoaiSP().equalsIgnoreCase(loai)) {
                loc.add(sp);
            }
        }

        hienThiSanPham(loc);
    }//GEN-LAST:event_cbxLoaiSPActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed

    }//GEN-LAST:event_jTextField1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JComboBox<String> cbxLoaiSP;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTree jTree1;
    private javax.swing.JLabel lblChat;
    private javax.swing.JLabel lblDM;
    private javax.swing.JLabel lblDX;
    private javax.swing.JLabel lblHD;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblMT;
    private javax.swing.JLabel lblMaHD;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblNgayGio;
    private javax.swing.JLabel lblOrder;
    private javax.swing.JLabel lblSP;
    private javax.swing.JLabel lblTB;
    private javax.swing.JLabel lblTKe;
    private javax.swing.JLabel lblTKhoan;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlCN;
    private javax.swing.JPanel pnlCNNgang;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMainSDM;
    private javax.swing.JPanel pnlMainTTM;
    private javax.swing.JPanel pnlSDM;
    private javax.swing.JPanel pnlSDM1;
    private javax.swing.JPanel pnlSP;
    private javax.swing.JTable tableOrder;
    private javax.swing.JLabel txtSDM;
    // End of variables declaration//GEN-END:variables
}
