package ViewAD.Code;

import Controller.DBConnection;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

public class TAB4_LoadMayTinh {

    public static void LoadSoDoMay(JPanel pnlSDM,
                                   JLabel lblTenMay,
                                   JLabel lblTrangThai,
                                   JLabel lblCPU,
                                   JLabel lblRAM,
                                   JLabel lblGPU,
                                   JLabel lblMonitor,
                                   JLabel lblGia) {

        ArrayList<Computer> dsMay = LayDanhSachMay();

        pnlSDM.removeAll();
        pnlSDM.setBackground(new Color(30, 30, 47));

        // Reset thông tin chi tiết máy
        lblTenMay.setText("");
        lblTrangThai.setText("");
        lblCPU.setText("");
        lblRAM.setText("");
        lblGPU.setText("");
        lblMonitor.setText("");
        lblGia.setText("");

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
        int startX = (797 - totalWidth) / 2 + 40;
        int yThongKe = 80;

        long tong = dsMay.size();
        long hoatDong = dsMay.stream().filter(m -> m.trangThai == 0 || m.trangThai == 1).count();
        long baoTri = dsMay.stream().filter(m -> m.trangThai == 2).count();

        JLabel lblTong = createStatLabel("icTSM.png", "TỔNG: " + tong, startX, yThongKe);
        JLabel lblHoatDong = createStatLabel("Inactive.png", "HOẠT ĐỘNG: " + hoatDong, startX + labelW + gap, yThongKe);
        JLabel lblBaoTri = createStatLabel("Maintenance.png", "BẢO TRÌ: " + baoTri, startX + 2 * (labelW + gap), yThongKe);

        pnlSDM.add(lblTong);
        pnlSDM.add(lblHoatDong);
        pnlSDM.add(lblBaoTri);

        // Hiển thị sơ đồ máy
        int cols = 5;
        int itemW = 90, itemH = 90;
        int gapX = 150;
        int gapY = 150;
        int panelWidth = 797;
        int widthAll = (cols - 1) * gapX + itemW;
        int offsetX = (panelWidth - widthAll) / 2;
        int startY = yThongKe + 90;

        int i = 0;
        for (Computer may : dsMay) {
            int col = i % cols;
            int row = i / cols;
            int x = offsetX + col * gapX;
            int y = startY + row * gapY;

            String iconPath;
            String trangThaiHienThi;

            if (may.trangThai == 2) {
                iconPath = "E:/SU25/BL2/CyberCafe4KL/CyberCafe4KL/src/Assets/Icons/DM/Maintenance.png";
                trangThaiHienThi = "Máy đang bảo trì";
            } else {
                iconPath = "E:/SU25/BL2/CyberCafe4KL/CyberCafe4KL/src/Assets/Icons/DM/Inactive.png";
                trangThaiHienThi = "Máy đang hoạt động";
            }

            JLabel lblMay = new JLabel(may.ten, LoadIcon(iconPath, 72), JLabel.CENTER);
            lblMay.setBounds(x, y, itemW, itemH);
            lblMay.setHorizontalTextPosition(JLabel.CENTER);
            lblMay.setVerticalTextPosition(JLabel.BOTTOM);
            lblMay.setForeground(Color.WHITE);
            lblMay.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblMay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Click hiển thị thông tin máy
            lblMay.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    lblTenMay.setText(may.ten);
                    lblTrangThai.setText(trangThaiHienThi);
                    lblCPU.setText(may.cpu);
                    lblRAM.setText(may.ram);
                    lblGPU.setText(may.gpu);
                    lblMonitor.setText(may.monitor);
                    lblGia.setText(may.donGia + " đ/phút");
                }
            });

            pnlSDM.add(lblMay);
            i++;
        }

        pnlSDM.revalidate();
        pnlSDM.repaint();
    }

    private static JLabel createStatLabel(String iconFileName, String text, int x, int y) {
        String absPath = "E:/SU25/BL2/CyberCafe4KL/CyberCafe4KL/src/Assets/Icons/DM/" + iconFileName;
        JLabel label = new JLabel(text, LoadIcon(absPath, 24), JLabel.LEFT);
        label.setBounds(x, y, 220, 24);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setHorizontalTextPosition(SwingConstants.RIGHT);
        label.setVerticalTextPosition(SwingConstants.CENTER);
        return label;
    }

    private static ImageIcon LoadIcon(String absolutePath, int size) {
        Image image = new ImageIcon(absolutePath).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    public static class Computer {
        public String ten, cpu, ram, gpu, monitor;
        public int trangThai;
        public int donGia;

        public Computer(String ten, String cpu, String ram, String gpu, String monitor, int trangThai, int donGia) {
            this.ten = ten;
            this.cpu = cpu;
            this.ram = ram;
            this.gpu = gpu;
            this.monitor = monitor;
            this.trangThai = trangThai;
            this.donGia = donGia;
        }
    }

    private static ArrayList<Computer> LayDanhSachMay() {
        ArrayList<Computer> ds = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Không thể kết nối DB");
                return ds;
            }

            String sql = "SELECT NameComputer, CPU, RAM, GPU, Monitor, ComputerStatus, PricePerMinute FROM Computer WHERE ComputerStatus IN (0, 1, 2)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ds.add(new Computer(
                        rs.getString("NameComputer"),
                        rs.getString("CPU"),
                        rs.getString("RAM"),
                        rs.getString("GPU"),
                        rs.getString("Monitor"),
                        rs.getInt("ComputerStatus"),
                        (int) rs.getDouble("PricePerMinute")
                ));
            }

        } catch (Exception e) {
            System.out.println("Lỗi lấy dữ liệu máy: " + e.getMessage());
        }
        return ds;
    }
}
