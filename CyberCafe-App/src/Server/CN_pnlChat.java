package Server;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CN_pnlChat {

    public static void LoadDanhSachMay(JPanel pnlSender, List<ThongTinMay> danhSach) {
        pnlSender.removeAll();
        pnlSender.setLayout(new BoxLayout(pnlSender, BoxLayout.Y_AXIS));
        pnlSender.setBackground(new Color(153, 255, 255)); // Màu nền giống ảnh

        for (ThongTinMay may : danhSach) {
            JPanel pnlItem = new JPanel(null);
            pnlItem.setPreferredSize(new Dimension(252, 45)); // 239 + 13
            pnlItem.setBackground(new Color(30, 30, 47));
            pnlItem.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(204, 255, 255)));

            JLabel lblTenMay = new JLabel(may.getTenMay() + ": Order mới");
            lblTenMay.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblTenMay.setForeground(new Color(153, 255, 255));
            lblTenMay.setBounds(13, 10, 180, 25); // Cách trái 13px

            JLabel lblGio = new JLabel(may.getGioGui());
            lblGio.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblGio.setForeground(new Color(153, 255, 255));
            lblGio.setBounds(252 - 55, 10, 50, 25); // Cách phải 13px

            pnlItem.add(lblTenMay);
            pnlItem.add(lblGio);

            pnlSender.add(pnlItem);
        }

        pnlSender.revalidate();
        pnlSender.repaint();
    }

    public static class ThongTinMay {
        private String tenMay;
        private String gioGui;

        public ThongTinMay(String tenMay, String gioGui) {
            this.tenMay = tenMay;
            this.gioGui = gioGui;
        }

        public String getTenMay() {
            return tenMay;
        }

        public String getGioGui() {
            return gioGui;
        }
    }
}
