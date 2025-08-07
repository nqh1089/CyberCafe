package ViewAD.Code;

import Controller.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class CN_Dropdown {

    private static final HashSet<String> displayedMessages = new HashSet<>();
    private static Timer autoUpdateTimer;
    private static boolean isDropdownVisible = false;

    private static JPanel pnlThongBao;
    private static JScrollPane scrollPaneThongBao;

    public static void initThongBaoIfNeeded(JFrame frame, JLabel lblTB) {
        if (pnlThongBao == null || scrollPaneThongBao == null) {
            pnlThongBao = new JPanel();
            pnlThongBao.setBackground(new Color(44, 44, 62));
            pnlThongBao.setLayout(new BoxLayout(pnlThongBao, BoxLayout.Y_AXIS));
            pnlThongBao.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel lblTitle = new JLabel("THÔNG BÁO");
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblTitle.setForeground(Color.CYAN);
            lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
            pnlThongBao.add(lblTitle);

            scrollPaneThongBao = new JScrollPane(pnlThongBao);
            scrollPaneThongBao.setPreferredSize(new Dimension(235, 0));
            scrollPaneThongBao.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPaneThongBao.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPaneThongBao.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 2));
            scrollPaneThongBao.getVerticalScrollBar().setUnitIncrement(16);
            scrollPaneThongBao.setBounds(lblTB.getX(), lblTB.getY() + lblTB.getHeight(), 250, 300);
            scrollPaneThongBao.setVisible(false);

            attachDropdown(lblTB);
            startAutoUpdate();
        }

        // Nếu scrollPane không nằm trong frame hiện tại thì chuyển nó sang
        if (scrollPaneThongBao.getParent() != frame.getLayeredPane()) {
            Container oldParent = scrollPaneThongBao.getParent();
            if (oldParent != null) {
                oldParent.remove(scrollPaneThongBao);
            }
            scrollPaneThongBao.setBounds(lblTB.getX(), lblTB.getY() + lblTB.getHeight(), 250, 300);
            frame.getLayeredPane().add(scrollPaneThongBao, JLayeredPane.POPUP_LAYER);
            frame.getLayeredPane().revalidate();
            frame.getLayeredPane().repaint();
        }
    }

    private static void startAutoUpdate() {
        if (autoUpdateTimer != null) {
            autoUpdateTimer.cancel();
        }

        autoUpdateTimer = new Timer(true);
        autoUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> loadThongBao(pnlThongBao));
            }
        }, 0, 5000);
    }

    private static void attachDropdown(JLabel lblTB) {
        lblTB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isDropdownVisible = !isDropdownVisible;
                scrollPaneThongBao.setVisible(isDropdownVisible);
                if (isDropdownVisible) {
                    loadThongBao(pnlThongBao);
                }
            }
        });
    }

    public static void resetSession() {
        displayedMessages.clear();
        isDropdownVisible = false;
        if (autoUpdateTimer != null) {
            autoUpdateTimer.cancel();
            autoUpdateTimer = null;
        }
        pnlThongBao = null;
        scrollPaneThongBao = null;
    }

    public static void loadThongBao(JPanel pnlThongBao) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                return;
            }

            Timestamp batDau = CN_TaiKhoanDangNhap.getThoiGianDangNhap();
            if (batDau == null) {
                batDau = new Timestamp(System.currentTimeMillis());
            }

            int adminId = CN_TaiKhoanDangNhap.getIDTaiKhoan();
            boolean coThongBaoMoi = false;

            // LOGIN / LOGOUT
            String sqlLog = """
                SELECT C.NameComputer, A.NameAccount, L.ThoiGianBatDau, L.ThoiGianKetThuc
                FROM LogAccess L
                JOIN Computer C ON L.IDComputer = C.IDComputer
                LEFT JOIN Account A ON L.IDAccount = A.IDAccount
                WHERE L.ThoiGianBatDau >= ?
                ORDER BY L.ThoiGianBatDau DESC
            """;

            try (PreparedStatement ps = conn.prepareStatement(sqlLog)) {
                ps.setTimestamp(1, batDau);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String may = rs.getString("NameComputer");
                        String user = rs.getString("NameAccount");
                        Timestamp start = rs.getTimestamp("ThoiGianBatDau");
                        Timestamp end = rs.getTimestamp("ThoiGianKetThuc");

                        String time = formatTime(start);
                        String msg = (end == null) ? ("Đã đăng nhập bởi " + user) : ("Đã đăng xuất bởi " + user);
                        String key = may + "|" + msg + "|" + time;

                        if (!displayedMessages.contains(key)) {
                            displayedMessages.add(key);
                            pnlThongBao.add(createThongBaoItem(may, msg, time, ""), 1); // nếu không có mã hóa đơn
                            coThongBaoMoi = true;
                        }
                    }
                }
            }

            // ORDER
            String sqlOrder = """
    SELECT TOP 20 
        ISNULL(NULLIF(RTRIM(LTRIM(O.Note)), ''), 'Máy khách') AS NameComputer, 
        O.OrderTime,
        O.IDOrder
    FROM OrderFood O
    WHERE O.IDAccount = ? AND O.OrderTime >= ?
    ORDER BY O.OrderTime DESC
""";

            try (PreparedStatement ps = conn.prepareStatement(sqlOrder)) {
                ps.setInt(1, adminId);
                ps.setTimestamp(2, batDau);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String may = rs.getString("NameComputer");
                        Timestamp orderTime = rs.getTimestamp("OrderTime");
                        int idOrder = rs.getInt("IDOrder");
                        String maHD = "HĐ" + idOrder;

                        String time = formatTime(orderTime);
                        String msg = "Order mới";
                        String key = may + "|" + msg + "|" + orderTime.getTime();

                        if (!displayedMessages.contains(key)) {
                            displayedMessages.add(key);
                            pnlThongBao.add(createThongBaoItem(may, msg, time, maHD), 1);
                            coThongBaoMoi = true;
                        }
                    }
                }
            }

            if (coThongBaoMoi) {
                pnlThongBao.revalidate();
                pnlThongBao.repaint();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel createThongBaoItem(String tenMay, String noiDung, String gioPhut, String maHD) {
        JPanel pnlItem = new JPanel(new BorderLayout());
        pnlItem.setBackground(new Color(60, 60, 90)); // Màu mặc định mới
        pnlItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65)); // Chiều cao của 1 lbl TB
        pnlItem.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        pnlItem.putClientProperty("read", false); // Mặc định là chưa đọc

        pnlItem.putClientProperty("maHD", maHD);       // Gán mã hóa đơn (HĐxxx)
        pnlItem.putClientProperty("noiDung", noiDung); // Để phân biệt Order

        // Label tên máy
        JLabel lblTitle = new JLabel(tenMay);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(Color.CYAN);

        // Label nội dung
        JLabel lblDetail = new JLabel("• " + noiDung);
        lblDetail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDetail.setForeground(new Color(200, 200, 200));

        // Box chứa title + detail
        Box box = Box.createVerticalBox();
        box.add(lblTitle);
        box.add(lblDetail);

        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false);
        pnlTop.add(box, BorderLayout.CENTER);

        // Label thời gian
        JLabel lblTime = new JLabel(gioPhut);
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTime.setForeground(Color.LIGHT_GRAY);
        lblTime.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setOpaque(false);
        pnlBottom.add(lblTime, BorderLayout.EAST);

        pnlItem.add(pnlTop, BorderLayout.CENTER);
        pnlItem.add(pnlBottom, BorderLayout.SOUTH);

        // Màu theo yêu cầu
        Color bgDefault = new Color(60, 60, 90);   // Chưa đọc
        Color bgHover = new Color(30, 30, 47);     // Hover
        Color bgRead = new Color(50, 50, 70);      // Đã đọc

        pnlItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                pnlItem.setBackground(bgHover);
                pnlItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boolean isRead = Boolean.TRUE.equals(pnlItem.getClientProperty("read"));
                pnlItem.setBackground(isRead ? bgRead : bgDefault);
                pnlItem.setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                pnlItem.setBackground(bgRead);
                pnlItem.putClientProperty("read", true);

                String noiDung = (String) pnlItem.getClientProperty("noiDung");
                if ("Order mới".equals(noiDung) && e.getClickCount() == 2) {
                    String maHD = (String) pnlItem.getClientProperty("maHD");
                    if (maHD != null && !maHD.isEmpty()) {
                        new TAB5_ChiTietHD(maHD).setVisible(true);
                    }
                }
            }
        });

        return pnlItem;
    }

    private static String formatTime(Timestamp timestamp) {
        if (timestamp == null) {
            return "--:--";
        }
        return new SimpleDateFormat("HH:mm").format(timestamp);
    }
}

//package ViewAD.Code;
//
//import Controller.DBConnection;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//
//public class CN_Dropdown {
//
//    // Tạo panel chứa danh sách thông báo
//    public static JPanel createThongBaoPanel() {
//        JPanel pnlDanhSachMay = new JPanel();
//        pnlDanhSachMay.setBackground(new Color(44, 44, 62));
//        pnlDanhSachMay.setLayout(new BoxLayout(pnlDanhSachMay, BoxLayout.Y_AXIS));
//        pnlDanhSachMay.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        JLabel lblTitle = new JLabel("THÔNG BÁO");
//        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
//        lblTitle.setForeground(Color.CYAN);
//        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
//        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
//
//        pnlDanhSachMay.add(lblTitle);
//
//        return pnlDanhSachMay;
//    }
//
//    // Tạo JScrollPane bọc panel thông báo
//    public static JScrollPane createScrollPane(JPanel pnlThongBao) {
//        JScrollPane scrollPane = new JScrollPane(pnlThongBao);
//        scrollPane.setPreferredSize(new Dimension(235, 0));
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 2));
//        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
//        scrollPane.setVisible(false);
//        return scrollPane;
//    }
//
//    // Gán sự kiện toggle cho lblTB
//    public static void attachDropdown(JLabel lblTB, JScrollPane scrollPane, JPanel pnlThongBao) {
//        lblTB.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                boolean isVisible = scrollPane.isVisible();
//                scrollPane.setVisible(!isVisible);
//                if (!isVisible) {
//                    loadThongBao(pnlThongBao);
//                }
//            }
//        });
//    }
//
//    // Load dữ liệu thông báo từ DB
//    public static void loadThongBao(JPanel pnlThongBao) {
//        pnlThongBao.removeAll();
//
//        JLabel lblTitle = new JLabel("THÔNG BÁO");
//        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
//        lblTitle.setForeground(Color.CYAN);
//        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
//        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
//        pnlThongBao.add(lblTitle);
//
//        try (Connection conn = DBConnection.getConnection()) {
//            if (conn == null) return;
//
//            // LOGIN/LOGOUT
//            String sqlLog = """
//                SELECT TOP 10 C.NameComputer, A.NameAccount, L.ThoiGianBatDau, L.ThoiGianKetThuc
//                FROM LogAccess L
//                JOIN Computer C ON L.IDComputer = C.IDComputer
//                LEFT JOIN Account A ON L.IDAccount = A.IDAccount
//                ORDER BY L.ThoiGianBatDau DESC
//            """;
//            PreparedStatement ps = conn.prepareStatement(sqlLog);
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                String may = rs.getString("NameComputer");
//                String user = rs.getString("NameAccount");
//                Timestamp start = rs.getTimestamp("ThoiGianBatDau");
//                Timestamp end = rs.getTimestamp("ThoiGianKetThuc");
//
//                String time = formatTime(start);
//                String msg = (end == null) ? ("Đã đăng nhập bởi " + user) : ("Đã đăng xuất bởi " + user);
//                pnlThongBao.add(createThongBaoItem(may, msg, time));
//            }
//            rs.close();
//            ps.close();
//
//            // ORDER
//            String sqlOrder = """
//                SELECT TOP 10 C.NameComputer, A.NameAccount, O.OrderTime
//                FROM OrderFood O
//                JOIN Account A ON O.IDAccount = A.IDAccount
//                JOIN LogAccess L ON A.IDAccount = L.IDAccount AND L.ThoiGianKetThuc IS NULL
//                JOIN Computer C ON C.IDComputer = L.IDComputer
//                ORDER BY O.OrderTime DESC
//            """;
//            ps = conn.prepareStatement(sqlOrder);
//            rs = ps.executeQuery();
//
//            while (rs.next()) {
//                String may = rs.getString("NameComputer");
//                String user = rs.getString("NameAccount");
//                Timestamp timeOrder = rs.getTimestamp("OrderTime");
//                String time = formatTime(timeOrder);
//                String msg = "Order món bởi " + user;
//                pnlThongBao.add(createThongBaoItem(may, msg, time));
//            }
//
//            rs.close();
//            ps.close();
//
//            pnlThongBao.revalidate();
//            pnlThongBao.repaint();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Tạo từng dòng thông báo
//    public static JPanel createThongBaoItem(String tenMay, String noiDung, String gioPhut) {
//        JPanel pnlItem = new JPanel(new BorderLayout());
//        pnlItem.setBackground(new Color(54, 54, 74));
//        pnlItem.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
//
//        JLabel lblTitle = new JLabel("\uD83D\uDDA5 " + tenMay);
//        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
//        lblTitle.setForeground(Color.CYAN);
//
//        JLabel lblDetail = new JLabel("• " + noiDung + " lúc " + gioPhut);
//        lblDetail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//        lblDetail.setForeground(new Color(200, 200, 200));
//
//        Box box = Box.createVerticalBox();
//        box.add(lblTitle);
//        box.add(lblDetail);
//
//        pnlItem.add(box, BorderLayout.CENTER);
//        return pnlItem;
//    }
//
//    // Format giờ:phút
//    private static String formatTime(Timestamp timestamp) {
//        if (timestamp == null) return "--:--";
//        return new SimpleDateFormat("HH:mm").format(timestamp);
//    }
//}
