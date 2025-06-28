package View.Code;

import Controller.DBConnection;
import java.awt.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

public class TAB1_LoadSDM {

    public static void LoadSoDoMay(JPanel pnlSDM) {
        ArrayList<Computer> dsMay = LayDanhSachMay();

        long tong = dsMay.size();
        long trong = dsMay.stream().filter(m -> m.trangThai == 1).count();
        long dangDung = tong - trong;

        pnlSDM.removeAll();
        pnlSDM.setBackground(new Color(30, 30, 47));

        // Tiêu đề
        JLabel lblTitle = new JLabel("SƠ ĐỒ MÁY", SwingConstants.CENTER);
        lblTitle.setBounds(0, 25, 797, 30);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        pnlSDM.add(lblTitle);

        // Thống kê
        int labelW = 220;
        int gap = 40;
        int totalWidth = 3 * labelW + 2 * gap;
        int startX = (797 - totalWidth) / 2 + 40; // căn giữa 3 lbl, đẩy qua phải 40px
        int yThongKe = 80; // khoảng cách dòng giữa Title với thống kê

        JLabel lblTong = createStatLabel("icTSM.png", "Tổng số máy: " + tong, startX, yThongKe);
        JLabel lblTrong = createStatLabel("Inactive.png", "Máy đang trống: " + trong, startX + labelW + gap, yThongKe);
        JLabel lblDangDung = createStatLabel("Active.png", "Máy đang sử dụng: " + dangDung, startX + 2 * (labelW + gap), yThongKe);

        pnlSDM.add(lblTong);
        pnlSDM.add(lblTrong);
        pnlSDM.add(lblDangDung);

        // Sơ đòo máy
        int cols = 5;
        int itemW = 90, itemH = 90;
        int gapX = 150; // khoảng cách ngang
        int gapY = 150; // giãn dòng máy
        int panelWidth = 797;
        int widthAll = (cols - 1) * gapX + itemW;
        int offsetX = (panelWidth - widthAll) / 2;
        int startY = yThongKe + 90; // khoảng cách bên dưới thống kê

        int i = 0;
        for (Computer may : dsMay) {
            int col = i % cols;
            int row = i / cols;
            int x = offsetX + col * gapX;
            int y = startY + row * gapY;

            JLabel lblMay = new JLabel(
                may.ten,
                LoadIcon(may.trangThai == 1 ? "Inactive.png" : "Active.png", 72), // Kích cỡ icon = 72
                JLabel.CENTER
            );
            lblMay.setBounds(x, y, itemW, itemH);
            lblMay.setHorizontalTextPosition(JLabel.CENTER);
            lblMay.setVerticalTextPosition(JLabel.BOTTOM);
            lblMay.setForeground(Color.WHITE);
            lblMay.setFont(new Font("Segoe UI", Font.BOLD, 12));
            pnlSDM.add(lblMay);
            i++;
        }

        pnlSDM.revalidate();
        pnlSDM.repaint();
    }

    private static JLabel createStatLabel(String iconName, String text, int x, int y) {
        JLabel label = new JLabel(text, LoadIcon(iconName, 24), JLabel.LEFT);
        label.setBounds(x, y, 220, 24);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setHorizontalTextPosition(SwingConstants.RIGHT);
        label.setVerticalTextPosition(SwingConstants.CENTER);
        return label;
    }

    private static ImageIcon LoadIcon(String iconName, int size) {
        String path = "/Assets/Icons/DM/" + iconName;
        URL url = TAB1_LoadSDM.class.getResource(path);
        if (url == null) {
            System.err.println("Không tìm thấy icon: " + path);
            return new ImageIcon();
        }
        Image img = new ImageIcon(url).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private static class Computer {
        String ten;
        int trangThai;

        public Computer(String ten, int trangThai) {
            this.ten = ten;
            this.trangThai = trangThai;
        }
    }

    private static ArrayList<Computer> LayDanhSachMay() {
        ArrayList<Computer> ds = new ArrayList<>();
        try (Connection conn = DBConnection.connect()) {
            if (conn == null) {
                System.out.println("Không thể kết nối DB");
                return ds;
            }
            String sql = "SELECT NameComputer, ComputerStatus FROM Computer";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(new Computer(rs.getString("NameComputer"), rs.getInt("ComputerStatus")));
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy dữ liệu máy: " + e.getMessage());
        }
        return ds;
    }
}
